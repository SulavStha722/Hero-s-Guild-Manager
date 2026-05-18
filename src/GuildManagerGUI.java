import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GuildManagerGUI extends JFrame {

    private ArrayList<Hero> database;
    private String filename;
    
    private JTable rosterTable;
    private DefaultTableModel tableModel;

    public GuildManagerGUI(ArrayList<Hero> database, String filename) {
        this.database = database;
        this.filename = filename;

        // --- 1. WINDOW SETUP ---
        setTitle("Hero's Guild Manager - God Mode Editor");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- 2. HEADER PANEL ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("God Mode: Global Hero Preferences");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- 3. THE DATA TABLE (Center View) ---
        String[] columns = {"ID", "Name", "Class", "Power Level", "Cost"};
        tableModel = new DefaultTableModel(columns, 0);
        rosterTable = new JTable(tableModel);
        rosterTable.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(rosterTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- 4. CONTROL PANEL (Right Side Navigation & Actions) ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(7, 1, 10, 10));
        controlPanel.setBorder(new EmptyBorder(10, 10, 20, 20));

        JButton btnAdd = new JButton("Add Hero");
        JButton btnUpdate = new JButton("Update Power");
        JButton btnDelete = new JButton("Delete Hero");
        JButton btnSort = new JButton("Sort by Power (Merge)");
        JButton btnSearch = new JButton("Search by Name (Binary)");
        JButton btnSaveLaunch = new JButton("Save & Launch Game");

        controlPanel.add(new JLabel("Database Controls:", SwingConstants.CENTER));
        controlPanel.add(btnAdd);
        controlPanel.add(btnUpdate);
        controlPanel.add(btnDelete);
        controlPanel.add(btnSort);
        controlPanel.add(btnSearch);
        controlPanel.add(btnSaveLaunch);
        
        add(controlPanel, BorderLayout.EAST);

        // --- 5. ACTION LISTENERS FOR OPERATIONS ---

        // CREATE (ADD) RECORD
        btnAdd.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog(this, "Enter Hero Name:");
                if (name == null || name.trim().isEmpty()) return;

                String[] classes = {"Warrior", "Mage", "Archer"};
                String heroClass = (String) JOptionPane.showInputDialog(this, "Select Class:", 
                        "Class Configuration", JOptionPane.QUESTION_MESSAGE, null, classes, classes[0]);
                if (heroClass == null) return;

                int power = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Power Level:"));
                int cost = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Hiring Cost:"));

                if (heroClass.equals("Warrior")) database.add(new Warrior(name, power, cost));
                else if (heroClass.equals("Mage")) database.add(new Mage(name, power, cost));
                else database.add(new Archer(name, power, cost));

                saveAndRefresh();
                JOptionPane.showMessageDialog(this, "Custom hero configured successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Power and Cost fields require valid numbers.", "Data Type Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // UPDATE RECORD
        btnUpdate.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Enter Row ID of Hero to Update:");
                if (idStr == null) return;
                int id = Integer.parseInt(idStr);

                if (id >= 0 && id < database.size()) {
                    int newPower = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter New Power Level:"));
                    database.get(id).setPowerLevel(newPower);
                    saveAndRefresh();
                    JOptionPane.showMessageDialog(this, "Hero statistics updated!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid database row index.", "Execution Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Entry formatting incorrect.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // DELETE RECORD
        btnDelete.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "Enter Row ID of Hero to Purge:");
                if (idStr == null) return;
                int id = Integer.parseInt(idStr);

                if (id >= 0 && id < database.size()) {
                    database.remove(id);
                    saveAndRefresh();
                    JOptionPane.showMessageDialog(this, "Record dropped successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid database index.", "Execution Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Entry formatting incorrect.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // SORT DATA (Merge Sort Algorithm)
        btnSort.addActionListener(e -> {
            RosterAlgorithms.sortDatabaseByPower(database);
            saveAndRefresh();
            JOptionPane.showMessageDialog(this, "Global registry sorted via O(n log n) Merge Sort!");
        });

        // QUERY DATA (Binary Search Algorithm)
        btnSearch.addActionListener(e -> {
            String target = JOptionPane.showInputDialog(this, "Search preference settings (Exact Match Case-Insensitive):");
            if (target != null && !target.trim().isEmpty()) {
                
                Hero found = RosterAlgorithms.searchHeroByName(database, target);
                
                if (found != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Data Node Located!\n\nName: " + found.getName() + 
                        "\nClass: " + found.getHeroClass() + 
                        "\nPower: " + found.getPowerLevel() + 
                        "\nCost: " + found.getHiringCost(), 
                        "O(log n) Binary Search Output", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Hero identity '" + target + "' not recognized in system preferences.", "Data Missing", JOptionPane.WARNING_MESSAGE);
                }
                refreshTable();
            }
        });

        // OPTION B: SAVE RUNTIME PREFERENCES & TRANSFER CONTROLS TO CAMPAIGN LOOP
        btnSaveLaunch.addActionListener(e -> {
            TextMain.saveHeroes(database, filename);
            
            String guildName = JOptionPane.showInputDialog(this, "Give Your Legendary Guild a Name:", "New Campaign Setup", JOptionPane.PLAIN_MESSAGE);
            if (guildName == null || guildName.trim().isEmpty()) {
                guildName = "Shadowfang Vanguard";
            }

            this.dispose(); // Gracefully terminate settings pane

            String finalGuildName = guildName;
            SwingUtilities.invokeLater(() -> {
                GameGUI gameWindow = new GameGUI(finalGuildName, database);
                gameWindow.setLocationRelativeTo(null);
                gameWindow.setVisible(true);
            });
        });

        // Initialize table structure
        refreshTable();
    }

    private void saveAndRefresh() {
        TextMain.saveHeroes(database, filename);
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < database.size(); i++) {
            Hero h = database.get(i);
            Object[] rowData = {i, h.getName(), h.getHeroClass(), h.getPowerLevel(), h.getHiringCost()};
            tableModel.addRow(rowData);
        }
    }
}