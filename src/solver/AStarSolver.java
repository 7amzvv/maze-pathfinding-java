package solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import model.Cell;
import model.Grid;

public final class AStarSolver extends AbstractSolver {
    @Override
    public SearchResult solve(Grid grid) {
        grid.resetSearch();
        long startedAt = System.nanoTime();
        Cell start = grid.getStart();
        Cell end = grid.getEnd();
        List<Cell> explored = new ArrayList<>();
        Map<Cell, Integer> costs = new HashMap<>();
        Comparator<Cell> priority = Comparator
                .comparingLong((Cell cell) -> (long) costs.get(cell) + manhattan(cell, end))
                .thenComparingLong(cell -> manhattan(cell, end))
                .thenComparingInt(Cell::getRow)
                .thenComparingInt(Cell::getCol);
        PriorityQueue<Cell> frontier = new PriorityQueue<>(priority);
        costs.put(start, 0);
        frontier.add(start);

        while (!frontier.isEmpty()) {
            Cell current = frontier.remove();
            current.setVisited(true);
            explored.add(current);
            if (current == end) {
                List<Cell> path = reconstructPath(start, end);
                return new SearchResult(explored, path, System.nanoTime() - startedAt);
            }
            int nextCost = costs.get(current) + 1;
            for (Cell neighbor : grid.getNeighbors(current)) {
                Integer previousCost = costs.get(neighbor);
                if (neighbor.isVisited() || (previousCost != null && previousCost <= nextCost)) {
                    continue;
                }
                if (previousCost != null) {
                    frontier.remove(neighbor);
                }
                costs.put(neighbor, nextCost);
                neighbor.setParent(current);
                frontier.add(neighbor);
            }
        }
        return new SearchResult(explored, List.of(), System.nanoTime() - startedAt);
    }

    private static long manhattan(Cell from, Cell to) {
        return (long) Math.abs(from.getRow() - to.getRow())
                + Math.abs(from.getCol() - to.getCol());
    }
}
