import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Part2 {
  
  public static Set<Integer> extractHashSetFromArray(final String[] array) {

    Set<Integer> extractedSet = Arrays.stream(array)
        .filter(s -> s.matches("\\d+"))
        .map(Integer::parseInt)
        .collect(Collectors.toCollection(HashSet::new));

    return extractedSet;
  }

  public static int getWinsOfCard(String[] cardTokens) {
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

    return matches;
  }

  public static int getTotalNumScratchCards(final String fileNameAndPath) {

    int totalSum = 0;
    Map<Integer, Integer> scratchCardCountMap = new HashMap<>();

    int cardNumber = 1;
    
    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      // while there is a next element
      while (sc.hasNext()) {

        // note the current card information
        String cardInformation = sc.next();
        String[] tokens = cardInformation.split("[:|]");

        // determine how many of the current cards there are
        int copiesOfCurrentCard = scratchCardCountMap.getOrDefault(cardNumber, 0) + 1;
        totalSum += copiesOfCurrentCard;

        int numWins = getWinsOfCard(tokens);

        // record how many additions of the subsequent cards are found
        for (int i = 1; i <= numWins; i++) {
          int copiesOfCardAfterCurrent = scratchCardCountMap.getOrDefault(cardNumber + i, 0) + copiesOfCurrentCard;
          scratchCardCountMap.put(cardNumber + i, copiesOfCardAfterCurrent);
        }

        cardNumber++;
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

    int totalNumScratchCards = getTotalNumScratchCards(fileNameAndPath);

    System.out.println(totalNumScratchCards);
  }
}
