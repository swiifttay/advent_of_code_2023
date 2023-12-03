import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Part1 {

  // function to get the calibration value of a given line
  public static int getCalibrationValue(final String line) {
    
    // first extracts the numerical values in a string,
    String resultString = line.replaceAll("[a-zA-Z]", "");

    // take the first and last digit then append together
    String calibrationString = resultString.charAt(0) + "" + resultString.charAt(resultString.length() - 1);

    // return by parsing as an Integer
    return Integer.parseInt(calibrationString);
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
    String fileNameAndPath = "./input.txt";

    int totalCalibrationSum = getSumOfCalibrationValue(fileNameAndPath);

    System.out.println(totalCalibrationSum);
  }
}