# CMPT 371 Summer 2022 Matching Game

LAN Multiplayer card pair matching game with up to 4 players!

## Requirements

IntelliJ IDEA Community Edition (Version 2022.2)

JAVA JDK (Version 17.0.4)

JavaFX (Version 18.0.2)


## Running the Application
To run the application 

1. Clone the git repository.
2. Open the project in intellij
3. Change the run configuration 
   1. Main file: `Application`
   2. Add the VM options `--add-modules javafx.controls,javafx.fxml`
4. Run the configuration and enjoy the game :)

## How to Start the Game
Once the game has started there will be two options: `Create Server Game` and `Join Game As Client`

### Create Server Game
- This options will host a lobby for other players to join, as well as act as a client for the player.
- Once clicked, your IP address will appear on the window. Other players can use this IP address to connect to your game.
- When the maximum number of players has joined the game will display the board containing all the cards and the player scoreboards.

### Join Game As Client
- When this option is selected the window will display a text box where you can enter the IP of the player hosting the game.
- Once all players have connected the game will start.

## How to Play
- All players will start the game at the same time.
- When a player clicks on a card it will be flipped for all players, showing the face value of that card.
- Once a card is selected it becomes locked, so that other players can not click on it.
- To unlock the card, the player that clicked on it must click on another card.
- Once two cards are selected by a single player the game will determine if the two cards match.
  - If the two card match the player that selected them will receive a point.
  - If the two cards do not match they will be flipped back over to their backs and returned to into play for other players.
- Once all cards have been matched the game will end and each player's result will be displayed on screen.

## Video
DEMO: https://www.youtube.com/watch?v=eMsG3WkvgY0
