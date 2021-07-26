# Project Gomoku
### Requirements
- Can set up two players.
- For a human player, collect their name. (A random player's name is randomly generated.)
- For each stone placement, use the player's name to ask questions.
- Since the random player doesn't require input, the UI should display stone placement and the results of placement. 
  (Random player placement may fail since they don't know what they're doing.)
- Re-prompt on failed placement. The game must not proceed until a player has made a valid move.
- Display the final result of the game.
- Give the option to play again.
- Cannot modify Gomoku, Player, HumanPlayer, RandomPlayer, Stone, or Result classes

### State
- Gomoku object
- Board object

### Steps
- Ask for input by showing choices from the menu
- Create Gomoku (by creating players) & Board  
- Take turns to play
- For Human player:  \
  ask for stone placement location  \
  if invalid move: keep on prompting until it is a valid move  
- For Random player:  \
  show the move  \
  if invalid move: keep on repeating until a valid move is generated
- Print board after each move
- Check if a player has won after each move
    * if yes, prompt for playing again
    * otherwise, continue to next player move
    
### Classes & Methods
#### (only new classes added shown here)

- #### Game
  - responsible for setup & gameplay
  - fields
    * private Gomoku gomoku
    * private Board board
  - methods
    * public void run()
    * set up methods
      * private void setUp()
      * private Player createPlayer(int num)
      * private int choosePlayer(int num)
    * gameplay methods
      * private void takeTurns()
      * private Result humanPlayerTurn()
      * private Result randomPlayerTurn(Stone stone)
    * helper methods for collecting input
      * private String inputName(int num)
      * private int[] inputCoordinates()
      * private boolean askPlayAgain()
      * private int readNonNegativeInt(String prompt)
      * private String readRequiredString (String prompt)

- #### Board
  - responsible for generating & printing board
  - fields
    * private final char[][] board
  - methods
    * public void show(List<Stone> stones)
    * private void printBoard()
    * private void setBoard(List<Stone> stones)
    * private String makeHeader(int num)

