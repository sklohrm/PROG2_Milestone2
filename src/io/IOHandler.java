package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOHandler {

    public static List<String> readFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader("../../production/PROG1_Milestone1/" + fileName))) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
