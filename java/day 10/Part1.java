import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Part1 {

  public static char getPipeGivenNextDir(final List<String> map, final char dir, final Integer[] coord) {
    if (dir == 'T') {
      return map.get(coord[0] - 1).charAt(coord[1]);
    }
    if (dir == 'B') {
      return map.get(coord[0] + 1).charAt(coord[1]);
    }
    if (dir == 'L') {
      return map.get(coord[0]).charAt(coord[1] - 1);
    }
    return map.get(coord[0]).charAt(coord[1] + 1);
  }

  public static boolean isNextDirValid(final char nextPipe, final char dir) {
    if (dir == 'T') {
      return nextPipe == '|' || nextPipe == '7' || nextPipe == 'F';
    }
    if (dir == 'B') {
      return nextPipe == '|' || nextPipe == 'J' || nextPipe == 'L';
    }
    if (dir == 'R') {
      return nextPipe == '-' || nextPipe == '7' || nextPipe == 'J';
    }
    return nextPipe == '-' || nextPipe == 'F' || nextPipe == 'L';
  }

  public static char findNextDirection(final char prevDir, final char currentPipe) {

    if (prevDir == 'T') {
      if (currentPipe == '|')
        return 'T';
      else if (currentPipe == '7')
        return 'L';
      return 'R';
    } else if (prevDir == 'B') {
      if (currentPipe == '|')
        return 'B';
      else if (currentPipe == 'J')
        return 'L';
      return 'R';
    } else if (prevDir == 'R') {
      if (currentPipe == '-')
        return 'R';
      else if (currentPipe == '7')
        return 'B';
      return 'T';
    } else if (prevDir == 'L') {
      if (currentPipe == '-')
        return 'L';
      else if (currentPipe == 'F')
        return 'B';
      return 'T';
    }

    return 'N';
  }

  public static Integer[] getNextCoordFromPrevDir(final char prevDir, final Integer[] currentCoord) {
    Integer[] nextCoord = new Integer[2];
    nextCoord[0] = currentCoord[0];
    nextCoord[1] = currentCoord[1];

    if (prevDir == 'T') {
      nextCoord[0] -= 1;
      return nextCoord;
    }
    if (prevDir == 'B') {
      nextCoord[0] += 1;
      return nextCoord;
    }
    if (prevDir == 'L') {
      nextCoord[1] -= 1;
      return nextCoord;
    }
      nextCoord[1] += 1;
    return nextCoord;

  }

  public static Integer[] getBeginingPathCoord(final List<String> map, final char dir, final Integer[] currentCoord) {

    char nextPipe = getPipeGivenNextDir(map, dir, currentCoord);
    if (isNextDirValid(nextPipe, dir)) {
      return getNextCoordFromPrevDir(dir, currentCoord);
    }

    return null;
  }

  public static boolean sameAs(final Integer[] firstCoord, final Integer[] secondCoord) {
    return firstCoord[0].equals(secondCoord[0]) && firstCoord[1].equals(secondCoord[1]);
  } 
  // function to read the file and find the furthestStep
  public static long getFurthestStep(final String fileNameAndPath) {

    long furthestStep = 0;
    List<String> map = new ArrayList<>();
    // int x_axis = 0;
    int y_axis = 0;
    Integer[] s_coord = new Integer[2];

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all enters
      sc.useDelimiter("\r\n|\n");

      while (sc.hasNext()) {

        // note the record
        String line = sc.next();
        int searchIndex = line.indexOf('S');

        if (searchIndex != -1) {
          s_coord[0] = y_axis;
          s_coord[1] = searchIndex;
        }

        map.add(line);
        y_axis++;
      }

      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    Integer[] firstCoord = new Integer[2];
    char firstDir = 'N';
    
    String directions = "TRBL";

    int directionsIndex = 0;
    while (firstDir == 'N') {
      char testDir = directions.charAt(directionsIndex);
      Integer[] testCoord = getBeginingPathCoord(map, testDir, s_coord);

      if (testCoord != null) {
        firstCoord = testCoord;
        firstDir = testDir;
      }

      directionsIndex++;
    }
    
    while (!sameAs(firstCoord, s_coord)) {
      
      firstCoord = getNextCoordFromPrevDir(firstDir, firstCoord);
      char firstPipe = map.get(firstCoord[0]).charAt(firstCoord[1]);
      firstDir = findNextDirection(firstDir, firstPipe);
      
      furthestStep++;
    }

    return (furthestStep + 1) / 2;
  }

  public static void main(String[] args) {

    // read file
    // String fileNameAndPath = "./test.txt";
    String fileNameAndPath = "./input.txt";

    long numFurthestStep = getFurthestStep(fileNameAndPath);

    System.out.println(numFurthestStep);
  }
}
