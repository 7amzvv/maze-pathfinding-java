package solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import model.Cell;
import model.Grid;

public final class BFSSolver extends AbstractSolver {
    @Override
    public SearchResult solve(Grid grid) {
        grid.resetSearch();
        long startedAt = System.nanoTime();
        Cell start = grid.getStart();
        Cell end = grid.getEnd();
        List<Cell> explored = new ArrayList<>();
        Queue<Cell> frontier = new ArrayDeque<>();

        start.setVisited(true);
        frontier.add(start);
        for (Cell neighbor : grid.getNeighbors(start)) {
            neighbor.setVisited(true);
            neighbor.setParent(start);
            frontier.add(neighbor);
        }

        while (!frontier.isEmpty()) {
            Cell current = frontier.remove();
            explored.add(current);
            if (current == end) {
                List<Cell> path = reconstructPath(start, end);
                return new SearchResult(explored, path, System.nanoTime() - startedAt);
            }
        }
        return new SearchResult(explored, List.of(), System.nanoTime() - startedAt);
    }
}
