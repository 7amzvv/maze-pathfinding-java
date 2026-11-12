package model;

public final class Cell {
    private final int row;
    private final int col;
    private CellType type = CellType.EMPTY;
    private boolean visited;
    private Cell parent;

    Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellType getType() {
        return type;
    }

    void setType(CellType type) {
        this.type = type;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }
}
