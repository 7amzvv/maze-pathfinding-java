package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Cell;
import model.CellType;
import model.Grid;
import model.MazeGenerator;
import solver.AStarSolver;
import solver.BFSSolver;
import solver.DFSSolver;
import solver.MazeSolver;
import solver.SearchResult;

public final class MainFrame extends JFrame {
    private static final int ROWS = 15;
    private static final int COLS = 31;
    private static final int STEP_DELAY_MS = 16;

    private final MazeGenerator generator = new MazeGenerator();
    private final JComboBox<String> algorithms = new JComboBox<>(new String[]{"BFS", "DFS", "A*"});
    private final JComboBox<String> editModes = new JComboBox<>(
            new String[]{"Mur", "Déplacer S", "Déplacer E"});
    private final JLabel status = new JLabel("Grille prête à résoudre");
    private Grid grid;
    private final MazePanel mazePanel;
    private final Timer animation = new Timer(STEP_DELAY_MS, event -> advanceAnimation());
    private SearchResult pendingResult;
    private int nextCell;

    public MainFrame() {
        this(false);
    }

    public MainFrame(boolean openGrid) {
        super("Solveur de labyrinthes");
        grid = openGrid ? new Grid(ROWS, COLS) : generator.generate(ROWS, COLS);
        mazePanel = new MazePanel(grid, this::editCell);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEADING, 12, 10));
        JLabel title = new JLabel("Labyrinthe");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        JLabel algorithmLabel = new JLabel("Algorithme");
        algorithmLabel.setLabelFor(algorithms);
        JButton generate = new JButton("Générer");
        JButton solve = new JButton("Résoudre");
        JButton clear = new JButton("Effacer");
        JLabel editLabel = new JLabel("Édition");
        editLabel.setLabelFor(editModes);

        controls.add(title);
        controls.add(algorithmLabel);
        controls.add(algorithms);
        controls.add(generate);
        controls.add(solve);
        controls.add(clear);
        controls.add(editLabel);
        controls.add(editModes);
        add(controls, BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(16, 0));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        footer.add(status, BorderLayout.CENTER);
        footer.add(new JLabel("S Départ   ·   E Arrivée   ·   Bleu Exploration   ·   Or Chemin"),
                BorderLayout.LINE_END);
        add(footer, BorderLayout.SOUTH);

        generate.addActionListener(event -> {
            cancelAnimation();
            grid = generator.generate(ROWS, COLS);
            mazePanel.setGrid(grid);
            status.setText("Nouveau labyrinthe");
        });
        solve.addActionListener(event -> solveMaze());
        clear.addActionListener(event -> clearResult("Résultat effacé"));
        algorithms.addActionListener(event -> clearResult("Algorithme sélectionné"));
        getRootPane().setDefaultButton(solve);

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void solveMaze() {
        cancelAnimation();
        mazePanel.clearResult();
        MazeSolver solver = switch (algorithms.getSelectedIndex()) {
            case 1 -> new DFSSolver();
            case 2 -> new AStarSolver();
            default -> new BFSSolver();
        };
        pendingResult = solver.solve(grid);
        nextCell = 0;
        status.setText("Exploration…");
        animation.start();
    }

    private void advanceAnimation() {
        SearchResult result = pendingResult;
        if (result == null) {
            return;
        }
        if (nextCell < result.explored().size()) {
            mazePanel.showExplored(result.explored().get(nextCell++));
            return;
        }
        cancelAnimation();
        mazePanel.showPath(result.path());
        String outcome = result.found() ? "Chemin trouvé" : "Aucun chemin";
        String length = result.found() ? Integer.toString(result.pathLength()) : "—";
        status.setText(String.format(Locale.FRANCE, "%s  ·  Visitées %d  ·  Longueur %s  ·  Calcul %.3f ms",
                outcome, result.visitedCount(), length, result.elapsedNanos() / 1_000_000.0));
    }

    private void cancelAnimation() {
        animation.stop();
        pendingResult = null;
    }

    private void editCell(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        boolean edited = switch (editModes.getSelectedIndex()) {
            case 1 -> grid.moveStart(row, col);
            case 2 -> grid.moveEnd(row, col);
            default -> grid.setWall(row, col, cell.getType() != CellType.WALL);
        };
        if (edited) {
            clearResult("Grille modifiée");
        }
    }

    private void clearResult(String message) {
        cancelAnimation();
        grid.resetSearch();
        mazePanel.clearResult();
        status.setText(message);
    }
}
