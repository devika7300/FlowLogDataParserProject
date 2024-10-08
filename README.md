# Flow Log Data Parser

## Project Overview
This project parses a flow log file and maps each row to a tag based on a lookup table (lookup.csv) that specifies destination port, protocol, and tag combinations. The program produces an output file (output.csv) that contains two main sections:
* Tag Counts: The number of occurrences for each tag.
* Port/Protocol Combination Counts: The number of occurrences for each port/protocol combination that has a valid mapping in the lookup table.

## Assumptions
- The program only supports default flow log formats (as provided in the sample flowlog.txt).
- The only supported flow log version is 2 (as per the provided examples).
- The program is designed to handle ASCII plain text input files.
- Both the flow log file and the lookup table are plain text files (up to 10 MB and 10,000 lines, respectively).
- Tags can map to multiple destination port and protocol combinations.
- Case-insensitive matching is ensured for both destination ports and protocols.
- The flow log entries that don't match any combination in the lookup table are marked as Untagged.
- No custom flow log formats are supported (only the provided format is supported).

## File Descriptions
- FlowLogDataParser.java: The Java code to parse and map flow logs.
- resources/flowlog.txt: Input flow log file.
- resources/lookup.csv: Input lookup table file.
- output/output.csv: Generated output file after running the program.

## How to Compile and Run

### Prerequisites
Java Development Kit (JDK) installed (version 8 or higher).
A text editor or IDE (like Eclipse or IntelliJ).

### Compilation and Execution
- Clone or Download the Repository:

- Make sure the following files are present in the project:
FlowLogDataParser.java (main program)
flowlog.txt (input flow log)
lookup.csv (lookup table)

- Compile the Java Program:
Open a terminal or command prompt in the directory where FlowLogDataParser.java is located, and compile the program using the following command:
javac FlowLogParser.java

- Run the Program:
Once the program is compiled, run it using the following command:
java FlowLogParser

After successful execution, the program will generate an output.csv file in the output directory, which contains the tag counts and port/protocol combination counts.

## Tests Performed
Test 1: Case-Insensitive Matching
The program was tested with various case combinations for the protocol in the lookup table (e.g., TCP, tcp, UDP, udp). In all cases, the program correctly handled case-insensitive matching.

Test 2: Large File Handling
The program was tested with a large flow log file (close to 10 MB), containing multiple destination port and protocol combinations. The program processed the file efficiently without running out of memory or causing delays.

Test 3: No Matching Entries (Untagged)
The program was tested with flow log entries that didn't have matching destination ports and protocols in the lookup table. These entries were correctly counted as Untagged.

Test 4: Multiple Tags for the Same Destination Port
The lookup table was tested with multiple destination ports and protocols mapping to the same tag. The program correctly handled this and counted the occurrences of each tag appropriately.

Test 5: Missing Entries in Lookup Table
The program was tested with flow logs where some ports were not present in the lookup table. These entries were correctly classified as Untagged.

## Key Features
- Memory Efficiency: The program processes the flow log file line-by-line using BufferedReader, which allows it to handle large files (up to 10 MB) efficiently without consuming excessive memory.
- HashMap for Fast Lookup: The lookup table is loaded into a HashMap, providing constant-time lookup for destination port/protocol combinations.
- Case-Insensitive Matching: The program ensures that destination port and protocol lookups are case-insensitive, preventing issues due to mismatched casing.

## Limitations
- The program does not support custom flow log formats. It only processes logs that follow the provided format.
- Only version 2 of the flow log is supported.
- The program assumes that both input files (flowlog.txt and lookup.csv) are properly formatted and contain valid data.
