import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Part1 {

  public static Long getMapping(final Long seedNum, final Map<Long, Long> map) {
    // find the soil
    Long mappedLocation = Long.valueOf(-1);

    // get entry set
    for (Map.Entry<Long, Long> currentEntry : map.entrySet()) {
      Long currentKey = currentEntry.getKey();
      if (currentKey <= seedNum) {
        Long currentValue = currentEntry.getValue();

        // determine if should record the value
        if (currentValue == Long.valueOf(-1)) {
          mappedLocation = seedNum;

          // determine the difference from the currentEntry
        } else {
          Long difference = seedNum - currentKey;
          mappedLocation = currentValue + difference;
        }
      } else {
        break;
      }
    }

    if (mappedLocation == Long.valueOf(-1)) {
      mappedLocation = seedNum;
    }

    return mappedLocation;
  }

  public static Long getLocationOfSeed(final Long seedNum,
      final Map<Long, Long> seedToSoilMap,
      final Map<Long, Long> soilToFertilizerMap,
      final Map<Long, Long> fertilizerToWaterMap,
      final Map<Long, Long> waterToLightMap,
      final Map<Long, Long> lightToTemperatureMap,
      final Map<Long, Long> temperatureToHumidityMap,
      final Map<Long, Long> humidityToLocationMap) {

    Long soilLocation = getMapping(seedNum, seedToSoilMap);
    Long fertilizerLocation = getMapping(soilLocation, soilToFertilizerMap);
    Long waterLocation = getMapping(fertilizerLocation, fertilizerToWaterMap);
    Long lightLocation = getMapping(waterLocation, waterToLightMap);
    Long temperatureLocation = getMapping(lightLocation, lightToTemperatureMap);
    Long humidityLocation = getMapping(temperatureLocation, temperatureToHumidityMap);
    Long finalLocation = getMapping(humidityLocation, humidityToLocationMap);

    return finalLocation;
  }

  public static Map<Long, Long> convertToMap(final Scanner sc) {
    Map<Long, Long> returningMap = new TreeMap<>();

    sc.next(); // remove the map details
    sc.next(); // remove the enter line
    String details = sc.next();
    while (!details.equals("")) {
      // split the details
      String[] detailsToken = details.split("[\\s]+");

      // determine key values to record to denote ranges
      Long beginningDestinationRange = Long.parseLong(detailsToken[0]);
      Long beginningSourceRange = Long.parseLong(detailsToken[1]);
      Long endingSourceRange = beginningSourceRange + Long.parseLong(detailsToken[2]);

      // determine if a destination was put for this beginning source range before
      // then input into the map
      if (returningMap.getOrDefault(beginningSourceRange, Long.valueOf(-1)) == Long.valueOf(-1)) {
        returningMap.put(beginningSourceRange, beginningDestinationRange);
      }

      // determine if the next range already has an input in the map
      // otherwise denote with -1 that from this number inclusive onwards,
      // the mapping is direct
      if (!returningMap.containsKey(endingSourceRange)) {
        returningMap.put(endingSourceRange, Long.valueOf(-1));
      }

      // reset details to the next line
      if (sc.hasNext()) {
        details = sc.next();
      } else {
        details = "";
      }
    }

    return returningMap;
  }

  public static Long getLowestLocationNumber(final String fileNameAndPath) {

    Long lowestLocationNumber = Long.MAX_VALUE;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all colons and enters
      sc.useDelimiter(":|\r\n|\n");
      // extract the seeds first
      sc.next();
      String allSeeds = sc.next();
      Long[] seeds = Arrays.stream(allSeeds.split("[\\s]+"))
          .filter(s -> !s.isEmpty())
          .map(Long::parseLong)
          .toArray(Long[]::new);
      sc.next(); // remove the empty line

      // extract the seed to soil map
      Map<Long, Long> seedToSoilMap = convertToMap(sc);

      // extract the soil to fertilizer map
      Map<Long, Long> soilToFertilizerMap = convertToMap(sc);

      // extract the fertilizer to water map
      Map<Long, Long> fertilizerToWaterMap = convertToMap(sc);

      // extract the water to light map
      Map<Long, Long> waterToLightMap = convertToMap(sc);

      // extract the light to temperature map
      Map<Long, Long> lightToTemperatureMap = convertToMap(sc);

      // extract temperature to humidity map
      Map<Long, Long> temperatureToHumidityMap = convertToMap(sc);

      // extract humidity to location map
      Map<Long, Long> humidityToLocationMap = convertToMap(sc);

      // after extracting all the maps, determine the location of each seed
      for (Long seed : seeds) {
        Long locationNumber = getLocationOfSeed(seed,
            seedToSoilMap,
            soilToFertilizerMap,
            fertilizerToWaterMap,
            waterToLightMap,
            lightToTemperatureMap,
            temperatureToHumidityMap,
            humidityToLocationMap);

        lowestLocationNumber = Long.min(locationNumber, lowestLocationNumber);
      }
      // for catching exception
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    }

    return lowestLocationNumber;
  }

  public static void main(String[] args) {

    // read file
    String fileNameAndPath = "./input.txt";

    Long lowestLocationNumber = getLowestLocationNumber(fileNameAndPath);

    System.out.println(lowestLocationNumber);
  }
}
