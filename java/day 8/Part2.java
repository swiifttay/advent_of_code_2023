import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Vertex {
  String location;
  int stepIndex;

  Vertex(final String location, final int stepIndex) {
    this.location = location;
    this.stepIndex = stepIndex;
  }

  public String getLocation() {
    return location;
  }

  public int getStepIndex() {
    return stepIndex;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Vertex))
      return false;
    Vertex another = (Vertex) obj;
    return this.location.equals(another.location) && this.stepIndex == another.stepIndex;
  }

  @Override
  public String toString() {
    return "Vertex [location=" + location + ", stepIndex=" + stepIndex + "]";
  }

}

class Edge {
  Vertex source;
  Vertex destination;
  int length;

  Edge(final Vertex source, final Vertex destination, final int length) {
    this.source = source;
    this.destination = destination;
    this.length = length;
  }

  public Vertex getSource() {
    return source;
  }

  public Vertex getDestination() {
    return destination;
  }

  public int getLength() {
    return length;
  }

  @Override
  public String toString() {
    return "Edge [source=" + source + ", destination=" + destination + ", length=" + length + "]";
  }
}

public class Part2 {

  public static long lowestCommonMultiple(long smallerValue, long largerValue) {
    long product = smallerValue * largerValue;

    long gcd = calculateGCD(smallerValue, largerValue);

    long lcm = (product / gcd);

    return lcm;
}

public static long calculateGCD(long largerValue, long smallerValue) {
    while (smallerValue != 0) {
        long temp = smallerValue;
        smallerValue = largerValue % smallerValue;
        largerValue = temp;
    }
    return largerValue;
}
  public static Edge findCycleGivenDestination(final Map<String, List<String>> directionMap,
      final Vertex destination, final String stepSequence) {

    String destLocation = destination.getLocation();
    int stepIndex = destination.getStepIndex();
    int distance = 0;

    Vertex travelling = new Vertex(destLocation, stepIndex);
    String travellingLocation = destLocation;
    int stepsSequenceLength = stepSequence.length();

    while (!travelling.equals(destination) || distance == 0) {
      char direction = stepSequence.charAt(stepIndex);
      List<String> currentLocationSides = directionMap.get(travellingLocation);
      if (direction == 'L') {
        travellingLocation = currentLocationSides.get(0);
      } else {
        travellingLocation = currentLocationSides.get(1);
      }

      distance++;
      stepIndex = (stepIndex + 1) % stepsSequenceLength;
      travelling = new Vertex(travellingLocation, stepIndex);
    }

    Edge selfEdge = new Edge(destination, destination, distance);
    return selfEdge;
  }

  public static String getDetailsOfNewDestNodeAndStepCount(final Map<String, List<String>> directionMap,
      final String startingNode, final String stepSequence) {

    String currentLocation = startingNode;
    int stepsSequenceLength = stepSequence.length();
    int stepCount = 0;

    while (currentLocation.charAt(2) != 'Z') {
      char direction = stepSequence.charAt(stepCount % stepsSequenceLength);
      List<String> currentLocationSides = directionMap.get(currentLocation);
      if (direction == 'L') {
        currentLocation = currentLocationSides.get(0);
      } else {
        currentLocation = currentLocationSides.get(1);
      }
      stepCount++;
    }

    StringBuilder result = new StringBuilder(currentLocation);
    result.append(" " + stepCount);
    return result.toString();
  }

  // function to read the file and get the number of steps needed
  public static long getTotalSteps(final String fileNameAndPath) {

    final Set<String> startingNodes = new HashSet<>();

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
        String startingLocation = locationTokens[0];

        directionMap.put(startingLocation, List.of(locationTokens[1], locationTokens[2]));

        if (startingLocation.charAt(2) == 'A') {
          startingNodes.add(startingLocation);
        }
      }

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    Set<Edge> mapEdges = new HashSet<>();

    // insert the straightforward edge for the starting destination to ending
    // destination
    for (String startingLocation : startingNodes) {
      Vertex starting = new Vertex(startingLocation, 0);

      // gather details on how to reach the ending
      String details = getDetailsOfNewDestNodeAndStepCount(directionMap, startingLocation, steps);
      String[] tokens = details.split("[\\s]+");
      String dest = tokens[0];
      int distance = Integer.parseInt(tokens[1]);
      int destIndex = distance % stepsSequenceLength;

      // add details to the edgeset
      Vertex ending = new Vertex(dest, destIndex);
      Edge startToDestEdge = new Edge(starting, ending, distance);
      mapEdges.add(startToDestEdge);
    }


    Comparator<Edge> edgeComparator = Comparator.comparing(Edge::getLength);

    PriorityQueue<Edge> edgePQ = new PriorityQueue<>(edgeComparator);
    edgePQ.addAll(mapEdges);

    // studying all the edges, the self loop is the same length as the src to dest loop
    for (Edge destEdge : mapEdges) {
      Vertex dest = destEdge.getDestination();
      Edge selfEdge = findCycleGivenDestination(directionMap, dest, steps);
      // System.out.println(destEdge);
      // System.out.println(selfEdge);
    }

    long lowestCommon = edgePQ.poll().getLength();
    while (!edgePQ.isEmpty()) {
      int nextSmallest = edgePQ.poll().getLength();
      lowestCommon = lowestCommonMultiple(lowestCommon, nextSmallest);
    }

    return lowestCommon;
  }

  public static void main(String[] args) {

    // read file
    // String fileNameAndPath = "./test_2.txt";
    String fileNameAndPath = "./input.txt";

    long totalSteps = getTotalSteps(fileNameAndPath);

    System.out.println(totalSteps);
  }
}
