package com.spotify.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static List<String> getTextAsList(String filename){
        String path = String.format("src/test/resources/%s.csv",filename);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        }catch (IOException e){
            System.out.println("File not found");
        }
        return lines;
    }
}
