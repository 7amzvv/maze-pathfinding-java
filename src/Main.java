import model.Grid;
import model.MazeGenerator;

public final class Main {
    public static void main(String[] args) {
        Grid grid = new MazeGenerator().generate(15, 31);

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
