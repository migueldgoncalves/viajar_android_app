package com.viajar.viajar;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public CsvParser() {
    }

    static List<String> csvParser(Context context, int csvId) {
        InputStream csvStream = context.getResources().openRawResource(csvId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream));
        List<String> lineArray = new ArrayList<>();
        try {
            String csvLine = reader.readLine();
            while (csvLine != null) {
                lineArray.add(csvLine);
                csvLine = reader.readLine();
            }
            csvStream.close();
        }
        catch (IOException e) {
            Log.e("error", "Error while parsing CSV file: " + e);
        }
        return lineArray;
    }

    static List<String> lineParser(String csvLine) {
        String[] tempSplitLine = csvLine.split(",");

        List<String> splitLine = new ArrayList<>();
        for (int i=0; i<tempSplitLine.length; i++) {
            if (i < (tempSplitLine.length - 1)) {
                if (tempSplitLine[i].contains("\"") && tempSplitLine[i+1].contains("\"")) {
                    String quoted_text = tempSplitLine[i] + "," + tempSplitLine[i + 1];
                    quoted_text = quoted_text.substring(1, quoted_text.length() - 1); // Removes quotation marks
                    splitLine.add(quoted_text);
                    i++;
                } else {
                    splitLine.add(tempSplitLine[i]);
                }
            } else {
                splitLine.add(tempSplitLine[i]);
            }
        }

        return splitLine;
    }

}
