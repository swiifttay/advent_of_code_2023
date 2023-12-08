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

  Type determineHandType(final String combination) {

    Map<Character, Integer> checkedLetters = new HashMap<>();

    for (int i = 0; i < combination.length(); i++) {
      char currentChar = combination.charAt(i);

      // determine if this is unique
      int count = checkedLetters.getOrDefault(currentChar, 0);
      checkedLetters.put(currentChar, count + 1);
    }

    // determine how many characters are there
    Set<Character> matchedCharacters = checkedLetters.keySet();
    int numUniqueCharacters = matchedCharacters.size();

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
    // if the type is the same, keep checking the chracter sequence
    if (this.handType.equals(o.handType)) {
      for (int i = 0; i < 5; i++) {
        char originalChar = this.combination.charAt(i);
        char otherChar = o.combination.charAt(i);
        // skip the checking if the two characters are the same
        if (originalChar == otherChar) continue;
        if (originalChar == 'A' && otherChar != 'A')  {
          return -1;
        } else if (originalChar == 'K') {
          if (otherChar == 'A') return 1;
          return -1;
        } else if (originalChar == 'Q') {
          if (otherChar == 'A' || otherChar == 'K') return 1;
          return -1;
        } else if (originalChar == 'J') {
          if (otherChar == 'A' || otherChar == 'K' || otherChar == 'Q') return 1;
          return -1;
        } else if (originalChar == 'T') {
          if (otherChar == 'A' || otherChar == 'K' || otherChar == 'Q' || otherChar == 'J') return 1;
          return -1;
        } 

        // if it was not an alphabet, compare the digits as per usual
        return otherChar - originalChar;
      }
    }

    return this.handType.compareTo(o.handType);
  }

  // for debugging
  public String toString() {
    return this.combination + " " + this.handType.toString();
  }
}

public class Part1 {

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

    // keep getting the largest possible and get the returns
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
