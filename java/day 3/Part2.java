import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part2 {

  public static long findPartSize(final int horizontalIndex, final int verticalIndex,
      final List<String> engineSchematixMatrix) {

    // get the current string
    String currentSearchString = engineSchematixMatrix.get(horizontalIndex);

    // find the leftest part
    int leftIndex = verticalIndex;
    while (leftIndex - 1 >= 0 && Character.isDigit(currentSearchString.charAt(leftIndex - 1))) {
      leftIndex--;
    }

    // find the rightest part
    int rightIndex = verticalIndex;
    while (rightIndex + 1 < currentSearchString.length()
        && Character.isDigit(currentSearchString.charAt(rightIndex + 1))) {
      rightIndex++;
    }
    
    return Integer.parseInt(currentSearchString.substring(leftIndex, rightIndex + 1));
  }

  public static long getGearRatio(final String line, final int lineIndex, final int gearIndex,
      final List<String> engineSchematicMatrix) {
    long ratio = 1;

    int numParts = 0;

    // determine what are the boundaries of the box around the gear
    int leftestIndex = gearIndex;
    if (leftestIndex - 1 >= 0) {
      leftestIndex -= 1;
    }

    int rightestIndex = gearIndex;
    if (rightestIndex + 1 < line.length()) {
      rightestIndex += 1;
    }

    int toppestIndex = lineIndex;
    if (toppestIndex - 1 >= 0) {
      toppestIndex -= 1;
    }

    int bottomIndex = lineIndex;
    if (bottomIndex + 1 < engineSchematicMatrix.size()) {
      bottomIndex += 1;
    }

    // set the variables for use when searching
    int horizontalIndex = toppestIndex;
    int verticalIndex = leftestIndex;

    boolean isFoundDigit = false;
    boolean isDoneFinding = false;

    while (horizontalIndex <= bottomIndex) {
      char checkingForDigit = engineSchematicMatrix.get(horizontalIndex).charAt(verticalIndex);

      // find for the entire string of digits within the box around the gear
      while (Character.isDigit(checkingForDigit) && !isDoneFinding) {
        // set the foundDigit to true for extracting the full value later on
        // and shift the vertical pointer
        isFoundDigit = true;
        verticalIndex++;

        // prevent searching beyond the box
        if (verticalIndex > rightestIndex) {
          isDoneFinding = true;

        // determine if the current part within the box is still a digit
        } else {
          checkingForDigit = engineSchematicMatrix.get(horizontalIndex).charAt(verticalIndex);
        }
      }

      // if the digit was found previously extract the digit
      if (isFoundDigit) {
        // reset the counts
        numParts++;
        verticalIndex--;

        // determine the value
        ratio *= findPartSize(horizontalIndex, verticalIndex, engineSchematicMatrix);

        // reset the checks
        isDoneFinding = false;
        isFoundDigit = false;
      }

      // move the pointer
      if (verticalIndex == rightestIndex) {
        verticalIndex = leftestIndex;
        horizontalIndex++;
      } else {
        verticalIndex++;
      }
    }

    if (numParts != 2)
      return 0;
    return ratio;

  }

  public static long getGearRatioOfCurrentLine(final String line, final int lineIndex,
      final List<String> engineSchematicMatrix) {

    long sum = 0;

    // loop through the entire string and look for a gear
    for (int i = 0; i < line.length(); i++) {
      char currentChar = line.charAt(i);

      if (currentChar == '*') {
        // determine the gear ratio
        sum += getGearRatio(line, lineIndex, i, engineSchematicMatrix);
      }
    }
    return sum;
  }

  // function to read the file
  public static long getTotalGearRatio(final String fileNameAndPath) {

    long totalSum = 0;
    List<String> engineSchematicMatrix = new ArrayList<>();

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // while there is a next element
      while (sc.hasNext()) {

        String information = sc.next();

        // append to the map
        engineSchematicMatrix.add(information);
      }

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    for (int i = 0; i < engineSchematicMatrix.size(); i++) {
      totalSum += getGearRatioOfCurrentLine(engineSchematicMatrix.get(i), i, engineSchematicMatrix);
    }
    return totalSum;
  }

  public static void main(String[] args) {

    // read file
    String fileNameAndPath = "./input.txt";
    // String fileNameAndPath = "./test.txt";

    long totalGearRatio = getTotalGearRatio(fileNameAndPath);

    System.out.println(totalGearRatio);
  }
}
