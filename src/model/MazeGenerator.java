package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public final class MazeGenerator {
    private static final int[][] STEPS = {{-2, 0}, {0, 2}, {2, 0}, {0, -2}};
    private final RandomGenerator random;

    public MazeGenerator() {
        this(RandomGenerator.getDefault());
    }

    public MazeGenerator(RandomGenerator random) {
        this.random = Objects.requireNonNull(random);
    }

    public Grid generate(int rows, int cols) {
        if (rows < 3 || cols < 3) {
            throw new IllegalArgumentException("Maze dimensions must be at least 3 by 3.");
        }

        Grid grid = new Grid(rows, cols);
        grid.moveEnd((rows - 1) / 2 * 2, (cols - 1) / 2 * 2);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid.setWall(row, col, true);
            }
        }

        boolean[][] carved = new boolean[rows][cols];
        Deque<Cell> stack = new ArrayDeque<>();
        List<Cell> candidates = new ArrayList<>(STEPS.length);
        stack.push(grid.getStart());
        carved[0][0] = true;

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            candidates.clear();
            for (int[] step : STEPS) {
                int row = current.getRow() + step[0];
                int col = current.getCol() + step[1];
                if (grid.isInBounds(row, col) && !carved[row][col]) {
                    candidates.add(grid.getCell(row, col));
                }
            }
            if (candidates.isEmpty()) {
                stack.pop();
                continue;
            }

            Cell next = candidates.get(random.nextInt(candidates.size()));
            int row = next.getRow();
            int col = next.getCol();
            grid.setWall(current.getRow() + (row - current.getRow()) / 2,
                    current.getCol() + (col - current.getCol()) / 2, false);
            grid.setWall(row, col, false);
            carved[row][col] = true;
            stack.push(next);
        }
        return grid;
    }
}
