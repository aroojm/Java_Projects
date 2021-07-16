import java.util.Arrays;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int initialCapacity = startUp(scanner);
        String[] roomArray = new String[initialCapacity];
        runProgram(scanner, roomArray, initialCapacity);

        System.out.println(Arrays.toString(roomArray));
    }
    public static void runProgram (Scanner scanner, String[] roomArray, int initialCapacity) {
        int option;
        do {
            option = mainMenu(scanner);
            switch (option) {
                case 1:
                    checkIn(scanner, roomArray, initialCapacity);
                    break;
                case 2:
                    checkOut(scanner, roomArray, initialCapacity);
                    break;
                case 3:
                    viewGuests(scanner, roomArray, initialCapacity);
                    break;
                case 4:
                    if (confirmExit(scanner)) {
                        System.out.println("GOODBYE!");
                    } else {
                        option = 0;
                    }
                    break;
                default:
                    break;
            }
        } while (option != 4);
    }
    public static int startUp(Scanner scanner) {
        System.out.println(makeHeading("WELCOME TO HOTEL CAPSULE!", "*"));;
        String startPrompt = "Enter the number of capsules available: ";
        int capsules = readPositiveInt(scanner, startPrompt);
        System.out.printf("There are %d unoccupied capsules ready to be booked.\n", capsules);
        return capsules;
    }
    public static int mainMenu(Scanner scanner) {
        int option;
        String choosePrompt = "Choose an option [1-4]: ";
        String menuString = makeHeading("GUEST MENU","=") +
                "\n1. Check In \n2. Check Out \n3. View Guests \n4. Exit";
        boolean askAgain = true;
        do {
            System.out.println(menuString);
            option = readPositiveInt(scanner, choosePrompt);
            if (option > 4) {
                System.out.println("Invalid Option. Try Again.");
            } else {
                askAgain = false;
            }
        } while (askAgain);

//        System.out.println("*** GUEST MENU ***");
//        System.out.println("1. Check In");
//        System.out.println("2. Check Out");
//        System.out.println("3. View Guests");
//        System.out.println("4. Exit");

        return option;
    }
    public static void checkIn(Scanner scanner, String[] array, int initialCapacity) {
        System.out.println(makeHeading("GUEST CHECK IN","-"));
        String guestPrompt = "Guest Name: ";
        String capsulePrompt = String.format("Capsule #[1-%d]: ",initialCapacity);
        String guest;
        int capsuleNumber;
        boolean askAgain = true;
        if (isFull(array)) {
            System.out.println("Hotel is fully booked. Cannot check-in new guests.");
        } else {
            do {
                guest = readRequiredString(scanner, guestPrompt);
                capsuleNumber = readPositiveInt(scanner, capsulePrompt);
                if (capsuleNumber > initialCapacity) {
                    System.out.println("Invalid Capsule Number");
                } else if (array[capsuleNumber - 1] == null) {
                    array[capsuleNumber - 1] = guest;
                    System.out.println("Success :)");
                    System.out.printf("%s is booked in capsule %d.\n", guest, capsuleNumber);
                    askAgain = false;
                } else {
                    System.out.println("Error :(");
                    System.out.printf("Capsule #%d is occupied.\n", capsuleNumber);
                }
            } while (askAgain);
        }
    }
    public static void checkOut(Scanner scanner, String[] array, int initialCapacity) {
        System.out.println(makeHeading("GUEST CHECK OUT","-"));
        String capsulePrompt = String.format("Capsule #[1-%d]: ",initialCapacity);
        int capsuleNumber;
        boolean askAgain = true;
        if (isEmpty(array)) {
            System.out.println("Hotel is empty. Cannot check-out any guests.");
        } else {
            do {
                capsuleNumber = readPositiveInt(scanner, capsulePrompt);
                if (capsuleNumber > initialCapacity) {
                    System.out.println("Invalid Capsule Number");
                } else if (array[capsuleNumber - 1] == null) {
                    System.out.println("Error :(");
                    System.out.printf("Capsule #%d is unoccupied.\n", capsuleNumber);
                } else {
                    System.out.println("Success :)");
                    System.out.printf("%s checked out from capsule #%d.\n", array[capsuleNumber - 1], capsuleNumber);
                    array[capsuleNumber - 1] = null;
                    askAgain = false;
                }
            } while (askAgain);
        }

    }
    public static void viewGuests(Scanner scanner, String[] array, int initialCapacity) {
        System.out.println(makeHeading("VIEW GUESTS","-"));
        String capsulePrompt = String.format("Capsule #[1-%d]: ",initialCapacity);
        int capsuleNumber = readPositiveInt(scanner, capsulePrompt);
        // range of capsules
        int min = capsuleNumber - 5;
        int max = capsuleNumber + 5;
        // update range if values go beyond array size
        while (min < 1) { min++; }
        while (max > array.length) { max--; }
        String name;
        System.out.println("\nCapsule: Guest");
        for (int i = min; i <= max ; i++) {
            System.out.printf(array[i-1] == null ?
                    String.format("%d: [unoccupied]\n", i) : String.format("%d: %s\n", i, array[i - 1]));
        }
    }
    public static boolean confirmExit(Scanner scanner) {
        System.out.println(makeHeading("EXIT","-"));
        String exitPrompt = "Are you sure you want to exit? \nAll data will be lost. \nExit [y/n] ";
        String response = readRequiredString(scanner, exitPrompt);
        boolean exit;
        if (response.substring(0, 1).equalsIgnoreCase("y")) {
            exit = true;
        } else if (response.substring(0, 1).equalsIgnoreCase("n")) {
            exit = false;
        } else {
            System.out.println("Invalid input.");
            exit = false;
        }
        return exit;
    }
    public static boolean isFull(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return false;
            }
        }
        return true;
    }
    public static boolean isEmpty(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                return false;
            }
        }
        return true;
    }
    public static int readPositiveInt(Scanner scanner, String prompt) {
        int input;
        boolean askAgain = true;
        do {
            System.out.print(prompt);
            input = Integer.parseInt(scanner.nextLine());
            if (input <= 0) {
                System.out.println("Choose a positive number.");
            } else {
                askAgain = false;
            }
        } while (askAgain);
        return input;
    }
    public static String readRequiredString (Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
        } while (input.isBlank());
        return input;
    }
    public static String makeHeading(String text, String decoration) {
        String line = decoration.repeat(8 + text.length());
        return line + "\n    " + text + "    \n" + line;
    }

}
