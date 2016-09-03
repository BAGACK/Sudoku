/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.comze_instancelabs.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Sudoku helper class.
 * 
 * @author instancelabs
 * @author mepeisen
 */
public class Sudoku
{
    /** row count. */
    static final int  ROW          = 9;
    /** column count. */
    static final int  COLUMN       = 9;
    
    /** number of iterations to shuffle. */
    private final int shuffleCount = 100000;
    
    /** Difficulty. */
    private int       difficulty   = 20;
    
    /** the sudoku answer/ solution. */
    private int[][]   answer       = new int[ROW][COLUMN];
    private int[][]   problem      = new int[ROW][COLUMN];
    private int[][]   player       = new int[ROW][COLUMN];
    private int[][]   comp         = new int[ROW][COLUMN];
    
    // 2 arrays to
    private int[]     ansArray;
    private int[]     playerArray;
    
    // Stores variables to generate random sudoku.
    private int[]     variables    = new int[ROW];
    
    /** Random class */
    private Random    rand         = new Random();
    
    /**
     * Constructor
     * 
     * @param dif
     *            difficulty
     */
    public Sudoku(int dif)
    {
        this.difficulty = dif;
        this.ansArray = new int[this.difficulty];
        this.playerArray = new int[this.difficulty];
        build();
    }
    
    /**
     * Creates a new sudoku and copies the data from src
     * @param src
     */
    private Sudoku(Sudoku src)
    {
        this.ansArray = src.ansArray.clone();
        this.answer = src.deepCopy(src.answer);
        this.comp = src.deepCopy(src.comp);
        this.difficulty = src.difficulty;
        this.player = src.deepCopy(src.player);
        this.playerArray = src.playerArray.clone();
        this.problem = src.deepCopy(src.problem);
        this.variables = src.variables.clone();
    }
    
    /**
     * Creates a deep copy.
     * @param matrix
     * @return deep copy.
     */
    private int[][] deepCopy(int[][] matrix)
    {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }
    
    /**
     * Clones this sudoku
     * @return clone
     */
    @Override
    public Sudoku clone()
    {
        return new Sudoku(this);
    }
    
    /** Public method to build the whole game. */
    public void build()
    {
        generate();
        storeAns();
        // display();
    }
    
