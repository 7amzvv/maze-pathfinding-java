package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

    private final MazeGenerator generator = new MazeGenerator();
    private final JComboBox<String> algorithms = new JComboBox<>(new String[]{"BFS", "DFS", "A*"});
    private final JLabel status = new JLabel("Labyrinthe prêt à résoudre");
    private Grid grid = generator.generate(ROWS, COLS);
    private final MazePanel mazePanel = new MazePanel(grid);

    public MainFrame() {
        super("Solveur de labyrinthes");
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

        controls.add(title);
        controls.add(algorithmLabel);
        controls.add(algorithms);
        controls.add(generate);
        controls.add(solve);
        controls.add(clear);
        add(controls, BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(16, 0));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        footer.add(status, BorderLayout.CENTER);
        footer.add(new JLabel("S Départ   ·   E Arrivée   ·   Bleu Exploration   ·   Or Chemin"),
                BorderLayout.LINE_END);
        add(footer, BorderLayout.SOUTH);

        generate.addActionListener(event -> {
            grid = generator.generate(ROWS, COLS);
            mazePanel.setGrid(grid);
            status.setText("Nouveau labyrinthe");
        });
        solve.addActionListener(event -> solveMaze());
        clear.addActionListener(event -> clearResult("Résultat effacé"));
        algorithms.addActionListener(event -> clearResult("Algorithme sélectionné"));
        getRootPane().setDefaultButton(solve);

        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    private void solveMaze() {
        MazeSolver solver = switch (algorithms.getSelectedIndex()) {
            case 1 -> new DFSSolver();
            case 2 -> new AStarSolver();
            default -> new BFSSolver();
        };
        SearchResult result = solver.solve(grid);
        mazePanel.showResult(result);
        status.setText(result.found() ? "Chemin trouvé" : "Aucun chemin");
    }

    private void clearResult(String message) {
        grid.resetSearch();
        mazePanel.clearResult();
        status.setText(message);
    }
}
