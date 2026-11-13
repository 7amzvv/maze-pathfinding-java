package solver;

import java.util.List;
import model.Cell;

public record SearchResult(List<Cell> explored, List<Cell> path, long elapsedNanos) {
    public SearchResult {
        explored = List.copyOf(explored);
        path = List.copyOf(path);
    }

    public boolean found() {
        return !path.isEmpty();
    }

    public int visitedCount() {
        return explored.size();
    }

    public int pathLength() {
        return found() ? path.size() - 1 : -1;
    }
}
