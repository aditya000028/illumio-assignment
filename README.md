# Illumio Technical Assessment
This is the take-home assigment for Illumio as part of the interview process. The program is written in Java by Aditya Gupta

### Assumptions
* This program only supports the default log format, not custom and the only version that is supported is 2
* The log format should be the same as the ones provided. For example: ```2 123456789012 eni-6m7n8o9p 10.0.2.200 198.51.100.4 143 49158 6 18 14000 1620140761 1620140821 ACCEPT OK```
* Each element of a single log line should be separated by a single space
* Log lines should only be separated by a single new line
* The `protocol` given in the lookup table file should be in decimal format, not the name format
* A single log's 7th and 8th elements should be the `dstPort` and `protocol` respectively 
* The lookup table file should be in a `csv` format, where the first line in the file are the column headers
* Each element in a row of the lookup table file should be separated by a single comma
* The program requires the path to the name of the log file and the path to the lookup table filename to be arguments
* If the program encounters an error which violate any of the assumptions, it will terminate without generating any file

### Remarks
1. One of the assumptions is that the protocol number is given in a decimal format, but we can avoid this. We can manually create a map which has the decimal number of a protocol as the key to the map, and the name of the protocol as the value. We can then convert the protocol number from the logs files to the protocol name to be written to the generated files. If we were to go through with this, we must make sure that all possible protocols are stored in this conversion map. 

2. I have created 3 differnt log files which all vary in size. The program executes successfully and efficiently with all 3 test files. 

3. Here are a few benchmarks. The execution time is averaged over 5 runs. Also note that these benchmarks were achieved with a constant lookup table file size of 1 KB.

    | logs file sizes (KB)    | Execution time (ms) |
    | ----------------------- | ------------------- |
    | 2                       | 20                  |
    | 5589                    | 241                 |
    | 10286                   | 327                 |


### How to run the program
This program is written in Java, so it must be installed on your system. To install it if you do not already have it, please refer to this [link](https://www.java.com/en/download/help/download_options.html)

Once you have Java installed and have navigated to the main directory, you can run it like so (note that this command may vary if you are on an OS other than Windows):
```
java Main.java [PATH_TO_TXT_LOG_FILE] [PATH_TO_LOOKUP_TABLE_FILE]
```

For example, if my `logs-1.txt` and `lookupTable.csv` files reside in the same folder as my `Main.java`, I could run the program like this:
```
java Main.java logs-1.txt lookupTable.csv
```

The program will then generate 2 files, `portProtocolCombinationCount.csv` and `tagCount.csv`, both of which follow the instructions given in the requirements. 