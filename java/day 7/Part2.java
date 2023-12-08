import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

enum Type {

  NOT_FOUND(7),
  FIVE_OF_A_KIND(0),
  FOUR_OF_A_KIND(1),
  FULL_HOUSE(2),
  THREE_OF_A_KIND(3),
  TWO_PAIR(4),
  ONE_PAIR(5),
  HIGH_CARD(6);

  Type(int value) {
  }
}

class Hand implements Comparable<Hand> {
  String combination;
  Long bet;
  Type handType;

  Hand(String combination, Long bet) {
    this.combination = combination;
    this.bet = bet;
    this.handType = determineHandType(combination);
  }

  int compareDistinctAlphabets(final char orig, final char other) {
    // if it was joker for any of the two, return the value to show it was smaller
    // or larger
    if (orig == 'J') {
      return 1;
    } else if (other == 'J') {
      return -1;

      // any others just return the order as per shown
    } else if (orig == 'A' && other != 'A') {
      return -1;
    } else if (orig == 'K') {
      if (other == 'A')
        return 1;
      return -1;
    } else if (orig == 'Q') {
      if (other == 'A' || other == 'K')
        return 1;
      return -1;
    } else if (orig == 'T') {
      if (other == 'A' || other == 'K' || other == 'Q')
        return 1;
      return -1;
    }
    return other - orig;
  }

  Map<Character, Integer> extractCounts(final String combination) {
    Map<Character, Integer> checkedLetters = new HashMap<>();

    for (int i = 0; i < combination.length(); i++) {
      char currentChar = combination.charAt(i);

      // determine if this is unique
      int count = checkedLetters.getOrDefault(currentChar, 0);
      checkedLetters.put(currentChar, count + 1);
    }

    // determine if there are any Js
    if (checkedLetters.containsKey('J')) {
      int countJ = checkedLetters.get('J');
      // find the key with the largest number of matches and add to it
      char largestMatchedKey = 'J';
      int largestCount = -1;
      for (Character key : checkedLetters.keySet()) {
        // skip checking if the current key is J
        if (key == 'J')
          continue;

        // note down what was the largest
        int count = checkedLetters.get(key);
        if (count > largestCount) {
          largestMatchedKey = key;
          largestCount = count;
        } else if (count == largestCount && compareDistinctAlphabets(key, largestMatchedKey) < 0) {
          largestMatchedKey = key;
        }
      }

      // in case it was a full J
      if (countJ == 5) {
        largestMatchedKey = 'K';
        largestCount = 0;
      }

      // replace the counts
      checkedLetters.remove('J');
      checkedLetters.put(largestMatchedKey, largestCount + countJ);
    }

    return checkedLetters;

  }

  Type determineHandType(final String combination) {

    Map<Character, Integer> checkedLetters = extractCounts(combination);

    // determine how many characters are there
    Set<Character> matchedCharacters = checkedLetters.keySet();
    int numUniqueCharacters = matchedCharacters.size();

    // perform the checking
    if (numUniqueCharacters == 1) {
      return Type.FIVE_OF_A_KIND;
    } else if (numUniqueCharacters == 4) {
      return Type.ONE_PAIR;
    } else if (numUniqueCharacters == 5) {
      return Type.HIGH_CARD;
    }

    boolean hasTwo = false;
    boolean hasAnotherTwo = false;
    boolean hasThree = false;
    boolean hasFour = false;

    for (Character currentMatch : matchedCharacters) {
      // get the count
      int matchCount = checkedLetters.get(currentMatch);

      if (matchCount == 2 && hasTwo) {
        hasAnotherTwo = true;
      } else if (matchCount == 2) {
        hasTwo = true;
      } else if (matchCount == 3) {
        hasThree = true;
      } else if (matchCount == 4) {
        hasFour = true;
      }
    }

    if (hasFour) {
      return Type.FOUR_OF_A_KIND;
    } else if (hasThree && hasTwo) {
      return Type.FULL_HOUSE;
    } else if (hasThree) {
      return Type.THREE_OF_A_KIND;
    } else if (hasAnotherTwo && hasTwo) {
      return Type.TWO_PAIR;
    } else if (hasTwo) {
      return Type.ONE_PAIR;
    }

    System.out.println("error");
    return Type.NOT_FOUND;

  }

  Long getBet() {
    return bet;
  }

  @Override
  public int compareTo(Hand o) {
    if (this.handType.equals(o.handType)) {
      for (int i = 0; i < 5; i++) {
        char originalChar = this.combination.charAt(i);
        char otherChar = o.combination.charAt(i);
        if (originalChar == otherChar)
          continue;
        return compareDistinctAlphabets(originalChar, otherChar);
      }
    }

    return this.handType.compareTo(o.handType);
  }

  public String toString() {
    return this.combination + " " + this.handType.toString();
  }
}

public class Part2 {

  // function to read the file and get the total winning
  public static long getTotalWinnings(final String fileNameAndPath) {

    long totalSum = 0;

    PriorityQueue<Hand> pq = new PriorityQueue<>();

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      while (sc.hasNext()) {

        // note the current string
        String currentHand = sc.next();
        String[] handTokens = currentHand.split("[\\s]+");

        // create a hand object
        Hand hand = new Hand(handTokens[0], Long.parseLong(handTokens[1]));
        pq.add(hand);

      }

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    int rank = pq.size();

    while (!pq.isEmpty()) {
      Hand largest = pq.poll();
      long betReturns = rank * largest.getBet();
      totalSum += betReturns;
      rank--;
    }
    return totalSum;
  }

  public static void main(String[] args) {

    // read file
    // String fileNameAndPath = "./test.txt";
    String fileNameAndPath = "./input.txt";

    long totalWinnings = getTotalWinnings(fileNameAndPath);

    System.out.println(totalWinnings);
  }

}
