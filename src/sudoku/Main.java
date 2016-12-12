package sudoku;

/**
 *
 * @author yatloutou
 */
public class Main {

    public static void main(String[] args) {
        String path = "puzzels\\45.txt";
        SudokuSolver.solveLines(path,100);
    }

}
