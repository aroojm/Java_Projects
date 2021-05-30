package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static Map<String, String> cardMap;
    static List<String> log;
    static List<Integer> hardCards;
    static boolean export;
    static String fileName;
    public static void main(String[] args) {
        cardMap = new LinkedHashMap<>();
        log = new ArrayList<>();
        hardCards = new ArrayList<>();
        export = false;
        int len = args.length;
        if (len == 2) {
            if (args[0].equals("-import")) {
                importCardsOnLoad(args[1]);
            } else {
                export = true;
                fileName = args[1];
            }
        } else if (len == 4) {
            if (args[0].equals("-import")) {
                importCardsOnLoad(args[1]);
                export = true;
                fileName = args[3];
            } else {
                importCardsOnLoad(args[3]);
                export = true;
                fileName = args[1];
            }
        }
        Scanner scanner = new Scanner(System.in);
        menu(scanner);
    }
    public static void menu(Scanner scanner){
        String menuString = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n";
        System.out.print(menuString);
        log.add(menuString);
        String input = scanner.nextLine();
        log.add(input + "\n");
        while (!input.equals("exit")) {
            callFunction(input, scanner);
            System.out.print("\n" + menuString);
            log.add("\n" + menuString);
            input = scanner.nextLine();
            log.add(input + "\n");
        }
        if (input.equals("exit")) {
            callFunction("exit", scanner);
        }
    }
    public static void callFunction(String input, Scanner scanner) {
        switch (input) {
            case "add":
                addCard(scanner);
                break;
            case "remove":
                removeCard(scanner);
                break;
            case "import":
                importCards(scanner);
                break;
            case "export":
                exportCards(scanner);
                break;
            case "ask":
                quizCards(scanner);
                break;
            case "exit":
                if (export) {
                    exportCardsOnExit(fileName);
                }
                System.out.println("Bye bye!");
                break;
            case "log":
                saveLog(scanner);
                break;
            case "hardest card":
                checkHardestCard();
                break;
            case "reset stats":
                resetStats();
                break;
        }
    }
    public static void resetStats() {
        for (int i = 0; i < hardCards.size(); i++) {
            hardCards.set(i, 0);
        }
        String str = "Card statistics have been reset.\n";
        System.out.print(str);
        log.add(str);
    }
    public static void checkHardestCard() {
        int max = 0;
        int countMax = 0;
        boolean flag = false;
        for (int i = 0; i < hardCards.size(); i++) {
            if (hardCards.get(i) > max) {
                max = hardCards.get(i);
            }
        }
        if (max == 0) {
            String str1 = "There are no cards with errors.\n";
            System.out.print(str1);
            log.add(str1);
        } else {
            int index = 0;
            for (int i = 0; i < hardCards.size(); i++) {
                if (hardCards.get(i) == max) {
                    countMax++;
                    index = i;
                }
            }
            if (countMax == 1) {
                String term = "";
                int k = 0;
                for (var entry: cardMap.entrySet()) {
                    if (index == k) {
                        term = entry.getKey();
                        break;
                    }
                    k++;
                }
                String str2 = String.format("The hardest card is \"%s\". You have %d errors answering it.\n", term, max);
                System.out.printf(str2);
                log.add(str2);
            } else {
                StringBuilder terms = new StringBuilder();
                int l = 0;
                for (var entry: cardMap.entrySet()) {
                    if (hardCards.get(l) == max) {
                        terms.append(" \"" + entry.getKey() + "\",");
                    }
                    l++;
                }
                terms.deleteCharAt(terms.length() - 1);
                String strTerms = terms.toString();
                String str3 = String.format("The hardest cards are%s. You have %d errors answering them.\n", strTerms, max);
                System.out.printf(str3);
                log.add(str3);
            }
        }
    }
    public static void saveLog(Scanner s) {
        String str1, str2;
        str1 = "File name:\n";
        str2 = "The log has been saved.\n";
        System.out.print(str1);
        log.add(str1);
        String fileName = s.nextLine();
        log.add(fileName + "\n");
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) {
            log.add(str2);
            for (String str: log) {
                writer.printf("%s", str);
            }
            System.out.print(str2);
           // log.add(str2);
        } catch (IOException e) {
            String str3 = String.format("An exception occurs %s\n", e.getMessage());
            System.out.printf(str3);
            log.add(str3);
        }
    }
    public static void addCard(Scanner s) {
        String term;
        String def;
        String str1 = "The card:\n";
        System.out.print(str1);
        log.add(str1);
        term = s.nextLine();
        log.add(term + "\n");
        if (cardMap.containsKey(term)) {
            String str2 = String.format("The card \"%s\" already exists.\n", term);
            System.out.printf(str2);
            log.add(str2);
            return;
        }
        String str3 = "The definition of the card:\n";
        System.out.print(str3);
        log.add(str3);
        def = s.nextLine();
        log.add(def + "\n");
        if (cardMap.containsValue(def)) {
            String str4 = String.format("The definition \"%s\" already exists.\n", def);
            System.out.print(str4);
            log.add(str4);
            return;
        }
        cardMap.put(term, def);
        hardCards.add(0);
        String str5 = String.format("The pair (\"%s\":\"%s\") has been added.\n", term, def);
        System.out.print(str5);
        log.add(str5);
    }
    public static void removeCard(Scanner s) {
        String str1 = "Which card?\n";
        System.out.print(str1);
        log.add(str1);
        String term = s.nextLine();
        log.add(term + "\n");
        int i = 0;
        if (cardMap.containsKey(term)) {
            cardMap.entrySet().removeIf(entry -> entry.getKey().equals(term));
            for (var entry: cardMap.entrySet()) {
                if (entry.getKey().equals(term)) {
                    hardCards.remove(i);
                }
                i++;
            }
            String str2 = "The card has been removed.\n";
            System.out.print(str2);
            log.add(str2);
        } else {
            String str3 = String.format("Can't remove \"%s\": there is no such card.\n", term);
            System.out.printf(str3);
            log.add(str3);
        }
    }
    public static void importCards(Scanner s) {
        String str1 = "File name:\n";
        System.out.print(str1);
        log.add(str1);
        String fileName = s.nextLine();
        log.add(fileName + "\n");
        File file = new File(fileName);
        String def, term;
        int incorrect = 0;
        int n = 0;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                term = sc.nextLine();
                def = sc.nextLine();
                incorrect = sc.nextInt();
                sc.nextLine();
                if (cardMap.containsKey(term)) {
                    cardMap.replace(term, def);
                    hardCards.set(n, incorrect);
                } else {
                    cardMap.put(term, def);
                    hardCards.add(incorrect);
                }
                n++;
            }
            String str2 = String.format("%d cards have been loaded.\n", n);
            System.out.printf(str2);
            log.add(str2);
        } catch (FileNotFoundException e) {
            String str3 = "File not found.\n";
            System.out.print(str3);
            log.add(str3);
        }
    }
    public static void importCardsOnLoad(String fileName) {
        File file = new File(fileName);
        String def, term;
        int incorrect = 0;
        int n = 0;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                term = sc.nextLine();
                def = sc.nextLine();
                incorrect = sc.nextInt();
                sc.nextLine();
                cardMap.put(term, def);
                hardCards.add(incorrect);
                n++;
            }
            String str2 = String.format("%d cards have been loaded.\n", n);
            System.out.printf(str2);
            log.add(str2);
        } catch (FileNotFoundException e) {
            String str3 = "File not found.\n";
            System.out.print(str3);
            log.add(str3);
        }
    }
    public static void exportCards(Scanner s) {
        String str1 = "File name:\n";
        System.out.print(str1);
        log.add(str1);
        String fileName = s.nextLine();
        log.add(fileName + "\n");
        File file = new File(fileName);
        int i = 0;
        try (PrintWriter writer = new PrintWriter(file)) {
            for (var entry: cardMap.entrySet()) {
                writer.printf("%s\n", entry.getKey());
                writer.printf("%s\n",entry.getValue());
                writer.printf("%d\n",hardCards.get(i));
            }
            String str2 = String.format("%s cards have been saved.\n", cardMap.size());
            System.out.printf(str2);
            log.add(str2);
        } catch (IOException e) {
            String str3 = String.format("An exception occurs %s\n", e.getMessage());
            System.out.printf(str3);
            log.add(str3);
        }
    }
    public static void exportCardsOnExit(String fileName) {
        File file = new File(fileName);
        int i = 0;
        try (PrintWriter writer = new PrintWriter(file)) {
            for (var entry: cardMap.entrySet()) {
                writer.printf("%s\n", entry.getKey());
                writer.printf("%s\n",entry.getValue());
                writer.printf("%d\n",hardCards.get(i));
            }
            String str2 = String.format("%s cards have been saved.\n", cardMap.size());
            System.out.printf(str2);
            log.add(str2);
        } catch (IOException e) {
            String str3 = String.format("An exception occurs %s\n", e.getMessage());
            System.out.printf(str3);
            log.add(str3);
        }
    }
    public static void quizCards(Scanner s) {
        String str1 = "How many times to ask?\n";
        System.out.print(str1);
        log.add(str1);
        int n = Integer.parseInt(s.nextLine());
        log.add(String.valueOf(n) + "\n");
       // s.nextLine();
        String ans, term, def;
        String temp = "";
        Set<Map.Entry<String, String>> entrySet = cardMap.entrySet();
        String str2, str3, str4, str5;
        for (int i = 0; i < n; i++) {
            term = getRandomKey(entrySet);
            def = cardMap.get(term);
            str2 = String.format("Print the definition of \"" + term + "\":\n");
            System.out.printf(str2);
            log.add(str2);
            ans = s.nextLine();
            log.add(ans + "\n");
            if (def.equals(ans)) {
                str3 = "Correct!\n";
                System.out.print(str3);
                log.add(str3);
            } else if (cardMap.containsValue(ans)) {
                for (var entry : cardMap.entrySet()) {
                    if (entry.getValue().equals(ans)) {
                        temp = entry.getKey();
                        break;
                    }
                }
                int j = 0;
                for (var entry : cardMap.entrySet()) {
                    if (entry.getKey().equals(term)) {
                        hardCards.set(j, hardCards.get(j) + 1);
                        break;
                    }
                    j++;
                }
                str4 = String.format("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\".\n", def, temp);
                System.out.printf(str4);
                log.add(str4);
            } else {
                int j = 0;
                for (var entry : cardMap.entrySet()) {
                    if (entry.getKey().equals(term)) {
                        hardCards.set(j, hardCards.get(j) + 1);
                        break;
                    }
                    j++;
                }
                str5 = String.format("Wrong. The right answer is \"%s\".\n", def);
                System.out.printf(str5);
                log.add(str5);
            }
        }
    }
    public static String getRandomKey(Set<Map.Entry<String, String>> entrySet) {
        int size = entrySet.size();
        int item = new Random().nextInt(size);
        int i = 0;
        String temp = "";
        for(var entry : entrySet)
        {
            if (i == item)
                temp = entry.getKey();
            i++;
        }
        return temp;
    }
}

