import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Part1 {

  public static Set<Integer> extractHashSetFromArray(final String[] array) {

    Set<Integer> extractedSet = Arrays.stream(array)
        .filter(s -> s.matches("\\d+"))
        .map(Integer::parseInt)
        .collect(Collectors.toCollection(HashSet::new));

    return extractedSet;
  }

  public static int getScordOfCard(String[] cardTokens) {
    int matches = 0;

    String[] winningNumbersArray = cardTokens[1].split("[\\s]+");
    Set<Integer> winningNumbersSet = extractHashSetFromArray(winningNumbersArray);

    String[] cardNumbersArray = cardTokens[2].split("[\\s]+");
    Set<Integer> cardNumbersSet = extractHashSetFromArray(cardNumbersArray);

    // loop through the entire cardNumbersSet and find it in the winningNumbersSet
    for (Integer value : cardNumbersSet) {
      if (winningNumbersSet.contains(value))
        matches++;
    }

    if (matches > 0)
      return (int) Math.pow(2, matches - 1);

    return matches;
  }

  public static int getTotalWinningSum(final String fileNameAndPath) {

    int totalSum = 0;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // while there is a next element
      while (sc.hasNext()) {

        // note the current card information
        String cardInformation = sc.next();
        String[] tokens = cardInformation.split("[:|]");

        // determine the score of the current card
        totalSum += getScordOfCard(tokens);

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

    int totalWinningSum = getTotalWinningSum(fileNameAndPath);

    System.out.println(totalWinningSum);
  }
}
