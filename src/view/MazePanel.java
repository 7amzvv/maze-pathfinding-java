package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.Cell;
import model.Grid;

public final class MazePanel extends JPanel {
    private static final Color BACKGROUND = new Color(0x111827);
    private static final Color WALL = new Color(0x243249);
    private static final Color EMPTY = new Color(0xE5EDF5);
    private static final Color EXPLORED = new Color(0x9DC4E9);
    private static final Color PATH = new Color(0xF5C76A);
    private static final Color START = new Color(0x16735F);
    private static final Color END = new Color(0x9C3659);

    private Grid grid;
    private final Set<Cell> explored = new HashSet<>();
    private final Set<Cell> path = new HashSet<>();

    public MazePanel(Grid grid, Consumer<Cell> onCellClick) {
        this.grid = grid;
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(860, 430));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    Cell cell = cellAt(event.getX(), event.getY());
                    if (cell != null) {
                        onCellClick.accept(cell);
                    }
                }
            }
        });
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
        clearResult();
    }

    public void clearResult() {
        path.clear();
        explored.clear();
        repaint();
    }

    public void showExplored(Cell cell) {
        explored.add(cell);
        repaint();
    }

    public void showPath(Collection<Cell> cells) {
        path.addAll(cells);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        int size = cellSize();
        if (size < 1) {
            return;
        }
        int left = gridLeft(size);
        int top = gridTop(size);
        Graphics2D canvas = (Graphics2D) graphics.create();
        try {
            canvas.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            canvas.setFont(getFont().deriveFont(Font.BOLD, Math.max(9f, size * 0.58f)));
            for (int row = 0; row < grid.getRows(); row++) {
                for (int col = 0; col < grid.getCols(); col++) {
                    paintCell(canvas, grid.getCell(row, col), left + col * size, top + row * size, size);
                }
            }
        } finally {
            canvas.dispose();
        }
    }

    private int cellSize() {
        return Math.min((getWidth() - 24) / grid.getCols(), (getHeight() - 24) / grid.getRows());
    }

    private int gridLeft(int size) {
        return (getWidth() - size * grid.getCols()) / 2;
    }

    private int gridTop(int size) {
        return (getHeight() - size * grid.getRows()) / 2;
    }

    private Cell cellAt(int x, int y) {
        int size = cellSize();
        int localX = x - gridLeft(size);
        int localY = y - gridTop(size);
        if (size < 1 || localX < 0 || localY < 0) {
            return null;
        }
        int row = localY / size;
        int col = localX / size;
        return grid.isInBounds(row, col) ? grid.getCell(row, col) : null;
    }

    private void paintCell(Graphics2D canvas, Cell cell, int x, int y, int size) {
        canvas.setColor(switch (cell.getType()) {
            case WALL -> WALL;
            case START -> START;
            case END -> END;
            case EMPTY -> path.contains(cell) ? PATH : explored.contains(cell) ? EXPLORED : EMPTY;
        });
        int gap = size >= 5 ? 1 : 0;
        canvas.fillRect(x, y, size - gap, size - gap);

        String marker = switch (cell.getType()) {
            case START -> "S";
            case END -> "E";
            default -> "";
        };
        if (size >= 12 && !marker.isEmpty()) {
            FontMetrics metrics = canvas.getFontMetrics();
            canvas.setColor(Color.WHITE);
            canvas.drawString(marker, x + (size - metrics.stringWidth(marker)) / 2,
                    y + (size - metrics.getHeight()) / 2 + metrics.getAscent());
        }
    }
}
