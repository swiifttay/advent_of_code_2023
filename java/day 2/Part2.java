import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Part2 {

  public static int getPowerOfCurrentGame(String[] tokens) {
    
    Map<String, Integer> configuration = new HashMap<>();
    configuration.put("red", 0);
    configuration.put("green", 0);
    configuration.put("blue", 0);

    // determine from each one
    for (int i = 1; i < tokens.length; i++) {
      String currentSet = tokens[i];
      String[] setInformation = currentSet.split("[,\\s]+");
      for (int j = 1; j < setInformation.length; j += 2) {
        int currentPullAmount = Integer.parseInt(setInformation[j]);
        String currentPullColour = setInformation[j + 1];
        
        int availableOfCurrentColour = configuration.get(currentPullColour);

        if (currentPullAmount > availableOfCurrentColour) {
          configuration.put(currentPullColour, currentPullAmount);
        }
      }
    }

    int powerOfCurrentGame = 1;
    powerOfCurrentGame *= configuration.get("red");
    powerOfCurrentGame *= configuration.get("green");
    powerOfCurrentGame *= configuration.get("blue");

    return powerOfCurrentGame;
  }
  
  // function to read the file and get the sum of the calibration value
  public static int getTotalPower(final String fileNameAndPath) {

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
        int powerOfGame = getPowerOfCurrentGame(tokens);
        totalSum += powerOfGame;

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

    int totalPower = getTotalPower(fileNameAndPath);

    System.out.println(totalPower);
  }
}
