package com.coltonrobotics.dogeloader;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    public static class LibEntry implements Serializable {
        public String name, author, desc, version, url, title = "Placeholder";
    }
    public static List<LibEntry> parse(Activity activity){
        List<LibEntry> results = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(activity.getAssets().open("libs.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String sections[] =  mLine.split(",");
                LibEntry newEntry = new LibEntry();
                newEntry.name = sections[0];
                newEntry.author = sections[1];
                newEntry.desc = sections[2];
                newEntry.version = sections[3];
                newEntry.url = sections[4];
                newEntry.title = sections[5];

                results.add(newEntry);

            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return results;
    }
}
