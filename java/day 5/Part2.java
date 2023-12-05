import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Part2 {

  public static Long extractMapping(final Long key, final Long mapping) {
    if (mapping == -1)
      return key;
    return mapping;
  }

  public static Map<Long, Long> mapDestinationToSource(final Map<Long, Long> destination,
      final Map<Long, Long> source) {

    Map<Long, Long> destinationRanges = new TreeMap<>();

    List<Long> destinationKeys = new ArrayList<>(destination.keySet());
    int destinationKeysIndex = 0;
    int destinationKeysSize = destinationKeys.size();
    Long destinationKey = destinationKeys.get(destinationKeysIndex);
    Long oldDestinationkey = Long.valueOf(0);
    Long oldDestinationKeyMapping = extractMapping(oldDestinationkey,
        destination.getOrDefault(oldDestinationkey, Long.valueOf(-1)));

    List<Long> sourceKeys = new ArrayList<>(source.keySet());
    int sourceKeysIndex = 0;
    int sourceKeysSize = sourceKeys.size();
    Long beginningOfSourceRange = sourceKeys.get(sourceKeysIndex);
    Long endOfSourceRange = source.get(beginningOfSourceRange);

    Long beginningOfMapping = null;
    boolean endOfMapping = true;

    while (sourceKeysIndex < sourceKeysSize) {

      // if the mapping of the previous source range is done
      // reset the source range values
      if (endOfMapping) {
        beginningOfSourceRange = sourceKeys.get(sourceKeysIndex);
        endOfSourceRange = source.get(beginningOfSourceRange);
        endOfMapping = false;
      }

      // if the current destinationKey is less than the beginning of the source range
      if (destinationKey < beginningOfSourceRange) {
        destinationKeysIndex++;

        if (destinationKeysIndex < destinationKeysSize) {
          oldDestinationkey = destinationKey;
          oldDestinationKeyMapping = extractMapping(oldDestinationkey,
              destination.getOrDefault(oldDestinationkey, Long.valueOf(-1)));
          destinationKey = destinationKeys.get(destinationKeysIndex);
        } else {
          break;
        }

        // otherwise check if this is more than the end of the source range
      } else if (destinationKey >= endOfSourceRange) {
        Long difference = beginningOfSourceRange - oldDestinationkey;
        beginningOfMapping = oldDestinationKeyMapping + difference;

        Long range = endOfSourceRange - beginningOfSourceRange;
        destinationRanges.put(beginningOfMapping, beginningOfMapping + range);
        endOfMapping = true;

        sourceKeysIndex++;

        // otherwise re set the other stuffs
      } else {
        Long difference = beginningOfSourceRange - oldDestinationkey;
        beginningOfMapping = oldDestinationKeyMapping + difference;

        Long range = destinationKey - beginningOfSourceRange;
        destinationRanges.put(beginningOfMapping, beginningOfMapping + range);
        beginningOfSourceRange = destinationKey;

        destinationKeysIndex++;

        if (destinationKeysIndex < destinationKeysSize) {
          oldDestinationkey = destinationKey;
          oldDestinationKeyMapping = extractMapping(oldDestinationkey,
              destination.getOrDefault(oldDestinationkey, Long.valueOf(-1)));
          destinationKey = destinationKeys.get(destinationKeysIndex);
        } else {
          break;
        }

      }
    }

    // map the remaining of the source ranges supposing that they are not
    // represented by the destination mapping
    while (sourceKeysIndex < sourceKeysSize) {

      beginningOfSourceRange = sourceKeys.get(sourceKeysIndex);
      endOfSourceRange = source.get(beginningOfSourceRange);

      Long range = endOfSourceRange - beginningOfSourceRange;
      destinationRanges.put(beginningOfSourceRange, beginningOfSourceRange + range);

      sourceKeysIndex++;
    }

    return destinationRanges;

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

  public static Map<Long, Long> getSeedMap(final String seeds) {
    Map<Long, Long> seedsMap = new TreeMap<>();

    String[] seedsToken = seeds.split("[\\s]+");
    for (int i = 1; i < seedsToken.length; i += 2) {
      Long beginningSequence = Long.parseLong(seedsToken[i]);
      Long endingSequence = Long.parseLong(seedsToken[i + 1]) + beginningSequence;
      seedsMap.put(beginningSequence, endingSequence);
    }

    return seedsMap;
  }

  public static Long getLowestLocationNumber(final String fileNameAndPath) {

    Long lowestLocationNumber = Long.MAX_VALUE;

    try (Scanner sc = new Scanner(new File(fileNameAndPath))) {

      // section off at all colons and enters
      sc.useDelimiter(":|\r\n|\n");

      // extract the seeds first
      sc.next();
      String allSeeds = sc.next();
      Map<Long, Long> seedsMap = getSeedMap(allSeeds);
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

      // after extracting all the maps, get the new mappings
      Map<Long, Long> soilMapping = mapDestinationToSource(seedToSoilMap, seedsMap);

      // after extracting all the maps, get the new mappings
      Map<Long, Long> fertilizerMapping = mapDestinationToSource(soilToFertilizerMap, soilMapping);

      // after extracting all the maps, get the new mappings
      Map<Long, Long> waterMapping = mapDestinationToSource(fertilizerToWaterMap, fertilizerMapping);

      // after extracting all the maps, get the new mappings
      Map<Long, Long> lightMapping = mapDestinationToSource(waterToLightMap, waterMapping);

      // after extracting all the maps, get the new mappings
      Map<Long, Long> temperatureMapping = mapDestinationToSource(lightToTemperatureMap, lightMapping);

      // after extracting all the maps, get the new mappings
      Map<Long, Long> humidityMapping = mapDestinationToSource(temperatureToHumidityMap, temperatureMapping);

      // after extracting all the maps, get the new mappings
      Map<Long, Long> locationMapping = mapDestinationToSource(humidityToLocationMap, humidityMapping);

      List<Long> locationKeys = new ArrayList<>(locationMapping.keySet());
      lowestLocationNumber = locationKeys.get(0);

    } catch (FileNotFoundException e) {
      System.out.println("file not found");

    }

    return lowestLocationNumber;
  }

  public static void main(String[] args) {

    // read file
    // String fileNameAndPath = "./test.txt";
    String fileNameAndPath = "./input.txt";

    Long lowestLocationNumber = getLowestLocationNumber(fileNameAndPath);

    System.out.println(lowestLocationNumber);
  }

}