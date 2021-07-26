import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        int initialCapacity = startUp();
        String[] roomArray = new String[initialCapacity];
        runProgram(roomArray, initialCapacity);
    }
    public static int startUp() {
        System.out.println(makeHeading("WELCOME TO HOTEL CAPSULE!", "*"));
        String startPrompt = "Enter the number of capsules available: ";
        int capsules = readPositiveInt(startPrompt);
        System.out.printf("There are %d unoccupied capsules ready to be booked.\n", capsules);
        return capsules;
    }
    public static void runProgram (String[] roomArray, int initialCapacity) {
        int option;
        do {
            option = mainMenu();
            switch (option) {
                case 1:
                    checkIn(roomArray, initialCapacity);
                    break;
                case 2:
                    checkOut(roomArray, initialCapacity);
                    break;
                case 3:
                    viewGuests(roomArray, initialCapacity);
                    break;
                case 4:
                    if (confirmExit()) {
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
    public static int mainMenu() {
        int option;
        String choosePrompt = "Choose an option [1-4]: ";
        String menuString = makeHeading("GUEST MENU","=") +
                "\n1. Check In \n2. Check Out \n3. View Guests \n4. Exit";
        boolean askAgain = true;
        do {
            System.out.println(menuString);
            option = readPositiveInt(choosePrompt);
            if (option > 4) {
                System.out.println("Invalid Option. Try Again.");
            } else {
                askAgain = false;
            }
        } while (askAgain);
        return option;
    }
    public static void checkIn(String[] array, int initialCapacity) {
        System.out.println(makeHeading("GUEST CHECK IN","-"));
        String guestPrompt = "Guest Name: ";
        String capsulePrompt = String.format("Capsule #[1-%d]: ",initialCapacity);
        String guest;
        int capsuleNumber;
        boolean askAgain = true;
        if (isFull(array)) {
            System.out.println("Hotel is fully booked. Cannot check-in new guests.");
        } else {
            guest = readRequiredString(guestPrompt);
            do {
                capsuleNumber = readPositiveInt(capsulePrompt);
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
    public static void checkOut(String[] array, int initialCapacity) {
        System.out.println(makeHeading("GUEST CHECK OUT","-"));
        String capsulePrompt = String.format("Capsule #[1-%d]: ",initialCapacity);
        int capsuleNumber;
        boolean askAgain = true;
        if (isEmpty(array)) {
            System.out.println("Hotel is empty. Cannot check-out any guests.");
        } else {
            do {
                capsuleNumber = readPositiveInt(capsulePrompt);
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
    public static void viewGuests(String[] array, int initialCapacity) {
        System.out.println(makeHeading("VIEW GUESTS","-"));
        String capsulePrompt = String.format("Capsule #[1-%d]: ",initialCapacity);
        int capsuleNumber;
        boolean askAgain = true;
        do {
            capsuleNumber = readPositiveInt(capsulePrompt);;
            if (capsuleNumber > initialCapacity) {
                System.out.println("Invalid Capsule Number");
            } else {
                // range of capsules
                int min = capsuleNumber - 5;
                int max = capsuleNumber + 5;
                // update range if values go beyond array bounds
                while (min < 1) { min++; }
                while (max > array.length) { max--; }
                System.out.println("Capsule: Guest");
                for (int i = min; i <= max ; i++) {
                    System.out.print(array[i-1] == null ?
                            String.format("%d: [unoccupied]\n", i) : String.format("%d: %s\n", i, array[i - 1]));
                }
                askAgain = false;
            }
        } while(askAgain);

    }
    public static boolean confirmExit() {
        System.out.println(makeHeading("EXIT","-"));
        String exitPrompt = "Are you sure you want to exit? \nAll data will be lost. \nExit [y/n] ";
        String response = readRequiredString(exitPrompt).trim().substring(0, 1);
        boolean exit;
        if (response.equalsIgnoreCase("y")) {
            exit = true;
        } else if (response.equalsIgnoreCase("n")) {
            exit = false;
        } else {
            System.out.println("Invalid input.");
            exit = false;
        }
        return exit;
    }
    // helper methods
    /**
     * Returns true if each element in a string array is not null; false otherwise.
     * @param array String[]
     * @return boolean
     */
    public static boolean isFull(String[] array) {
        for (String item : array) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }
    /**
     * Returns true if each element in a string array is null; false otherwise.
     * @param array String[]
     * @return boolean
     */
    public static boolean isEmpty(String[] array) {
        for (String item : array) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }
    public static int readPositiveInt(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int input;
        boolean askAgain = true;
        do {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Choose a positive number.");
                } else {
                    askAgain = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input an integer.");
                input = 0; // to compile, input variable needs to be initialized
            }
        } while (askAgain);
        return input;
    }
    public static String readRequiredString (String prompt) {
        Scanner scanner = new Scanner(System.in);
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
