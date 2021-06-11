package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        String pathToFile = args[0];
        String fileString = "";
        try {
            fileString = readFileAsString(pathToFile);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
        System.out.printf("The text is:\n%s\n",fileString);
        int[] countArray = countAll(fileString);
        int sentences = countArray[0];
        int words = countArray[1];
        int characters = countArray[2];
        int syllables = countArray[3];
        int polysyllables =countArray[4];

        System.out.printf("Words: %d\nSentences: %d\nCharacters: %d\nSyllables: %d\nPolysyllables: %d\n" +
                "Enter the score you want to calculate (ARI, FK, SMOG, CL, all):",words,sentences,characters,syllables,polysyllables);
        input = scanner.nextLine();
        Output(input, countArray);

    }
    public static void Output(String input, int[] arr) {
        double score;
        String age;
        switch (input) {
            case "ARI":
                score = ARI(arr);
                age = ageRange((int)(Math.ceil(score)));
                System.out.printf("\nAutomated Readability Index: %.2f (about %s-year-olds).",score, age);
                System.out.printf("\nThis text should be understood in average by %s-year-olds.",age);
                break;
            case "FK":
                score = FKI(arr);
                age = ageRange((int)(Math.ceil(score)));
                System.out.printf("\nFlesch–Kincaid readability tests: %.2f (about %s-year-olds).",score, age);
                System.out.printf("\nThis text should be understood in average by %s-year-olds.",age);
                break;
            case "SMOG":
                score = SMOGI(arr);
                age = ageRange((int)(Math.ceil(score)));
                System.out.printf("\nSimple Measure of Gobbledygook: %.2f (about %s-year-olds).",score, age);
                System.out.printf("\nThis text should be understood in average by %s-year-olds.",age);
                break;
            case "CL":
                score = CLI(arr);
                age = ageRange((int)(Math.ceil(score)));
                System.out.printf("\nColeman–Liau index: %.2f (about %s-year-olds).", score, age);
                System.out.printf("\nThis text should be understood in average by %s-year-olds.",age);
                break;
            case "all":
                double total = 0;
                double avg;
                score = ARI(arr);
                age = ageRange((int)(Math.ceil(score)));
                total += score;
                System.out.printf("\nAutomated Readability Index: %.2f (about %s-year-olds).",score, age);
                score = FKI(arr);
                age = ageRange((int)(Math.ceil(score)));
                total += score;
                System.out.printf("\nFlesch–Kincaid readability tests: %.2f (about %s-year-olds).",score, age);
                score = SMOGI(arr);
                age = ageRange((int)(Math.ceil(score)));
                total += score;
                System.out.printf("\nSimple Measure of Gobbledygook: %.2f (about %s-year-olds).",score, age);
                score = CLI(arr);
                age = ageRange((int)(Math.ceil(score)));
                total += score;
                System.out.printf("\nColeman–Liau index: %.2f (about %s-year-olds).", score, age);
                avg = total / 4.0;
                System.out.printf("\nThis text should be understood in average by %.2f-year-olds.",avg);
                break;
        }
    }
    public static double ARI(int[] arr) {
        return (4.71 * (double)(arr[2]) / (double)(arr[1]) ) + (0.5 * (double)(arr[1]) / (double)(arr[0]) ) - 21.43;
    }
    public static double FKI(int[] arr) {
        return (0.39 * (double)(arr[1]) / (double)(arr[0]) ) + (11.8 * (double)(arr[3]) / (double)(arr[1]) ) - 15.59;
    }
    public static double SMOGI(int[] arr) {
        return (1.043 * Math.sqrt((double)(arr[4]) * 30 / (double)(arr[0])) ) + 3.1291;
    }
    public static double CLI(int[] arr) {
        return (0.0588 * (double)(arr[2]) / (double)(arr[1]) * 100 ) - (0.296 * (double)(arr[0]) / (double)(arr[1]) * 100) - 15.8;
    }
    public static String ageRange(int score) {
        String range;
        switch (score) {
            case 1:
                range = "6";
                break;
            case 2:
                range = "7";
                break;
            case 3:
                range = "9";
                break;
            case 4:
                range = "10";
                break;
            case 5:
                range = "11";
                break;
            case 6:
                range = "12";
                break;
            case 7:
                range = "13";
                break;
            case 8:
                range = "14";
                break;
            case 9:
                range = "15";
                break;
            case 10:
                range = "16";
                break;
            case 11:
                range = "17";
                break;
            case 12:
                range = "18";
                break;
            case 13:
                range = "24";
                break;
            case 14:
                range = "24+";
                break;
            default:
                range = "";
                break;
        }
        return range;
    }
    public static int[] countAll(String s) {
        int[] countArray = new int[5]; // sentences, words, characters, syllables, polysyllables
        String[] sentences = s.split("[.!?]\\s");
        int wordCount = 0;
        int syllableCount = 0;
        int polysyllableCount = 0;
        int temp = 0;
        for (int i = 0; i < sentences.length; i++) {
            String[] words = sentences[i].split("\\s");
            wordCount += words.length;
            for (int j = 0; j < words.length; j++) {
                temp = countSyllable(words[j]);
                syllableCount += temp;
                if (temp > 2) {
                    polysyllableCount += 1;
                }
            }
        }
        countArray[0] = sentences.length;
        countArray[1] = wordCount;
        countArray[2] = s.length() - wordCount;
        countArray[3] = syllableCount;
        countArray[4] = polysyllableCount;
        return countArray;
    }
    private static int countSyllable(String word) {
        String i = "(?i)[aiou][aeiou]*|e[aeiou]*(?!\\b)";
        Matcher m = Pattern.compile(i).matcher(word);
        int count = 0;
        while (m.find()) {
            count++;
        }
        // return at least 1
        return Math.max(count, 1);
    }
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
