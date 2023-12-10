import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Part1 {
  
  // function to read the file and get the number of steps needed
  public static int getTotalSteps(final String fileNameAndPath) {

    int stepCount = 0;
    String currentLocation = "AAA";

    String steps = new String();
    int stepsSequenceLength = 0;
    Map<String, List<String>> directionMap = new HashMap<>();

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // note the steps
      steps = sc.next();
      stepsSequenceLength = steps.length();
      sc.next(); // remove the empty line

      // insert all the details into a map
      while (sc.hasNext()) {
        String locationDetail = sc.next();
        String[] locationTokens = locationDetail.split("[=(),\\s]+");
        directionMap.put(locationTokens[0], List.of(locationTokens[1], locationTokens[2]));
      }

      // loop through the sequence of steps provided
      while (!currentLocation.equals("ZZZ")) {
        char direction = steps.charAt(stepCount % stepsSequenceLength);
        List<String> currentLocationSides = directionMap.get(currentLocation);
        if (direction == 'L') {
          currentLocation = currentLocationSides.get(0);
        } else {
          currentLocation = currentLocationSides.get(1);
        }
        stepCount++;
      }
      
      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    return stepCount;
  }

  public static void main(String[] args) {

    // read file
    // String fileNameAndPath = "./test.txt";
    String fileNameAndPath = "./input.txt";

    int totalSteps = getTotalSteps(fileNameAndPath);

    System.out.println(totalSteps);
  }
}
