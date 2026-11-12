import model.Grid;

public final class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(5, 9);
        grid.setWall(0, 3, true);
        grid.setWall(1, 3, true);
        grid.setWall(2, 3, true);
        grid.setWall(3, 6, true);
        grid.setWall(4, 6, true);

        for (int row = 0; row < grid.getRows(); row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < grid.getCols(); col++) {
                line.append(switch (grid.getCell(row, col).getType()) {
                    case EMPTY -> '.';
                    case WALL -> '#';
                    case START -> 'S';
                    case END -> 'E';
                });
            }
            System.out.println(line);
        }
    }
}
