import java.util.HashSet;
import java.util.List;
import model.Cell;
import model.Grid;
import solver.AStarSolver;
import solver.BFSSolver;
import solver.DFSSolver;
import solver.MazeSolver;
import solver.SearchResult;

public final class SolverTest {
    private static final List<MazeSolver> SOLVERS =
            List.of(new BFSSolver(), new DFSSolver(), new AStarSolver());

    public static void main(String[] args) {
        findsPathBeyondFirstLayer();
        reportsBlockedMazeAndRecovers();
        findsShortestPathAroundWall();
        System.out.println("3 tests passed (BFS, DFS and A*).");
    }

    private static void findsPathBeyondFirstLayer() {
        Grid grid = new Grid(1, 3);
        for (MazeSolver solver : SOLVERS) {
            SearchResult result = solver.solve(grid);
            check(result.path().equals(List.of(grid.getStart(), grid.getCell(0, 1), grid.getEnd())),
                    "Expected the exact path through S.E");
            check(result.pathLength() == 2 && result.visitedCount() == 3, "Unexpected metrics for S.E");
        }
    }

    private static void reportsBlockedMazeAndRecovers() {
        Grid grid = new Grid(1, 3);
        for (MazeSolver solver : SOLVERS) {
            check(solver.solve(grid).found(), "The initial passage should be open");
            grid.setWall(0, 1, true);
            SearchResult blocked = solver.solve(grid);
            check(!blocked.found() && blocked.pathLength() == -1, "S#E should be blocked");
            check(blocked.explored().equals(List.of(grid.getStart())), "Only the start is reachable");
            grid.setWall(0, 1, false);
            check(solver.solve(grid).pathLength() == 2, "The reopened passage should be solvable");
        }
    }

    private static void findsShortestPathAroundWall() {
        Grid grid = new Grid(3, 5);
        grid.moveEnd(0, 4);
        grid.setWall(0, 2, true);
        grid.setWall(1, 2, true);
        for (MazeSolver solver : SOLVERS) {
            SearchResult result = solver.solve(grid);
            List<Cell> path = result.path();
            check(result.found() && path.get(0) == grid.getStart()
                    && path.get(path.size() - 1) == grid.getEnd(), "Expected a path around the wall");
            check(new HashSet<>(path).size() == path.size(), "The path must be simple");
            for (int i = 1; i < path.size(); i++) {
                check(grid.getNeighbors(path.get(i - 1)).contains(path.get(i)), "Invalid movement");
            }
            if (!(solver instanceof DFSSolver)) {
                check(result.pathLength() == 8, "BFS and A* must find the eight-move path");
            }
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
