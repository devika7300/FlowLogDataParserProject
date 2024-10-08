package com.flowlogdata.parser;

import java.io.*;
import java.util.*;

public class FlowLogDataParser {

    private Map<String, String> lookupTable = new HashMap<>();
    private Map<String, Integer> tagCounts = new HashMap<>();
    private Map<String, Integer> portProtocolCounts = new HashMap<>();

    // Read the lookup table CSV and populate the HashMap
    public void loadLookupTable(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("dstport")) continue; // Skip the header line
            
            String[] columns = line.split(",");
            if (columns.length == 3) {
                String dstPort = columns[0].trim();
                String protocol = columns[1].trim().toLowerCase(); // Handle case insensitivity
                String tag = columns[2].trim();
                
                // Create a key based on port and protocol for lookup
                String key = dstPort + "-" + protocol;
                lookupTable.put(key, tag);
            }
        }
        reader.close();
    }

    // Process flowlog txt
    public void processFlowLog(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        
        while ((line = reader.readLine()) != null) {
            
            String[] columns = line.split("\\s+");
            if (columns.length >= 11) {
                String dstPort = columns[5]; // Get destination port (column 6)
                String protocolNumber = columns[7]; // Get protocol number (column 8)

                // Map protocol number to protocol name (tcp or udp)
                String protocol;
                if (protocolNumber.equals("6")) {
                    protocol = "tcp"; // Protocol number 6 is TCP
                } else if (protocolNumber.equals("17")) {
                    protocol = "udp"; // Protocol number 17 is UDP
                } else {
                    protocol = "unknown"; // For any other protocol number
                }

                // Debug: Print the destination port and protocol
               // System.out.println("Destination port = " + dstPort + ", Protocol = " + protocol);

             // Create key to look up in the lookup table
                String key = dstPort.toLowerCase() + "-" + protocol.toLowerCase(); 
                
                // Find tag
                String tag = lookupTable.getOrDefault(key, "Untagged");
                
                // Update tag count
                tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                // Only update the port/protocol combination count if the port/protocol combination is found in the lookup table
                if (!tag.equals("Untagged")) {
                    String portProtocolKey = dstPort + "-" + protocol;
                    portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);
                }
            }
        }
        reader.close();
    }

    // Output the results to a output file
    public void outputResults(String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        
        // Output Tag Counts
        writer.write("Count of matches for each tag, sample o/p shown below \n");
        writer.write("Tag Counts: \n");
        writer.write("\nTag,Count\n");
        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue() + "\n");
        }
        
        //Output Port/Protocol Combination Counts
        writer.write("\nCount of matches for each port/protocol combination \n");
        writer.write("Port/Protocol Combination Counts:  \n");
        writer.write("\nPort,Protocol,Count\n");
        for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
            String[] splitKey = entry.getKey().split("-");
            writer.write(splitKey[0] + "," + splitKey[1] + "," + entry.getValue() + "\n");
        }
        
        writer.close();
    }

    public static void main(String[] args) {
        try {
        	FlowLogDataParser parser = new FlowLogDataParser();
            
            // Load lookup table from lookup.csv
            parser.loadLookupTable("resources/lookup.csv");
            
            // Process flow log from flowlog.txt
            parser.processFlowLog("resources/flowlog.txt");
            
            // Output the results to output.csv
            parser.outputResults("output/output.csv");
            
            System.out.println("Processing complete. Check output.csv for results.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
