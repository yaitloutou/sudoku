package sudoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author yaitloutou
 */
public class SudokuSolver {

    static final int s = 3;
    static final int S = s * s;
    static final int SS = S * S;
    static ArrayList<Integer> grid = new ArrayList<>();
    private static int deepth = 0;
    
    public static void solveLines(String path){
        solveLines(path, 0);
    }
    
    public static void solveLines(String path, int limit) {
        int l = 0;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            for (String line : (Iterable<String>) lines::iterator) {
                if (limit>0 && l == limit) break;
                
                System.out.print(++l + "\t");
                System.out.print(line);
                System.out.println();

                if(!solveLine(line)) break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean solveLine(String line) {
        boolean isSolved = false;              
        ArrayList<Integer> g = sudokuStringToArrayList(line);

        if (!SudokuSolver.isValid(g)) 
            System.err.println("Not a valid grid");
        else {
//            Sudoku.printGrid(g);
            isSolved = SudokuSolver.solve(g);
            System.out.print(">>\t");
            Sudoku.printLine(g);
//            Sudoku.printGrid(g);
//            System.out.print("Solved ");
//            System.out.println(SudokuSolver.isValid(g));
        }
        System.out.println();
        return isSolved;
    }

    public static boolean solve(ArrayList<Integer> g) {
        deepth++;

//        for (int i = 0; i < deepth; i++) System.out.print("# ");        
//        System.out.println("    " + deepth + ": ");

        if (!isValid(g)) return false;
        
        // initialaize cells options
        ArrayList<ArrayList<Integer>> options = new ArrayList<>();
        for (int k = 0; k < g.size(); k++) {
            ArrayList<Integer> cellOptions = new ArrayList<>();
            if (g.get(k) != 0)
                cellOptions.add(g.get(k));
            
            options.add(cellOptions);
        }

        // initialaize cells excluded options
        ArrayList<ArrayList<Integer>> excludedOptions = new ArrayList<>();
        for (int k = 0; k < g.size(); k++) {
            ArrayList<Integer> cellExcludedOptions = new ArrayList<>();
            excludedOptions.add(cellExcludedOptions);
        }

        // gather options
        for (int k = 0; k < SS; k++) {
            if (g.get(k) == 0) {
                ArrayList<Integer> validCellOptions = new ArrayList<>();

                for (int n = 1; n <= S; n++)
                    if (SudokuSolver.isValid(n, k, g))
                        if (!excludedOptions.get(k).contains(n))
                            validCellOptions.add(n);

                if (validCellOptions.isEmpty())
                    return false;
                else
                    options.set(k, validCellOptions);                
            }
        }
//            Sudoku.printOptions(options,g);
//            Sudoku.printOptions(options);
        // 0 opt & 1 opt & max opts
        int opts_max = 0;
        for (int k = 0; k < SS; k++) {
            int opts_number = options.get(k).size();
//            if (opts_number ==1) {
//                int n = options.get(k).get(0);
//                if (SudokuSolver1.isValid(n, k, g)) 
//                    g.set(k, n);
//            }
            if (opts_max < opts_number) {
                opts_max = opts_number;
            }
        }

        // solve
        OpGroup_LOOP:
        for (int opts_size = 1; opts_size <= opts_max; opts_size++) {
            Cells_LOOP:
            for (int k = 0; k < SS; k++) {
                if (g.get(k) == 0 && options.get(k).size() == opts_size) {
//                    System.out.println("["+k/SIZE+", "+k%SIZE+"] >\t"+options.get(k)+" ");
                    Options_LOOP:
                    for (int o = 0; o < opts_size; o++) {
                        int n = options.get(k).get(o);

                        if (SudokuSolver.isValid(n, k, g)) {
//                            System.out.println("["+k/SIZE+", "+k%SIZE+"] > "+options.get(k)+"  opt "+o+":\t"+n+", ");
                            if (opts_size == 1) {
                                g.set(k, n);
                                break Options_LOOP;
                            }

                            ArrayList<Integer> _g = new ArrayList<>();
                            copyIn(g, _g);
                            _g.set(k, n);

                            if (solve(_g)) {
                                copyIn(_g, g);
//                                    System.out.println("** Solved **");
                                deepth = 0;
                                return true;
                            } else {
                                deepth--;
//                                    System.out.println("exclud "+n+" from ["+k/SIZE+", "+k%SIZE+"]");
                                excludedOptions.get(k).add(n);
                                if (excludedOptions.get(k).size() == options.get(k).size()) {
//                                        System.out.println("No Op");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        // Sudoku.printGrid(grid);
        return !g.contains(0);
    }
    
    static boolean isValid(int value, int position, ArrayList<Integer> grid) {

        // cell constaint
        if (grid.get(position) != 0)
            if (grid.get(position) != value)
                return false;

        // row constraint
        for (int j = 0; j < S; j++)
            if (position != S * (position / S) + j
                    && grid.get(S * (position / S) + j) == value)
                return false;

        // col constraint
        for (int i = 0; i < S; i++)
            if (position != S * i + (position % S)
                    && grid.get(S * i + (position % S)) == value)
                return false;

        // box constraint
        for (int i = ((position / S) / s) * s; i < ((position / S) / s) * s + s; i++)
            for (int j = ((position % S) / s) * s; j < ((position % S) / s) * s + s; j++)
                if (position != S * i + j
                        && grid.get(S * i + j) == value)
                    return false;

        return true;
    }

    static boolean isValid(ArrayList<Integer> grid) {
        if (grid.size() != S * S) {
            return false;
        }
        ArrayList<Integer> validations = new ArrayList<>();

        for (int k = 0; k < grid.size(); k++) {
            int v = grid.get(k) == 0 ? 1 : SudokuSolver.isValid(grid.get(k), k, grid) ? 1 : 0;
            validations.add(v);
        }

        boolean check = !validations.contains(0);
        if (!check) {
            Sudoku.printGrid(validations);
        }
        return check;
    }

    static boolean isFilled(ArrayList<Integer> grid) {
        return !grid.contains(0);
    }

    static boolean copyIn(ArrayList<Integer> src, ArrayList<Integer> dist) {
        dist.clear();
        for (Integer s : src) 
            dist.add(s);        
        return src.equals(dist);
    }

    static ArrayList<Integer> sudokuStringToArrayList(String line) {
        ArrayList<Integer> g = new ArrayList<>();
        
        char[] gChars = line.toCharArray();
        for (int i = 0; i < gChars.length; i++) 
            g.add(gChars[i] - '0');
        
        return g;
    }

}
