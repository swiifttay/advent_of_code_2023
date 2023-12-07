import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Part2 {

  // hard coded method
  // TODO: find a mathematical way to solve the question
  public static long getOptions(final long time, final long distance) {
    long starts = 0;
    long end = time;

    boolean isFound = false;
    for (long i = 1; i < time; i++) {
      long currentDist = i * time - i * i;
      if (!isFound && currentDist > distance) {
        starts = i;
        System.out.println(starts);
        isFound = true;
      } else if (isFound && currentDist <= distance) {
        end = i;
        System.out.println(end);
        break;
      }
    }

    return end - starts;
  }

  public static long extractActualValue(final String information) {
    String[] tokens = information.split("[\\s]+");
    StringBuilder actualInformation = new StringBuilder();
    for (int i = 1; i < tokens.length; i++) {
      actualInformation.append(tokens[i]);
    }

    return Long.parseLong(actualInformation.toString());

  }

  // function to read the file and get the number of permutations
  public static long getTotalPermutations(final String fileNameAndPath) {

    long totalSum = 1;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // note the current game information
      String timeInformation = sc.next();
      String distanceInformation = sc.next();

      long actualTime = extractActualValue(timeInformation);
      long actualDistance = extractActualValue(distanceInformation);

      totalSum = getOptions(actualTime, actualDistance);

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    return totalSum;
  }

  public static void main(String[] args) {

    // read file
    // String fileNameAndPath = "./test.txt";
    String fileNameAndPath = "./input.txt";

    long totalPermutationNum = getTotalPermutations(fileNameAndPath);

    System.out.println(totalPermutationNum);
  }
}
