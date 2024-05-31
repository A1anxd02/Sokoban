import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
/**
 * @author: Alikhan Nurgazy your name here (SID) 22201009
 *
 *
 */
public class Sokoban {
    /**
     * The following constants are variables that you can use in your code.
     * Use them whenever possible. Try to avoid writing something like:
     * if (input == 'W') ...
     * instead
     * if (input == UP) ...
     */
    public static final char UP = 'W';
    public static final char DOWN = 'S';
    public static final char LEFT = 'A';
    public static final char RIGHT = 'D';
    public static final char PLAYER = 'o';
    public static final char BOX = '@';
    public static final char WALL = '#';
    public static final char GOAL = '.';
    public static final char BOXONGOAL = '%';

    /**
     * Finished. You are not allowed to touch this method.
     * The main method.
     */
    public static void main(String[] args) {
        new Sokoban().runApp();
    }
    /**
     * All coding of this method has been finished.
     * You are not supposed to add or change any code in this method.
     * However, you are required to add comments after every // to explain the code below.
     */
    public void runApp() {

        String mapfile = "map1.txt"; //change this to test other maps

        char[][] map = readmap(mapfile); // reads the value for char 2D array called map from readmap method
        char[][] oldMap = readmap(mapfile); // reads the value for char 2D array called oldMap from the readmap method
        if (map == null) { // checks the value of map array, if it is null will tell that map file is not found
            System.out.println("Map file not found");
            return;
        }
        int[] start = findPlayer(map); // we are using it to take position of the player in the map char 2D array
        if (start.length == 0) { // if we do not have values in side of the start array, it will say no player is not found
            System.out.println("Player not found");
            return;
        }
        int row = start[0];
        int col = start[1];
        while (!gameOver(map)) { // while method gameOver return false this while loop will keep working
            printMap(map);
            System.out.println("\nPlease enter a move (WASD): ");
            char input = readValidInput(); // takes the value for input from readValidInput method
            if (input == 'q')  // if input equals to q game will be finished
                break;
            if (input == 'r') {  // if input equals to r, game will be restarted by creating map from file again
                map = readmap(mapfile);
                row = start[0];    // value of row and col will be taken from start array
                col = start[1];
                continue;
            }
            if (input == 'h') { // if input equals to h, printHelp method will appear
                printHelp();
            }
            if (!isValid(map, row, col, input)) // checks if the move is valid. if move is valid while loop will keep working
                continue;
            movePlayer(map, row, col, input); // if move is valid, then movePlayer method will be called to move player

            fixMap(map, oldMap);  // fixing map, if player stepped into GOAL and then moved from this place, it will bring the GOAL to map again

            int[] newPos = findPlayer(map); // after movePlayer method, we need to find new position of the player to keep playing the game
            row = newPos[0]; // row and col equals to the values from the newPos array
            col = newPos[1]; 

        }
        System.out.println("Bye!");
    }
    /**
     * Print the Help menu. 
     * TODO:
     * 
     * Inspect the code in runApp() and find out the function of each characters. 
     * The first one has been done for you.
     */
    public void printHelp() { // we need this method to print help menu to the player when he asks for it
        System.out.println("Sokoban Help:"); 
        System.out.println("Move up: W");
        System.out.println("Move down: S");
        System.out.println("Move left: A");
        System.out.println("Move right: D");
        System.out.println("Player sign: o");
        System.out.println("Box sign: @");
        System.out.println("Wall sign: #");
        System.out.println("GOAL sign: .");
        System.out.println("BOXONGOAL sign: %");
    }
    /**
     * Reading a valid input from the user.
     * 
     * TODO
     * 
     * This method will return a character that the user has entered. However, if a user enter an invalid character (e.g. 'x'),
     * the method should keep prompting the user until a valid character is entered. Noted, there are all together 7 valid characters 
     * which you need to figure out yourself.
     */
    public char readValidInput() {
        Scanner sc = new Scanner(System.in); // creating scanner to allow player to type his move
        char vla;
        String temp = sc.nextLine();
        vla = temp.charAt(0); // converts written move to char so program can read it
        while(true){ // while this loop is true program checks different cases and act accordingly
            if(temp.length()== 1) { // i added this if to check if written value is longer than single char. Because there can be miss-type like: AS, or Df, and program will still read it, so added this if to make sure length is only 1 char
                if (vla == UP) {
                    break;
                } else if (vla == DOWN) {
                    break;
                } else if (vla == LEFT) {
                    break;
                } else if (vla == RIGHT) {
                    break;
                } else if (vla == 'q') {
                    break;
                } else if (vla == 'r') {
                    break;
                } else if (vla == 'h') {
                    break;
                } else { // if player entered other values apart from: UP,DOWN,RIGHT,LEFT,q,r or h program will ask him to enter correct character
                    System.out.println("Enter correct character: ");
                    vla = sc.nextLine().charAt(0);
                }
            }
            else{ // if length is more than 1 char, player will be asked to enter correct character
                System.out.println("Enter correct character: ");
                vla = sc.nextLine().charAt(0);
            }
        }
        return vla;
    }

