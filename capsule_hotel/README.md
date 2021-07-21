# Project Hotel Capsule
### Requirements
- On start up, the application prompts for the hotel's capacity
- The application can check in a guest in an unoccupied capsule
- The application can check out a guest from an occupied capsule
- The application can view guests, and their capsule numbers in group of 10

### State
- initial capacity
- string array

### Steps
- Get initial capacity
- Set array
- Show choices from menu
- If check in:    
    * get guest name and capsule number
    * add name to array
    * check in fails if all rooms occupied or room with the chosen capsule number occupied
- If check out:    
    * get capsule number
    * remove guest from that location in array 
    * check out fails if all rooms are unoccupied or room with the chosen capsule number is unoccupied
- If view guests: 
     * get capsule number 
     * show 11 capsules closest to that capsule: 5 smaller and 5 larger
     * if 11 capsules not in neighbourhood, show maximum possible
- If exit:
    * ask for confirmation
    * close program or show menu again depending on input

### Methods
public static void main(String[] args)  \
public static int startUp() \
public static void runProgram (String[] roomArray, int initialCapacity) \
public static int mainMenu() \
public static void checkIn(String[] array, int initialCapacity) \
public static void checkOut(String[] array, int initialCapacity) \
public static void viewGuests(String[] array, int initialCapacity) \
public static boolean confirmExit() \
public static boolean isFull(String[] array)  \
public static boolean isEmpty(String[] array) \
public static int readPositiveInt(String prompt) \
public static String readRequiredString (String prompt) \
public static String makeHeading(String text, String decoration) 

