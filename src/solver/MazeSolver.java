package solver;

import model.Grid;

public interface MazeSolver {
    SearchResult solve(Grid grid);
}
