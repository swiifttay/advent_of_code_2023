import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Part1 {

  public static int getOptions(final int time, final int distance) {
    int numValids = 0;
    for (int i = 1; i < time; i++) {
      // get the speed
      int speed = i;
      int remainingTime = time - i;
      int travelled = speed * remainingTime;
      if (travelled > distance) numValids++;
    }
    System.out.println(numValids);
    return numValids;
  }

  public static Map<Integer, Integer> extractTimeAndDistance(final String timeInformation,
      final String distanceInformation) {
    String[] timeTokens = timeInformation.split("[\\s]+");
    String[] distanceTokens = distanceInformation.split("[\\s]+");

    Map<Integer, Integer> timeAndDistanceMap = new TreeMap<>();
    for (int i = 1; i < timeTokens.length; i++) {
      Integer time = Integer.parseInt(timeTokens[i]);
      Integer distance = Integer.parseInt(distanceTokens[i]);

      timeAndDistanceMap.put(time, distance);
    }

    return timeAndDistanceMap;
  }

  // function to read the file and get the number of permutations
  public static int getTotalPermutations(final String fileNameAndPath) {

    int totalSum = 1;

    Map<Integer, Integer> timingAndDistanceMap = null;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // note the current game information
      String timeInformation = sc.next();
      String distanceInformation = sc.next();

      timingAndDistanceMap = extractTimeAndDistance(timeInformation, distanceInformation);

      System.out.println(timingAndDistanceMap);

      // loop through all the values in the map
      for (Map.Entry<Integer, Integer> mapEntry : timingAndDistanceMap.entrySet()) {
        totalSum *= getOptions(mapEntry.getKey(), mapEntry.getValue());
      }

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

    int totalPermutationNum = getTotalPermutations(fileNameAndPath);

    System.out.println(totalPermutationNum);
  }
}