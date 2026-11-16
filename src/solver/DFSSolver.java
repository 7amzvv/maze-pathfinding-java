package solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import model.Cell;
import model.Grid;

public final class DFSSolver extends AbstractSolver {
    @Override
    public SearchResult solve(Grid grid) {
        grid.resetSearch();
        long startedAt = System.nanoTime();
        Cell start = grid.getStart();
        Cell end = grid.getEnd();
        List<Cell> explored = new ArrayList<>();
        Deque<Cell> frontier = new ArrayDeque<>();
        frontier.push(start);

        while (!frontier.isEmpty()) {
            Cell current = frontier.pop();
            if (current.isVisited()) {
                continue;
            }
            current.setVisited(true);
            explored.add(current);
            if (current == end) {
                List<Cell> path = reconstructPath(start, end);
                return new SearchResult(explored, path, System.nanoTime() - startedAt);
            }
            for (Cell neighbor : grid.getNeighbors(current)) {
                if (!neighbor.isVisited()) {
                    neighbor.setParent(current);
                    frontier.push(neighbor);
                }
            }
        }
        return new SearchResult(explored, List.of(), System.nanoTime() - startedAt);
    }
}
