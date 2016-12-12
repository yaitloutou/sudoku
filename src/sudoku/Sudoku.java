/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;

/**
 *
 * @author yaitloutou
 */
public class Sudoku { 
    
    private static int s = 3; // boxes' dimention
    private static ArrayList<Integer> grid;
    
    // Print Grid
    public static void printGrid() {
        System.out.println();
        final int S = s * s;
        for (int i = 0; i < S; i++) {
            if (i % s == 0) {
                if (i == 0) System.out.print("+-");
                else System.out.print("|-");
                for (int j = 0; j < s * s; j++) {
                    if (j % s == 0 && j % (s * s) != 0)
                        if (i == 0) System.out.print("--");
                        else System.out.print("+-");
                    System.out.print("---");
                }
                if (i == 0) System.out.println("+");
                else System.out.println("|");
            }
            for (int j = 0; j < S; j++) {
                if (j % s == 0) System.out.print("| ");
                if (grid.get(S * i + j) <= S) System.out.print(" ");
                System.out.print(grid.get(S * i + j) + " ");
            }
            System.out.println("|");
        }
        System.out.print("+-");
        for (int j = 0; j < s * s; j++) {

            if (j % s == 0 && j % (s * s) != 0)
                System.out.print("--");
            System.out.print("---");
        }
        System.out.println("+");
    }

    public static void printGrid(ArrayList grid) {
        setGrid(grid);
        printGrid();
    }

    public static void printOptions(ArrayList<ArrayList<Integer>> options) {
        int M = 0;
        for (int i = 0; i < options.size(); i++)
            if (options.get(i).size() >= M) M = options.get(i).size();
//        System.out.println("Max number of opts: "+M);

        System.out.println();
        final int S = s * s;
        for (int i = 0; i < S; i++) {
            if (i % s == 0) System.out.println();
            for (int j = 0; j < S; j++) {
                ArrayList opts = options.get(S * i + j);
                if (j != 0 && j % s == 0) System.out.print("\t\t\t");
                System.out.print(opts + "\t");
            }
            System.out.println();
        }
        System.out.println();

    }

    public static void printOptions(ArrayList<ArrayList<Integer>> options, ArrayList<Integer> g) {
        int M = 0;
        for (int i = 0; i < options.size(); i++)
            if (options.get(i).size() >= M) M = options.get(i).size();
//        System.out.println("Max number of opts: "+M);

        System.out.println();
        final int S = s * s;
        for (int i = 0; i < S; i++) {
            if (i % s == 0) System.out.println();
            for (int j = 0; j < S; j++) {
                int k = S * i + j;
                ArrayList opts = options.get(k);
                if (j != 0 && j % s == 0) System.out.print("\t\t\t");
                if(g.get(k)==0) System.out.print(opts + "\t");
                else System.out.print("OK \t");
            }
            System.out.println();
        }
        System.out.println();

    }
    
    public static void printLine(ArrayList<Integer> grid){
        setGrid(grid);
        for (Integer n : grid) System.out.print(n);
        System.out.println();        
    }

    //** Getters and Setters  **************************************************
    public static int getS() {
        return s;
    }

    public static void setS(int s) {
        Sudoku.s = s;
    }
    
    public static void setGrid(ArrayList grid) {
        Sudoku.grid = grid;
    }

    public static ArrayList<Integer> getGrid() {
        return grid;
    }


}
