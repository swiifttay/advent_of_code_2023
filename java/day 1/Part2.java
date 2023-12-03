import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Part2 {

  final static List<String> allowedStrings = Arrays.asList(
      "one", "two", "three", "four", "five",
      "six", "seven", "eight", "nine");

  public static int getFirstDigit(final String line) {

    int firstDigit = -1;
    StringBuilder portionBeforeFirstDigit = new StringBuilder();

    for (int i = 0; i < line.length(); i++) {
      char currentChar = line.charAt(i);
      portionBeforeFirstDigit.append(currentChar);
      if (Character.isDigit(currentChar)) {
        firstDigit = currentChar - '0';
        break;
      }
    }

    // determine if there is any words of the digit before this
    int earliestIndex = portionBeforeFirstDigit.length() - 1;
    int digitValue = 1;
    for (String digitWord : allowedStrings) {

      int fromIndex = portionBeforeFirstDigit.indexOf(digitWord);

      if (fromIndex != -1 && fromIndex <= earliestIndex) {
        earliestIndex = fromIndex;
        firstDigit = digitValue;
      }

      digitValue++;
    }

    return firstDigit;
  }

  public static int getLastDigit(final String line) {
    int lastDigit = -1;
    StringBuilder portionBeforeLastDigit = new StringBuilder();

    for (int i = line.length() - 1; i >= 0; i--) {
      char currentChar = line.charAt(i);
      portionBeforeLastDigit.append(currentChar);
      if (Character.isDigit(currentChar)) {
        lastDigit = currentChar - '0';
        break;
      }
    }

    portionBeforeLastDigit = portionBeforeLastDigit.reverse();

    // determine if there is any words of the digit before this
    int latestIndex = 0;
    int digitValue = 1;
    for (String digitWord : allowedStrings) {

      int fromIndex = portionBeforeLastDigit.lastIndexOf(digitWord);

      if (fromIndex != -1 && fromIndex >= latestIndex) {
        latestIndex = fromIndex;
        lastDigit = digitValue;
      }

      digitValue++;
    }
    return lastDigit;
  }

  public static int getCalibrationValue(final String line) {
    int firstNumber = getFirstDigit(line);
    int lastNumber = getLastDigit(line);

    return firstNumber * 10 + lastNumber;
  }

  // function to read the file and get the sum of the calibration value
  public static int getSumOfCalibrationValue(final String fileNameAndPath) {

    int totalSum = 0;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // while there is a next element
      while (sc.hasNext()) {

        // note the string in this line
        String line = sc.next();

        // get the calibration value of this line
        int calibrationValueOfLine = getCalibrationValue(line);

        // add to the totalSum
        totalSum += calibrationValueOfLine;
      }

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    return totalSum;
  }

  public static void main(String[] args) {

    // read file
    String fileNameAndPath = "./input.txt";

    int totalCalibrationSum = getSumOfCalibrationValue(fileNameAndPath);

    System.out.println(totalCalibrationSum);
  }
}