    /**
     * Public method to check if player's sudoku has reached the answer.
     * 
     * @return true if the answer is correct.
     */
    public boolean checkAns()
    {
        for (int i = 0; i < this.ansArray.length; i++)
        {
            if (this.playerArray[i] != this.ansArray[i])
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Public method to clear data.
     */
    public void clear()
    {
        copy(this.problem, this.player);
    }
    
    /**
     * Public method to display player's version of sudoku.
     * 
     * @return sudoku version.
     */
    public ArrayList<Integer[]> display_()
    {
        ArrayList<Integer[]> f = new ArrayList<>();
        ArrayList<Integer> row = new ArrayList<>();
        Integer[] row_;
        for (int i = 0; i < this.player.length; ++i)
        {
            for (int j = 0; j < this.player[i].length; ++j)
            {
                if (this.problem[i][j] == 0 && this.player[i][j] == 0)
                {
                    row.add(0);
                }
                else if (this.problem[i][j] == 0)
                {
                    row.add(this.player[i][j]);
                }
                else
                {
                    row.add(this.player[i][j]);
                }
            }
            row_ = new Integer[row.size()];
            row_ = row.toArray(row_);
            f.add(row_);
            row_ = null;
            row.clear();
        }
        
        return f;
    }
    
    /**
     * Returns the answer.
     * 
     * @return answer.
     */
    public ArrayList<int[]> getAnswer()
    {
        ArrayList<int[]> ret = new ArrayList<>(Arrays.asList(this.answer));
        return ret;
    }
    
    /** Public method to generate a new puzzle. */
    public void generate()
    {
        int randNum;
        
        // Generates variables.
        for (int i = 0; i < this.variables.length; i++)
        {
            randNum = this.rand.nextInt(9) + 1;
            while (!checkDuplicate(this.variables, i, randNum))
            {
                randNum = this.rand.nextInt(9) + 1;
            }
            this.variables[i] = randNum;
        }
        
        // Example sudoku.
        randNum = this.rand.nextInt(3) + 1;
        switch (randNum)
        {
            case 1:
                int[][] array1 = { { 4, 7, 1, 3, 2, 8, 5, 9, 6 }, { 6, 3, 9, 5, 1, 4, 7, 8, 2 }, { 5, 2, 8, 6, 7, 9, 1, 3, 4 }, { 1, 4, 2, 9, 6, 7, 3, 5, 8 }, { 8, 9, 7, 2, 5, 3, 4, 6, 1 },
                        { 3, 6, 5, 4, 8, 1, 2, 7, 9 }, { 9, 5, 6, 1, 3, 2, 8, 4, 7 }, { 2, 8, 4, 7, 9, 5, 6, 1, 3 }, { 7, 1, 3, 8, 4, 6, 9, 2, 5 } };
                copy(array1, this.answer); // Copies example to answer.
                break;
            case 2:
                int[][] array2 = { { 8, 3, 5, 4, 1, 6, 9, 2, 7 }, { 2, 9, 6, 8, 5, 7, 4, 3, 1 }, { 4, 1, 7, 2, 9, 3, 6, 5, 8 }, { 5, 6, 9, 1, 3, 4, 7, 8, 2 }, { 1, 2, 3, 6, 7, 8, 5, 4, 9 },
                        { 7, 4, 8, 5, 2, 9, 1, 6, 3 }, { 6, 5, 2, 7, 8, 1, 3, 9, 4 }, { 9, 8, 1, 3, 4, 5, 2, 7, 6 }, { 3, 7, 4, 9, 6, 2, 8, 1, 5 } };
                copy(array2, this.answer);
                break;
            default:
            case 3:
                int[][] array3 = { { 5, 3, 4, 6, 7, 8, 9, 1, 2 }, { 6, 7, 2, 1, 9, 5, 3, 4, 8 }, { 1, 9, 8, 3, 4, 2, 5, 6, 7 }, { 8, 5, 9, 7, 6, 1, 4, 2, 3 }, { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
                        { 7, 1, 3, 9, 2, 4, 8, 5, 6 }, { 9, 6, 1, 5, 3, 7, 2, 8, 4 }, { 2, 8, 7, 4, 1, 9, 6, 3, 5 }, { 3, 4, 5, 2, 8, 6, 1, 7, 9 } };
                copy(array3, this.answer);
                break;
        }
        
        replace(this.answer); // Randomize once more.
        copy(this.answer, this.problem); // Copies answer to problem.
        genProb(); // Generates problem.
        
        shuffle();
        copy(this.problem, this.player); // Copies problem to player.
        
        // Checking for shuffled problem
        copy(this.problem, this.comp);
        
        if (!solve(0, 0, 0, this.comp))
        {
            generate();
        }
        if (!unique())
        {
            generate();
        }
    }
    
    /**
     * Public method to get problem value
     * 
     * @param row
     * @param column
     * @return problem value.
     */
    public int getProblemValue(int row, int column)
    {
        return this.problem[row][column];
    }
    
    /**
     * Public method to check if player's choice valid
     * 
     * @param row
     * @param column
     * @param value
     * @return true if the players choice is valid.
     */
    public boolean isValid(int row, int column, int value)
    {
        for (int k = 0; k < 9; k++)
        { // Check row.
            if (value == this.player[k][column])
            {
                return false;
            }
        }
        
        for (int k = 0; k < 9; k++)
        { // Check column.
            if (value == this.player[row][k])
            {
                return false;
            }
        }
        
        int boxRowOffset = (row / 3) * 3; // Set offset to determine box.
        int boxColOffset = (column / 3) * 3;
        for (int k = 0; k < 3; k++)
        { // Check box.
            for (int m = 0; m < 3; m++)
            {
                if (value == this.player[boxRowOffset + k][boxColOffset + m])
                {
                    return false;
                }
            }
        }
        
        return true; // no violations, so it's legal
    }
    
    /**
     * Public method to mutate player sudoku.
     * 
     * @param row
     * @param col
     * @param value
     */
    public void setPlayer(int row, int col, int value)
    {
        this.player[row][col] = value;
    }
    
    /**
     * Public method storePlayer; stores player's answer.
     * 
     * @param index
     * @param val
     */
    public void storePlayer(int index, int val)
    {
        this.playerArray[index] = val;
    }
    
    /**
     * Private helper method checkComp; checks if it has same solution.
     * 
     * @return true if it has same solution
     */
    private boolean checkComp()
    {
        for (int i = 0; i < this.comp.length; i++)
        {
            for (int j = 0; j < this.comp[i].length; j++)
            {
                if (this.comp[i][j] != this.answer[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Private helper method checkDuplicate; it checks if it's already repeated.
     * 
     * @param array
     * @param index
     * @param value
     * @return true if there are duplicates
     */
    private boolean checkDuplicate(int[] array, int index, int value)
    {
        for (int i = 0; i < index; i++)
        {
            if (array[i] == value)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Private helper method copy; it deep copies array.
     * 
     * @param origin
     * @param dest
     */
    private void copy(int[][] origin, int[][] dest)
    {
        for (int i = 0; i < origin.length; i++)
        {
            for (int j = 0; j < origin[i].length; j++)
            {
                dest[i][j] = origin[i][j];
            }
        }
    }
    
    /**
     * Private helper method genProb; it generates problem from answer sudoku.
     */
    private void genProb()
    {
        int row;
        int column;
        int removed1;
        int removed2;
        
        for (int i = 0; i < this.difficulty / 2;)
        {
            row = this.rand.nextInt(9);
            column = this.rand.nextInt(9);
            while (this.problem[row][column] == 0 || (row == 4 && column == 4))
            {
                row = this.rand.nextInt(9);
                column = this.rand.nextInt(9);
            }
            
            // Clearing random boxes.
            removed1 = this.problem[row][column];
            removed2 = this.problem[8 - row][8 - column];
            this.problem[row][column] = 0;
            this.problem[8 - row][8 - column] = 0;
            
            copy(this.problem, this.comp);
            
            if (!solve(0, 0, 0, this.comp))
            {
                // Case of unsolvable.
                // Putting back original values.
                this.problem[row][column] = removed1;
                this.problem[8 - row][8 - column] = removed2;
            }
            else
            {
                if (unique())
                {
                    // Case of solution is unique.
                    i++;
                }
                else
                {
                    // Case of solution is not unique.
                    // Putting back original values.
                    this.problem[row][column] = removed1;
                    this.problem[8 - row][8 - column] = removed2;
                }
            }
        }
    }
    
    /**
     * Private helper method legal; checks if the number is legal to put in.
     * 
     * @param i
     * @param j
     * @param val
     * @param cells
     * @return true if number is legal
     */
    private boolean legal(int i, int j, int val, int[][] cells)
    {
        for (int k = 0; k < 9; k++)
        {
            // Check row.
            if (val == cells[k][j])
            {
                return false;
            }
        }
        
        for (int k = 0; k < 9; k++)
        {
            // Check column.
            if (val == cells[i][k])
            {
                return false;
            }
        }
        
        int boxRowOffset = (i / 3) * 3; // Set offset to determine box.
        int boxColOffset = (j / 3) * 3;
        for (int k = 0; k < 3; k++)
        {
            // Check box.
            for (int m = 0; m < 3; m++)
            {
                if (val == cells[boxRowOffset + k][boxColOffset + m])
                {
                    return false;
                }
            }
        }
        
        return true; // no violations, so it's legal
    }
    
    /**
     * Private helper method replace; it creates random puzzle.
     * 
     * @param array
     */
    private void replace(int[][] array)
    {
        int[] temp = new int[ROW];
        
        for (int i = 0; i < ROW; i++)
        {
            temp[i] = array[0][i];
        }
        
        for (int i = 0; i < ROW; i++)
        {
            for (int j = 0; j < ROW; j++)
            {
                for (int k = 0; k < COLUMN; k++)
                {
                    if (array[j][k] == temp[i])
                    {
                        array[j][k] = this.variables[i] + 10;
                    }
                }
            }
        }
        
        for (int i = 0; i < ROW; i++)
        {
            for (int j = 0; j < COLUMN; j++)
            {
                array[i][j] -= 10;
            }
        }
    }
    
    /**
     * Private helper method shuffle; it shuffles both answer and problem.
     */
    private void shuffle()
    {
        int[] temp = new int[ROW];
        int row, col, decider, indexToSwapWith;
        
        // Shuffle as many we want.
        for (int i = 0; i < this.shuffleCount; i++)
        {
            // Decide which line to shuffle (ex. row or column).
            decider = this.rand.nextInt(2);
            
            if (decider == 0)
            { // Swaps row.
                row = this.rand.nextInt(9);
                
                if (row < 3)
                {
                    indexToSwapWith = this.rand.nextInt(3);
                    
                    // Decides row to swap with.
                    while (indexToSwapWith == row)
                    {
                        indexToSwapWith = this.rand.nextInt(3);
                    }
                    
                    // Swap
                    for (int j = 0; j < ROW; j++)
                    {
                        temp[j] = this.answer[row][j];
                        this.answer[row][j] = this.answer[indexToSwapWith][j];
                        this.answer[indexToSwapWith][j] = temp[j];
                        temp[j] = this.problem[row][j];
                        this.problem[row][j] = this.problem[indexToSwapWith][j];
                        this.problem[indexToSwapWith][j] = temp[j];
                    }
                }
                else if (row < 6)
                {
                    indexToSwapWith = this.rand.nextInt(3) + 3;
                    
                    while (indexToSwapWith == row)
                    {
                        indexToSwapWith = this.rand.nextInt(3) + 3;
                    }
                    
                    for (int j = 0; j < ROW; j++)
                    {
                        temp[j] = this.answer[row][j];
                        this.answer[row][j] = this.answer[indexToSwapWith][j];
                        this.answer[indexToSwapWith][j] = temp[j];
                        temp[j] = this.problem[row][j];
                        this.problem[row][j] = this.problem[indexToSwapWith][j];
                        this.problem[indexToSwapWith][j] = temp[j];
                    }
                }
                else
                {
                    indexToSwapWith = this.rand.nextInt(3) + 6;
                    
                    while (indexToSwapWith == row)
                    {
                        indexToSwapWith = this.rand.nextInt(3) + 6;
                    }
                    
                    for (int j = 0; j < ROW; j++)
                    {
                        temp[j] = this.answer[row][j];
                        this.answer[row][j] = this.answer[indexToSwapWith][j];
                        this.answer[indexToSwapWith][j] = temp[j];
                        temp[j] = this.problem[row][j];
                        this.problem[row][j] = this.problem[indexToSwapWith][j];
                        this.problem[indexToSwapWith][j] = temp[j];
                    }
                }
            }
            else
            {
                col = this.rand.nextInt(9);
                
                if (col < 3)
                {
                    indexToSwapWith = this.rand.nextInt(3);
                    
                    // Decides column to swap with.
                    while (indexToSwapWith == col)
                    {
                        indexToSwapWith = this.rand.nextInt(3);
                    }
                    
                    // Swap
                    for (int j = 0; j < COLUMN; j++)
                    {
                        temp[j] = this.answer[j][col];
                        this.answer[j][col] = this.answer[j][indexToSwapWith];
                        this.answer[j][indexToSwapWith] = temp[j];
                        temp[j] = this.problem[j][col];
                        this.problem[j][col] = this.problem[j][indexToSwapWith];
                        this.problem[j][indexToSwapWith] = temp[j];
                    }
                }
                else if (col < 6)
                {
                    indexToSwapWith = this.rand.nextInt(3) + 3;
                    
                    // Decides column to swap with.
                    while (indexToSwapWith == col)
                    {
                        indexToSwapWith = this.rand.nextInt(3) + 3;
                    }
                    
                    // Swap
                    for (int j = 0; j < COLUMN; j++)
                    {
                        temp[j] = this.answer[j][col];
                        this.answer[j][col] = this.answer[j][indexToSwapWith];
                        this.answer[j][indexToSwapWith] = temp[j];
                        temp[j] = this.problem[j][col];
                        this.problem[j][col] = this.problem[j][indexToSwapWith];
                        this.problem[j][indexToSwapWith] = temp[j];
                    }
                }
                else
                {
                    indexToSwapWith = this.rand.nextInt(3) + 6;
                    
                    // Decides column to swap with.
                    while (indexToSwapWith == col)
                    {
                        indexToSwapWith = this.rand.nextInt(3) + 6;
                    }
                    
                    // Swap
                    for (int j = 0; j < COLUMN; j++)
                    {
                        temp[j] = this.answer[j][col];
                        this.answer[j][col] = this.answer[j][indexToSwapWith];
                        this.answer[j][indexToSwapWith] = temp[j];
                        temp[j] = this.problem[j][col];
                        this.problem[j][col] = this.problem[j][indexToSwapWith];
                        this.problem[j][indexToSwapWith] = temp[j];
                    }
                }
            }
        }
    }
    
    /**
     * Private helper method solve; checks if it's solvable.
     * 
     * @param i2
     * @param j2
     * @param pos
     * @param cells
     * @return true if solved
     */
    private boolean solve(int i2, int j2, int pos, int[][] cells)
    {
        int i = i2;
        int j = j2;
        if (i == 9)
        {
            i = 0;
            if (++j == 9)
                return true;
        }
        if (cells[i][j] != 0) // Skip filled cells
            return solve(i + 1, j, pos, cells);
        
        for (int k = 1; k <= 9; k++)
        {
            int val = k + pos;
            if (val > 9)
            {
                val = val - 9;
            }
            if (legal(i, j, val, cells))
            {
                cells[i][j] = val;
                if (solve(i + 1, j, pos, cells))
                    return true;
            }
        }
        cells[i][j] = 0; // Reset on backtrack
        return false;
    }
    
    /**
     * Private helper method storeAns; stores blanks.
     */
    private void storeAns()
    {
        int count = 0; // Keeps track of blank number.
        
        for (int i = 0; i < 3; i++)
        { // row
            for (int j = 0; j < 3; j++)
            { // column
                for (int k = i * 3; k < i * 3 + 3; k++)
                { // Corresponding box.
                    for (int l = j * 3; l < j * 3 + 3; l++)
                    {
                        if (this.problem[k][l] == 0)
                        {
                            this.ansArray[count] = this.answer[k][l]; // fills ansArray.
                            count++;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Private helper method unique;
     * 
     * @return true if unique
     */
    private boolean unique()
    {
        for (int i = 1; i <= 9; i++)
        {
            copy(this.problem, this.comp);
            solve(0, 0, i, this.comp);
            if (!checkComp())
            {
                return false;
            }
        }
        return true;
    }
}
