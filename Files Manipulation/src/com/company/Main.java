package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileName = "";
        String command = "";
        try (FileReader fileReader = new FileReader("README.txt");
             BufferedReader reader = new BufferedReader(fileReader)) {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!line.isEmpty())  {
                    if (line.charAt(0) != '#') {
                        String[] lineArray = line.split(" ");
                        command = lineArray[0];
                        fileName = lineArray[1];
                        switch (command) {
                            case "CREATE":
                                createFile(fileName);
                                break;
                            case "DELETE":
                                deleteFile(fileName);
                                break;
                            case "APPEND":
                                StringBuilder strToAppend = new StringBuilder();
                                for (int i = 2; i < lineArray.length; i++) {
                                    strToAppend.append(lineArray[i] + " ");
                                }
                                appendToFile(fileName, strToAppend.toString());
                                break;
                            case "COPY":
                                copyToDestinationFolder(fileName, lineArray[2]);
                            default:
                                break;
                        }

                    }
                }
            }
        } catch (IOException ex ) {
            ex.printStackTrace();
        }
    }
    public static void createFile(String fileName) {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.printf("Success: \"%s\" file created\n", fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        System.out.printf(file.delete() ? "Success: \"%s\" file deleted\n" : "Fail: \"%s\" does not exist\n", fileName);

    }
    public static void appendToFile(String fileName, String input) {
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            writer.println(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void createDestinationFolder (String destinationPath) {
        String path = ".";
        File file;
        String[] directories = destinationPath.split("/");
        for (int i = 0; i < directories.length - 1; i++) {
            path = path + "/" + directories[i];
            file = new File(path);
            if (file.mkdir()) {
                System.out.printf("Success: \"%s\" directory created\n", directories[i]);
            }
        }
    }
    public static void copyToDestinationFolder(String sourceFileName, String destinationPath) {
        createDestinationFolder(destinationPath);
        createFile(destinationPath);

        try (FileReader fileReader = new FileReader(sourceFileName);
             BufferedReader reader = new BufferedReader(fileReader)) {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                appendToFile(destinationPath, line);
            }
         } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