    /**
     * Mysterious method.
     * 
     * TODO
     * 
     * We know this method is to "fix" the map. But we don't know how it does and why it is needed.
     * You need to figure out the function of this method and implement it accordingly.
     * 
     * You are given an additional demo program that does not implement this method. 
     * You can run them to see the difference between the two demo programs.
     */
    public void fixMap(char[][] map, char [][] oldMap) { // we need this method to make sure that GOAL will not disappear after player stand on it and left its position without putting BOX to it
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(oldMap[i][j] == GOAL){
                    if(map[i][j] != BOXONGOAL && map[i][j] != PLAYER){
                        map[i][j] = GOAL;
                    }
                }
            }
        }
    }

    /**
     * To move a box in a map.
     * 
     * TODO
     * 
     * This method will move a box in the map. The box will be moved to the direction specified by the parameter "direction".
     * You must call this method somewhere in movePlayer() method.
     * 
     * After this method, a box should be moved to the new position from the coordinate [row, col] according to the direction.
     * For example, if [row, col] is [2, 5] and the direction is 'S', the box should be moved to [3, 5].
     * 
     * If a box is moved to a goal, the box should be marked as BOXONGOAL.
     * If a box is moved to a non-goal, the box should be marked as BOX.
     * You should set the original position of the box to ' ' in this method.
     * 
     * Note, you may always assume that this method is called when the box can be moved to the direction. 
     * During grading, we will never call this method when the box cannot be moved to the direction. 
     */
    public void moveBox(char[][] map, int row, int col, char direction) {
        if(map[row][col] == BOX){
            map[row][col]= ' '; // position where BOX stands right now will be free space
        }
        switch(direction){ // I used switch to cover different cases and to make code look better
            case UP: // if char direction equals to UP, row value will be decreased and then it checks if this new row position occupied by different things
                row--;
                if(map[row][col] == GOAL){ // if new row position equals to GOAL it will become BOXONGOAL because according to logic we are placing BOX to it
                    map[row][col] = BOXONGOAL;
                }

                else if(map[row][col] == ' '){ // if new row position is empty space, BOX will just move there
                    map[row][col] = BOX;
                }
                break;
            case DOWN: // same logic I explained in first case applies to this one too
                row++;
                if(map[row][col] == GOAL){
                    map[row][col] = BOXONGOAL;
                }
                else if(map[row][col] == ' '){
                    map[row][col] = BOX;
                }
                break;
            case LEFT: // same logic I explained in first case applies to this one too
                col--;
                if(map[row][col] == GOAL){
                    map[row][col] = BOXONGOAL;
                }
                else if(map[row][col] == ' '){
                    map[row][col] = BOX;
                }
                break;
            case RIGHT: // same logic I explained in first case applies to this one too
                col++;
                if(map[row][col] == GOAL){
                    map[row][col] = BOXONGOAL;
                }
                else if(map[row][col] == ' '){
                    map[row][col] = BOX;
                }
                break;
        }
    }
    /**
     * To move the player in the map.
     * 
     * TODO
     * 
     * This method will move the player in the map. The player will be moved to the direction specified by the parameter "direction".
     * 
     * After this method, the player should be moved to the new position from the coordinate [row, col] according to the direction.
     * At the same time, the original position of the player should be set to ' '.
     * 
     * During the move of the player, it is also possible that a box is also moved.
     * 
     * Note, you may always assume that this method is called when the player can be moved to the direction. 
     * During grading, we will never call this method when the player cannot be moved to the direction.
     */
    public void movePlayer(char[][] map, int row, int col, char direction) {
        map[row][col] = ' '; // current position of the player will become free space

        switch (direction){ // I am using switch to cover different cases and to make code look better
            case UP:
                row--; // i need this to check if new position where player is going to move occupied by something else
                if(map[row][col] == BOX || map[row][col] == BOXONGOAL){ // if new position occupied by BOX or BOXONGOAL we need to use moveBox method
                    moveBox( map,row,col, direction);
                    break;
                }
                break;
            case DOWN:
                row++; // i need this to check if new position where player is going to move occupied by something else
                if(map[row][col] == BOX || map[row][col] == BOXONGOAL) { // if new position occupied by BOX or BOXONGOAL we need to use moveBox method
                    moveBox(map, row, col, direction);
                    break;
                }
                break;
            case LEFT:
                col--; // i need this to check if new position where player is going to move occupied by something else
                if(map[row][col] == BOX || map[row][col] == BOXONGOAL) { // if new position occupied by BOX or BOXONGOAL we need to use moveBox method
                    moveBox(map, row, col, direction);
                    break;
                }
                break;
            case RIGHT:
                col++; // i need this to check if new position where player is going to move occupied by something else
                if(map[row][col] == BOX || map[row][col] == BOXONGOAL) { // if new position occupied by BOX or BOXONGOAL we need to use moveBox method
                    moveBox(map, row, col, direction);
                    break;
                }
                break;
        }
            map[row][col] = PLAYER; // new position in the map is equal to PLAYER


    }
    /**
     * To check if the game is over.
     * 
     * TODO
     * 
     * This method should return true if the game is over, false otherwise.
     * The condition for game over is that there is no goal left in the map that is not covered by a box.
     * 
     * According to this definition, if the number of goal is actually more than the number of boxes,
     * the game will never end even through all boxes are placed on the goals.
     */
    public boolean gameOver(char[][] map) {
        for(int i = 0; i<map.length; i++){
            for(int j = 0; j<map[i].length; j++){
                if(map[i][j] == GOAL){ // I created for loops to check if we still have GOAL in the map, and if we still have it, game is not finished
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * To count the number of rows in a file.
     * 
     * TODO
     * 
     * This method should return the number of rows in the file which filename is stated in the argument. 
     * If the file is not found, it should return -1.
     */
    public int numberOfRows(String fileName) {
        try{
            int num_of_rows = 0;
            File input2 = new File(fileName);
            Scanner sc2 = new Scanner(input2);

            while(sc2.hasNextLine()){ // while this file has next line number of rows will be counted
                sc2.nextLine();
                num_of_rows += 1;
            }
            return num_of_rows;
        }
        catch (Exception e){ // if no file is found, program will catch the exception and return the -1
            return -1;

        }

    }
    /**
     * To read a map from a file.
     * 
     * TODO
     * 
     * This method should return a 2D array of characters which represents the map.
     * This 2D array should be read from the file which filename is stated in the argument.
     * If the file is not found, it should return null.
     * 
     * The number of columns in each row may be different. However, there is no restriction on
     * the number of columns that is declared in the array. You can declare the number of columns
     * in your array as you wish, as long as it is enough to store the map.
     * 
     * That is, if the map is as follow,
     * ####
     * #.@o#
     * #  #
     * ###
     * your array may be declared as
     * char[][] map = {{'#', '#', '#', '#'},
     *                 {'#', '.', '@', 'o', '#'},
     *                 {'#', ' ', ' ', '#'},
     *                 {'#', '#', '#'} };
     * or something like
     * char[][] map = {{'#', '#', '#', '#', ' ', ' ', ' '},
     *                 {'#', '.', '@', 'o', '#', ' ', ' '},
     *                 {'#', ' ', ' ', '#', ' ', ' ', ' '},
     *                 {'#', '#', '#', ' ', ' ', ' ', ' '} };
     */
    public char[][] readmap(String fileName)  {
        try{
            File inputFile = new File(fileName);
            Scanner in = new Scanner(inputFile);
            Scanner in2 = new Scanner(inputFile);// created this scanner to count number of columns
            int k = 0;
            int rows = numberOfRows(fileName);
            int [] col_num = new int[rows];
            while(in2.hasNextLine()){
                String line1 = in2.nextLine();
                col_num[k]= line1.length();
                k++;
            }
            int max_col_num = 0;
            for(int i = 0; i < col_num.length; i++ ){ // checking the biggest value between rows length to know for sure how many columns we need in our new read_map char
                if(col_num[i] > max_col_num){
                    max_col_num = col_num[i];
                }
            }
            in2.close();

            char[][] read_map = new char[numberOfRows(fileName)][max_col_num];
            for(int i = 0; i < read_map.length; i++){
                String line2 = in.nextLine();
                for(int j = 0; j < read_map[i].length; j++) {
                    if (j < line2.length()) {
                        read_map[i][j] = line2.charAt(j); // putting values in side of the read_map by converting string line2 to char
                    } else {
                        read_map[i][j] = ' ';
                    }
                }
            }
            in.close();
            return read_map;
        }
        catch(FileNotFoundException e){ // if no file found program will catch the exception and return null
            return null;
        }
    }
    /**
     * To find the coordinate of player in the map.
     * 
     * TODO
     * 
     * This method should return a 2D array that stores the [row, col] of the player in the map.
     * For example, if the map is as follow,
     * ####
     * #.@o#
     * #  #
     * ###
     * this method should return {1, 3}.
     * 
     * In case there is no player in the map, this method should return null.
     */
    public int[] findPlayer(char[][] map) {
        int[] pl_pos = new int[2];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == PLAYER){ // searching for PLAYER value in the map and then writes the position of it to pl_pos array
                    pl_pos[0] = i;
                    pl_pos[1] = j;
                }
            }
        }
        return pl_pos;

    }
    /**
     * To check if a move is valid.
     * 
     * TODO
     * 
     * This method should return true if the move is valid, false otherwise.
     * The parameter "map" represents the map.
     * The parameter "row" and "col" indicates where the player is.
     * The parameter "direction" indicates the direction of the move.
     * At the end of the method, this method should not change any content of the map.
     * 
     * The physics of the game is as follow:
     * 1. The player can only move to a position that is not occupied by a wall or a box.
     * 2. If the player is moving to a position that is occupied by a box, the box can only be moved to a position that is not occupied by a wall or a box.
     * 
     * Thus, in the following condition, the player can move to the right
     * o #   <-- there is a space
     * o@ #  <-- there is a space right to the box.
     * In the following condition, the player cannot move to the right
     * o#    <-- there is a wall
     * o@#   <-- there is a wall right to the box.
     * o@@ # <-- there is a box right to the box.
     */
    public boolean isValid(char[][] map, int row, int col, char direction) {
        switch(direction){
            case UP:
                if(map[row-1][col] == ' ' || map[row-1][col] == GOAL){ // if next position is free space or GOAL returns true, means player can go there
                    return true;
                }
                break;
            case DOWN:
                if(map[row+1][col] == ' ' || map[row+1][col] == GOAL){ // if next position is free space or GOAL returns true, means player can go there
                    return true;
                }
                break;
            case LEFT:
                if(map[row][col-1] == ' ' || map[row][col-1] == GOAL){ // if next position is free space or GOAL returns true, means player can go there
                    return true;
                }
                break;
            case RIGHT:
                if(map[row][col+1] == ' ' || map[row][col+1] == GOAL){ // if next position is free space or GOAL returns true, means player can go there
                    return true;
                }
        }
        if(map[row][col] != ' '){ // if next position is not free space, going through other switch
            switch (direction){
                case UP:
                    if(((map[row-1][col] == BOX) || (map[row-1][col] == BOXONGOAL)) && (!((map[row-2][col] == WALL) || (map[row-2][col] == BOX) ||(map[row-2][col] == BOXONGOAL)))){ // if next position is equal to BOX or BOXONGOAL AND is not equal to WALL or BOX or BOXONGOAL return true
                        return true;
                    }
                    else if(map[row-1][col] == WALL){ // if next position equals to WALL return false
                        return false;
                    }
                    break;
                case DOWN:
                    if(((map[row+1][col] == BOX) || (map[row+1][col] == BOXONGOAL)) && (!((map[row+2][col] == WALL) || (map[row+2][col] == BOX) ||(map[row+2][col] == BOXONGOAL)))) { // if next position is equal to BOX or BOXONGOAL AND is not equal to WALL or BOX or BOXONGOAL return true
                        return true;
                    }
                    else if(map[row+1][col] == WALL){ // if next position equals to WALL return false
                        return false;
                    }
                    break;
                case LEFT:
                    if(((map[row][col - 1] == BOX) || (map[row][col-1] == BOXONGOAL)) && (!((map[row][col-2] == WALL) || (map[row][col-2] == BOX) ||(map[row][col-2] == BOXONGOAL)))) { // if next position is equal to BOX or BOXONGOAL AND is not equal to WALL or BOX or BOXONGOAL return true
                        return true;
                    }
                    else if(map[row][col-1] == WALL){ // if next position equals to WALL return false
                        return false;
                    }
                    break;
                case RIGHT:
                    if(((map[row][col+1] == BOX) || (map[row][col+1] == BOXONGOAL)) && (!((map[row][col+2] == WALL) || (map[row][col+2] == BOX)||(map[row][col+2] == BOXONGOAL)))) { // if next position is equal to BOX or BOXONGOAL AND is not equal to WALL or BOX or BOXONGOAL return true
                        return true;
                    }
                    else if(map[row][col+1] == WALL){ // if next position equals to WALL return false
                        return false;
                    }

                    break;
            }
        }
        return false;
    }
    /**
     * To print the map.
     * 
     * TODO
     * 
     * This method should print the map in the console.
     * At the top row, it should print a space followed by the last digit of the column indexes. 
     * At the leftmost column, it should print the last two digits of row indexes, aligning to the left.
     */
    public void printMap(char[][] map) {
        int max_col_num = 0;
        for(int g = 0; g < map.length; g++){// we need this loop to find the biggest value of the row's length
            if(map[g].length > max_col_num){
                max_col_num = map[g].length;
            }
        }
        System.out.print("  ");
        for(int k = 0; k < max_col_num; k++){
            System.out.print(k % 10); //printing only last digit of the column indexes until it reaches last column
        }
        System.out.println();

        for(int i = 0; i < map.length; i++){
            System.out.printf("%-2d", i );
            for(int j = 0; j < map[i].length; j++){
                System.out.print(map[i][j]); // printing map

            }
            System.out.println();

        }

    }

}