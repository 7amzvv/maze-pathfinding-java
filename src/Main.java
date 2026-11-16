import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import javax.swing.SwingUtilities;
import model.Grid;
import model.MazeGenerator;
import solver.AStarSolver;
import solver.BFSSolver;
import solver.DFSSolver;
import solver.SearchResult;
import view.MainFrame;

public final class Main {
    public static void main(String[] args) {
        var options = Arrays.asList(args);
        if (options.contains("--console")) {
            runConsoleComparison();
        } else {
            SwingUtilities.invokeLater(() -> new MainFrame(options.contains("--open-grid")).setVisible(true));
        }
    }

    private static void runConsoleComparison() {
        Grid maze = new MazeGenerator(new Random(42)).generate(15, 31);
        printGrid(maze);
        compare("Labyrinthe 15 x 31 (graine 42)", maze);
        compare("Grille ouverte 15 x 31", new Grid(15, 31));
    }

    private static void printGrid(Grid grid) {
        for (int row = 0; row < grid.getRows(); row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < grid.getCols(); col++) {
                line.append(switch (grid.getCell(row, col).getType()) {
                    case EMPTY -> '.';
                    case WALL -> '#';
                    case START -> 'S';
                    case END -> 'E';
                });
            }
            System.out.println(line);
        }
    }

    private static void compare(String title, Grid grid) {
        System.out.println();
        System.out.println(title);
        System.out.printf("%-6s %9s %10s %12s%n", "Algo", "Explorees", "Longueur", "Temps (ms)");
        printResult("BFS", new BFSSolver().solve(grid));
        printResult("DFS", new DFSSolver().solve(grid));
        printResult("A*", new AStarSolver().solve(grid));
    }

    private static void printResult(String name, SearchResult result) {
        String length = result.found() ? Integer.toString(result.pathLength()) : "-";
        System.out.printf(Locale.ROOT, "%-6s %9d %10s %12.3f%n",
                name, result.visitedCount(), length, result.elapsedNanos() / 1_000_000.0);
    }
}
