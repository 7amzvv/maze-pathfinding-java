package model;

import java.util.ArrayList;
import java.util.List;

public final class Grid {
    private final Cell[][] cells;
    private Cell start;
    private Cell end;

    public Grid(int rows, int cols) {
        if (rows < 1 || cols < 1 || (rows == 1 && cols == 1)) {
            throw new IllegalArgumentException("A grid needs at least two cells.");
        }
        cells = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        start = cells[0][0];
        end = cells[rows - 1][cols - 1];
        start.setType(CellType.START);
        end.setType(CellType.END);
    }

    public int getRows() {
        return cells.length;
    }

    public int getCols() {
        return cells[0].length;
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < getRows() && col >= 0 && col < getCols();
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public Cell getStart() {
        return start;
    }

    public Cell getEnd() {
        return end;
    }

    public boolean setWall(int row, int col, boolean wall) {
        Cell cell = getCell(row, col);
        if (cell == start || cell == end) {
            return false;
        }
        cell.setType(wall ? CellType.WALL : CellType.EMPTY);
        return true;
    }

    public boolean moveStart(int row, int col) {
        Cell target = getCell(row, col);
        if (target.getType() != CellType.EMPTY) {
            return false;
        }
        start.setType(CellType.EMPTY);
        target.setType(CellType.START);
        start = target;
        return true;
    }

    public boolean moveEnd(int row, int col) {
        Cell target = getCell(row, col);
        if (target.getType() != CellType.EMPTY) {
            return false;
        }
        end.setType(CellType.EMPTY);
        target.setType(CellType.END);
        end = target;
        return true;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>(4);
        if (cell.getType() == CellType.WALL) {
            return neighbors;
        }
        int row = cell.getRow();
        int col = cell.getCol();
        addAccessible(neighbors, row - 1, col);
        addAccessible(neighbors, row, col + 1);
        addAccessible(neighbors, row + 1, col);
        addAccessible(neighbors, row, col - 1);
        return neighbors;
    }

    public void resetSearch() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setVisited(false);
                cell.setParent(null);
            }
        }
    }

    private void addAccessible(List<Cell> neighbors, int row, int col) {
        if (isInBounds(row, col) && cells[row][col].getType() != CellType.WALL) {
            neighbors.add(cells[row][col]);
        }
    }
}
