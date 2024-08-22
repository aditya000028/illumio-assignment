import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

  private static String FILE_NAMES_NOT_PROVIDED = "Expected log file name and lookup table file name respectively. Example: java Main logs.txt lookupTable.csv";
  private static String INVALID_TABLE_FORMAT = "Invalid lookup table format.";
  private static String INVALID_LOG_FILE_FORMAT = "Invalid log file format.";

  private static String UNTAGGED = "untagged";

  private static String TAG_COUNT_FILENAME = "tagCount.csv";
  private static String TAG_COUNT_FILENAME_HEADER = "Tag,Count\n";

  private static String PORT_PROTOCOL_COMBINATION_COUNT_FILENAME = "portProtocolCombinationCount.csv";
  private static String PORT_PROTOCOL_COMBINATION_COUNT_FILE_HEADER = "Port,Protocol,Count\n";

  private static String FINISHED_PARSING_LOGS = "Finished parsing log file.";

  public static void main(String[] args) {
    final long startTime = System.currentTimeMillis();
    if (args.length != 2) {
      System.out.println("Error: " + FILE_NAMES_NOT_PROVIDED);
      return;
    }

    String logFileName = args[0];
    String lookupTableFileName = args[1];
    HashMap<String, String> lookupTable = null;

    try {
      lookupTable = createLookupTable(lookupTableFileName);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }

    HashMap<String, Integer> tagMatchesCount = new HashMap<String, Integer>();
    HashMap<String, Integer> portProtocolCombinationCount = new HashMap<String, Integer>();

    try {
      parseLogFile(logFileName, lookupTable, tagMatchesCount, portProtocolCombinationCount);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }

    try {
      generateFile(TAG_COUNT_FILENAME, TAG_COUNT_FILENAME_HEADER, tagMatchesCount);
      generateFile(PORT_PROTOCOL_COMBINATION_COUNT_FILENAME, PORT_PROTOCOL_COMBINATION_COUNT_FILE_HEADER,
          portProtocolCombinationCount);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    final long endTime = System.currentTimeMillis();
    System.out.println("Total execution time: " + (endTime - startTime));
    System.out.println(FINISHED_PARSING_LOGS);
  }

  /**
   * Generates the file with the given filename and header and inserts the given data into the file.
   *
   * @param filename   name of the file to generate
   * @param header     header of the file to generate
   * @param data       data to insert into the file
   * @throws Exception if the file cannot be generated
  */
  private static void generateFile(String filename, String filenameHeader, HashMap<String, Integer> data)
      throws Exception {

    FileWriter fileWriter = new FileWriter(filename);
    fileWriter.write(filenameHeader);

    for (String key : data.keySet()) {
      fileWriter.write(key + "," + data.get(key) + "\n");
    }

    fileWriter.close();
  }

  /**
   * Parses the log file and populates the given hashmaps with the data 
   * 
   * @param logFileName                     name of the log file to parse
   * @param lookupTable                     lookup table to use for parsing
   * @param tagMatchesCount                 hashmap to populate with tag matches
   * @param portProtocolCombinationCount    hashmap to populate with port protocol combination matches
   * @throws Exception                      if the log file cannot be parsed
  */
  private static void parseLogFile(String logFileName, HashMap<String, String> lookupTable,
      HashMap<String, Integer> tagMatchesCount, HashMap<String, Integer> portProtocolCombinationCount)
      throws Exception {
    File logFile = new File(logFileName);
    Scanner fileScanner = new Scanner(logFile);

    while (fileScanner.hasNextLine()) {
      String[] logParts = fileScanner.nextLine().toLowerCase().split(" ");
      if (logParts.length < 8) {
        fileScanner.close();
        throw new Exception(INVALID_LOG_FILE_FORMAT);
      }

      String dstPort = logParts[6];
      String protocol = logParts[7];

      String lookupTableKey = String.join(",", dstPort, protocol);

      portProtocolCombinationCount.put(lookupTableKey,
          portProtocolCombinationCount.getOrDefault(lookupTableKey, 0) + 1);

      String tag = lookupTable.getOrDefault(lookupTableKey, UNTAGGED);
      tagMatchesCount.put(tag, tagMatchesCount.getOrDefault(tag, 0) + 1);

    }

    fileScanner.close();
  }

  /**
  * Creates a lookup table from the given file.
  *
  * @param lookupTableFileName name of the lookup table file
  * @return                    lookup table
  * @throws Exception          if the lookup table file cannot be parsed or if the table format is invalid
  */
  private static HashMap<String, String> createLookupTable(String lookupTableFileName) throws Exception {
    File lookupTableFile = new File(lookupTableFileName);
    Scanner fileScanner = new Scanner(lookupTableFile);

    HashMap<String, String> lookupTable = new HashMap<String, String>();

    // Read the column names line of the lookup table file
    if (fileScanner.hasNextLine())
      fileScanner.nextLine();

    while (fileScanner.hasNextLine()) {
      String[] rowParts = fileScanner.nextLine().toLowerCase().split(",");
      if (rowParts.length != 3) {
        fileScanner.close();
        throw new Exception(INVALID_TABLE_FORMAT);
      }

      String dstPort = rowParts[0];
      String protocol = rowParts[1];
      String tag = rowParts[2];

      lookupTable.put(dstPort + "," + protocol, tag);
    }

    fileScanner.close();
    return lookupTable;
  }
}