import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part1 {

  public static boolean isValidPart(final int lineIndex, final int leftIndex, final int rightIndex,
      final List<String> engineSchematicMatrix) {
    int leftCheckIndex = leftIndex;
    int rightCheckIndex = rightIndex;

    // get the regex for any special characters
    String regex = "[^a-zA-Z0-9.]";

    Pattern pattern = Pattern.compile(regex);

    // determine what is the most left possible
    if (leftIndex - 1 >= 0) {
      leftCheckIndex--;
    }

    // determine what is the most right possible
    if (rightIndex + 1 < engineSchematicMatrix.get(0).length()) {
      rightCheckIndex++;
    }

    // determine the line above
    if (lineIndex - 1 >= 0) {
      String rangeCheck = engineSchematicMatrix.get(lineIndex - 1).substring(leftCheckIndex, rightCheckIndex);
      Matcher matcher = pattern.matcher(rangeCheck);
      if (matcher.find())
        return true;
    }

    // determine the line below
    if (lineIndex + 1 < engineSchematicMatrix.size()) {
      String rangeCheck = engineSchematicMatrix.get(lineIndex + 1).substring(leftCheckIndex, rightCheckIndex);
      Matcher matcher = pattern.matcher(rangeCheck);
      if (matcher.find())
        return true;
    }

    // determine if the current or next letter is okay
    String rangeCheck = engineSchematicMatrix.get(lineIndex).substring(leftCheckIndex, rightCheckIndex);
    Matcher matcher = pattern.matcher(rangeCheck);
    if (matcher.find())
      return true;
    return false;
  }

  public static int getSumPartNumOnLine(final String line, final int lineIndex,
      final List<String> engineSchematicMatrix) {
    int sum = 0;

    int startingIndex = 0;
    boolean valueFound = false;
    int currentIndex = 0;

    while (currentIndex < line.length()) {

      // determine if this current char is a proper one
      char currentChar = line.charAt(currentIndex);

      if (currentChar >= '0' && currentChar <= '9') {
      // update the startingIndex if it has yet been set
        if (!valueFound) {
          startingIndex = currentIndex;
          valueFound = true;
        }

      } else {
        // determine if there was any value held
        if (valueFound) {
          if (isValidPart(lineIndex, startingIndex, currentIndex, engineSchematicMatrix)) {
            sum += Integer.parseInt(line.substring(startingIndex, currentIndex));
          }
          valueFound = false;
        } 
      }

      currentIndex++;
    }

    // determine if there was any value held after exiting the loop
    if (valueFound) {
      if (isValidPart(lineIndex, startingIndex, currentIndex, engineSchematicMatrix)) {
        sum += Integer.parseInt(line.substring(startingIndex, currentIndex));
      }
      valueFound = false;
    } 

    return sum;
  }

  // function to read the file
  public static int getTotalPartNumberSum(final String fileNameAndPath) {

    int totalSum = 0;
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
      totalSum += getSumPartNumOnLine(engineSchematicMatrix.get(i), i, engineSchematicMatrix);
    }
    return totalSum;
  }

  public static void main(String[] args) {

    // read file
    String fileNameAndPath = "./input.txt";
    // String fileNameAndPath = "./test.txt";

    int totalPartNumberSum = getTotalPartNumberSum(fileNameAndPath);

    System.out.println(totalPartNumberSum);
  }
}
