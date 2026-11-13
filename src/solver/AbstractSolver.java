package solver;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import model.Cell;

public abstract class AbstractSolver implements MazeSolver {
    protected final List<Cell> reconstructPath(Cell start, Cell end) {
        Deque<Cell> path = new ArrayDeque<>();
        for (Cell current = end; current != null; current = current.getParent()) {
            path.addFirst(current);
            if (current == start) {
                return List.copyOf(path);
            }
        }
        return List.of();
    }
}
