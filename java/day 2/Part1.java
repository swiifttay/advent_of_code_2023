import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Part1 {

  public static boolean isValidGame(String[] tokens, Map<String, Integer> configuration) {
    // determine from each one
    for (int i = 1; i < tokens.length; i++) {
      String currentSet = tokens[i];
      String[] setInformation = currentSet.split("[,\\s]+");
      for (int j = 1; j < setInformation.length; j += 2) {
        int availableOfCurrentColour = configuration.get(setInformation[j + 1]);
        if (Integer.parseInt(setInformation[j]) > availableOfCurrentColour) return false;
      }
    }

    return true;
  }
  
  // function to read the file and get the sum of the calibration value
  public static int getGameIDSum(final String fileNameAndPath, final Map<String, Integer> configuration) {

    int totalSum = 0;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // while there is a next element
      while (sc.hasNext()) {

        // note the current game information
        String gameInformation = sc.next();
        String[] tokens = gameInformation.split("[:;]");

        // determine if this game is valid given the details
        if (isValidGame(tokens, configuration)) {
          String gameIdString = tokens[0];
          String[] gameIdInfo = gameIdString.split("[\\s]+");
          totalSum += Integer.parseInt(gameIdInfo[1]);
        }

      }

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    return totalSum;
  }

  public static void main(String[] args) {
    int numRed = 12;
    int numGreen = 13;
    int numBlue = 14;

    Map<String, Integer> configuration = new HashMap<>();
    configuration.put("red", numRed);
    configuration.put("green", numGreen);
    configuration.put("blue", numBlue);

    
    // read file
    String fileNameAndPath = "./input.txt";

    int totalGameSum = getGameIDSum(fileNameAndPath, configuration);

    System.out.println(totalGameSum);
  }
}
