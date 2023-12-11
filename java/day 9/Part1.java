import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Part1 {

  public static List<Integer> getDifference(final List<Integer> previousArray) {
    List<Integer> diff = new ArrayList<>();
    int prev = previousArray.get(0);
    int current = previousArray.get(1);
    int firstDiffElement = current - prev;
    diff.add(firstDiffElement);
    boolean isAllSame = true;
    prev = current;

    for (int i = 2; i < previousArray.size(); i++) {
      current = previousArray.get(i);
      int diffElement = current - prev;
      if (isAllSame && diffElement != firstDiffElement) isAllSame = false;

      diff.add(diffElement);
      prev = current;
    }

    if (isAllSame) {
      diff = new ArrayList<>();
      diff.add(firstDiffElement);
    };
    return diff;
  }

  public static List<List<Integer>> getDifferences(final String records) {
    String[] recordsTokens = records.split("[\\s]+");
    List<Integer> recordList = Arrays.stream(recordsTokens)
        .map(Integer::parseInt)
        .collect(Collectors.toCollection(ArrayList::new));

    List<List<Integer>> allDifferences = new ArrayList<>();
    allDifferences.add(recordList);
    List<Integer> nextDiff = getDifference(recordList);
    while (nextDiff.size() != 1) {
      allDifferences.add(nextDiff);
      nextDiff = getDifference(nextDiff);
    }
    allDifferences.add(nextDiff);

    return allDifferences;
  }

  public static long getPrediction(final String records) {
    List<List<Integer>> allDifferences = getDifferences(records);
    int numDiffArrays = allDifferences.size();
    long predictionValue = allDifferences.get(numDiffArrays - 1).get(0);

    for (int i = numDiffArrays - 2; i >= 0; i--) {
      List<Integer> currentDiffArray = allDifferences.get(i);
      long oldValue = currentDiffArray.get(currentDiffArray.size() - 1);
      predictionValue += oldValue;
    }

    return predictionValue;
  }

  // function to read the file and get the sum of all predictions
  public static long getSumOfPredictions(final String fileNameAndPath) {

    long totalSum = 0;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      while (sc.hasNext()) {

        // note the record
        String currentRecords = sc.next();

        long prediction = getPrediction(currentRecords);
        totalSum += prediction;
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

    long sumOfPrediction = getSumOfPredictions(fileNameAndPath);

    System.out.println(sumOfPrediction);
  }
}
