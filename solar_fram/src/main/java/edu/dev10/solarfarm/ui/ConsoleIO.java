package edu.dev10.solarfarm.ui;

import edu.dev10.solarfarm.models.MaterialType;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Scanner;

@Component
public class ConsoleIO {
    private final Scanner scanner;

//    public ConsoleIO(Scanner scanner) {
//        this.scanner = scanner;
//    }


    public ConsoleIO() {
       this.scanner = new Scanner(System.in);
    }

    public int readInt(String prompt, boolean isRequired) {
        int result = Integer.MAX_VALUE;
        boolean isValid = false;
        do {
            String value = readString(prompt, isRequired);
            if (!isRequired && value.isBlank()) {
                break;
            }
            try {
                result = Integer.parseInt(value.trim());
                isValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Value must be a number.");
            }
        } while (!isValid);
        return result;
    }
    public int readPositiveInt(String prompt, boolean isRequired) {
        int result;
        boolean isPositive = false;
        do {
            result = readInt(prompt, isRequired);
            if (!isRequired && result == Integer.MAX_VALUE) {
                break;
            }
            if (result > 0) {
                isPositive = true;
            } else {
                System.out.println("Value must be positive.");
            }
        } while (!isPositive);
        return result;
    }
    public int readInt(String prompt, int min, int max, boolean isRequired) {
        int result;
        do {
            result = readInt(prompt, isRequired);
            if (!isRequired && result == Integer.MAX_VALUE) {
                break;
            }
            if (result < min || result > max) {
                System.out.printf("Value must be between %d and %d.\n", min, max);
            }
        } while (result < min || result > max);
        return result;
    }
    public String readString(String prompt, boolean isRequired) {
        String result = "";
        do {
            System.out.print(prompt);
            result = scanner.nextLine();
            if (result.isBlank() && isRequired) {
                System.out.println("Value is required.");
            }
        } while (result.isBlank() && isRequired);
        return result;
    }
    public MaterialType readMaterialType(boolean isRequired) {
        MaterialType[] materials = MaterialType.values();
        System.out.println("Materials Available");
        for (int i = 1; i <= materials.length; i++) {
            System.out.printf("%s. %s \n",i, materials[i - 1].getMaterialName());
        }
        int index = readInt("Select [1-5]: ", 1, 5, isRequired);
        return (!isRequired && index == Integer.MAX_VALUE) ? null : materials[index - 1];
    }
    public boolean readBoolean(String prompt, boolean isRequired, boolean previous) {
        String input;
        boolean check = false;
        boolean askAgain;
        do {
            input = readString(prompt, isRequired);
            if (input.isBlank() && !isRequired) {
                return previous;
            }
            input = input.trim().toLowerCase(Locale.ROOT);
            if (input.equals("y") || input.equals("yes")) {
                check = true;
                askAgain = false;
            } else if (input.equals("n") || input.equals("no")) {
                check = false;
                askAgain = false;
            } else {
                askAgain = true;
            }
        } while (askAgain);
        return check;
    }

}
