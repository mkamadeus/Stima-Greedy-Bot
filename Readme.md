# Tugas Besar 1 Stima : Greedy Bot for Entelect Challenge 2018

> ..to be as greedy as a greedy bot can be greedy.

## Prerequisites

* Ensure that you have Java installed. JDK 8 is recommended. If you haven't already, visit [this link]([http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html]). 


## Compiling the Bot

TBA

## Running the Simulation

### Step 1 : Running the program

Let's start off by running your very first match.

For Windows users, simply run the `run.bat` file.

For Linux users, open the terminal in this directory and enter `make` or `make run` command.

If some text appears, you've successfully ran the program. If it shows some errors, check your bot directory and bot location in the `bot.json` in your bot directory.

### Step 2 : Config Setup

This program have several options that can be used; such as changing between different bots and console. The format of the `game-runner-config.json` is as follows:

| Field | Usage |
|---|---|
| `round-state-output-location` | This is the path to where you want the match folder in which each round's folder with its respective logs will be saved.  |
| `game-config-file-location`  | This is the path to the game-config.properties file that is used to set various game-engine settings such as map size and building stats.|
|  `max-runtime-ms` | This is the amount of milliseconds that the game runner will allow a bot to run before making its command each round. |
| `player-a` | This is the path to the folder containing the `bot.json` file for Player A. |
| `player-b` | This is the path to the folder containing the `bot.json` file for Player B. |
    
> Note this; if you would like to replace one of the bot players with a console player, just use the word "console" as the path.

On the other hand, `bot.json` is found at the bot directory. The format of the `bot.json` is as follows:

| Field | Usage |
|---|---|
| `author` | This is the name of the person who wrote the bot. |
| `email`  | This is an email address where the author of the bot can be contacted if there are any questions. |
|  `nickName` | This is a nickname for the bot that will be used in visualisations. |
| `botLocation` | This is a relative path to the folder containing the compiled bot file for the specific language. |
| `botFileName` | This is the compiled bot file's name. |
| `botLanguage` | This is the language name in which the bot is coded. This bot uses Java, so this field will be filled with `"java"`. |

### Step 3 : Logic Modification (Optional)

For the sake of our university project, we made a *greedy-based* bot. But, you also have the option to change the logic as much as you want.

Here is a brief explanation of how a bot should work:

1. The bot will be called to run once per round in the game. After every round, the bot will be terminated and restarted for the next round.
2. *For each round* the bot should go through the following process:
   1. Firstly read in the `state.json` file that contains the game map and all the round properties.
   2. Apply your logic to these properties and decide on what your next move is and where you want to apply it.
   3. Finally, write your move into the `command.txt` file with a format like below:
    ```
    <coord-x>,<coord-y>,<building-type>
    
    Building Types:
    0 = Defense
    1 = Attack
    2 = Energy
    3 = Tesla
    ```

Here are short explanations of the files mentioned above:

| Filename | Usage |
|---|---|
| `state.json` | This file keeps track of everything that is on the map, the player's health, energy and scores.|
| `command.txt`  | This file will be read by the game engine to know what your bot decided to do for that specific round.|

> Examples are available in the `examples` directory.   