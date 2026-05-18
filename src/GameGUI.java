import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameGUI extends JFrame {

    // Core Game State
    private Guild playerGuild;
    private ArrayList<Guild> rivals;
    private Tavern tavern;
    private WorldBoard worldBoard;
    private int turnCounter = 1;
    private ArrayList<Hero> customDatabase;
    private ArrayList<Quest> dailyQuests;
    private boolean demonKingAppeared;

    // UI Components
    private JPanel displayPanel;
    private CardLayout cardLayout;
    
    // Screens & Real-time Labels
    private JTextArea homeText;
    private JTextArea rankingText;
    private DefaultListModel<String> tavernListModel;
    private JLabel tavernGoldLabel; // Real-time gold tracker for Tavern
    private DefaultListModel<String> questListModel;
    private JLabel turnLabel;

    public GameGUI(String guildName, ArrayList<Hero> customDatabase) {
        this.customDatabase = customDatabase;
        
        // Initialize Game Data (All starting at 0 Reputation)
        playerGuild = new Guild(guildName, 500);
        rivals = new ArrayList<>();
        rivals.add(new Guild("The Iron Vanguard", 500));
        rivals.add(new Guild("Shadow Syndicate", 500));
        rivals.add(new Guild("Crimson Blades", 500));
        rivals.add(new Guild("Arcane Order", 500));
        
        for (Guild rival : rivals) {
            rival.setReputation(0);
        }
        
        tavern = new Tavern();
        worldBoard = new WorldBoard();

        // Setup Window
        setTitle("Hero's Guild Manager - Campaign");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- TOP HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.DARK_GRAY);
        turnLabel = new JLabel("Turn 1 of 5 | Guild: " + playerGuild.getGuildName());
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnLabel.setForeground(Color.WHITE);
        headerPanel.add(turnLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER: THE DISPLAY AREA (Left Side Layout) ---
        cardLayout = new CardLayout();
        displayPanel = new JPanel(cardLayout);
        displayPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Card 1: Home Screen
        homeText = new JTextArea();
        homeText.setEditable(false);
        homeText.setFont(new Font("Monospaced", Font.PLAIN, 16));
        displayPanel.add(new JScrollPane(homeText), "HOME");

        // Card 2: Recruit Hero Screen (With Live Gold Counter)
        JPanel tavernPanel = new JPanel(new BorderLayout());
        JPanel tavernHeader = new JPanel(new BorderLayout());
        tavernHeader.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel tavernTitle = new JLabel("Available Recruits");
        tavernTitle.setFont(new Font("Arial", Font.BOLD, 16));
        tavernGoldLabel = new JLabel("Current Gold: 500");
        tavernGoldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tavernGoldLabel.setForeground(new Color(0, 128, 0));
        
        tavernHeader.add(tavernTitle, BorderLayout.WEST);
        tavernHeader.add(tavernGoldLabel, BorderLayout.EAST);
        
        tavernListModel = new DefaultListModel<>();
        JList<String> tavernList = new JList<>(tavernListModel);
        tavernList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JButton btnBuyHero = new JButton("Hire Selected Hero");
        
        tavernPanel.add(tavernHeader, BorderLayout.NORTH);
        tavernPanel.add(new JScrollPane(tavernList), BorderLayout.CENTER);
        tavernPanel.add(btnBuyHero, BorderLayout.SOUTH);
        displayPanel.add(tavernPanel, "TAVERN");

        // Card 3: Quest Board Screen
        JPanel questPanel = new JPanel(new BorderLayout());
        questListModel = new DefaultListModel<>();
        JList<String> questList = new JList<>(questListModel);
        questList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JButton btnAttemptQuest = new JButton("Attempt Selected Quest");
        questPanel.add(new JScrollPane(questList), BorderLayout.CENTER);
        questPanel.add(btnAttemptQuest, BorderLayout.SOUTH);
        displayPanel.add(questPanel, "QUEST");

        // Card 4: Rankings Screen
        rankingText = new JTextArea();
        rankingText.setEditable(false);
        rankingText.setFont(new Font("Monospaced", Font.PLAIN, 16));
        displayPanel.add(new JScrollPane(rankingText), "RANKINGS");

        add(displayPanel, BorderLayout.CENTER);

        // --- RIGHT SIDE: NAVIGATION CONTROLS ---
        JPanel controlPanel = new JPanel(new GridLayout(6, 1, 10, 15));
        controlPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        controlPanel.setPreferredSize(new Dimension(200, 0));

        JButton btnHome = new JButton("Home / Status");
        JButton btnVisitTavern = new JButton("Recruit Hero");
        JButton btnViewQuests = new JButton("View Quest Board");
        JButton btnRankings = new JButton("Guild Rankings");
        JButton btnEndTurn = new JButton("End Turn");
        JButton btnRetire = new JButton("Retire (Quit)");

        controlPanel.add(btnHome);
        controlPanel.add(btnVisitTavern);
        controlPanel.add(btnViewQuests);
        controlPanel.add(btnRankings);
        controlPanel.add(btnEndTurn);
        controlPanel.add(btnRetire);
        add(controlPanel, BorderLayout.EAST);

        // --- NAVIGATION EVENT LISTENERS ---
        btnHome.addActionListener(e -> {
            updateHomeScreen();
            cardLayout.show(displayPanel, "HOME");
        });

        btnVisitTavern.addActionListener(e -> {
            updateTavernScreen();
            cardLayout.show(displayPanel, "TAVERN");
        });

        btnViewQuests.addActionListener(e -> cardLayout.show(displayPanel, "QUEST"));
        
        btnRankings.addActionListener(e -> {
            updateRankingsScreen();
            cardLayout.show(displayPanel, "RANKINGS");
        });
        
        btnRetire.addActionListener(e -> System.exit(0));

        // --- GAMEPLAY ACTION EVENT LISTENERS ---
        btnBuyHero.addActionListener(e -> {
            int index = tavernList.getSelectedIndex();
            if (index != -1) {
                Hero selected = tavern.getAvailableHeroes().get(index);
                if (playerGuild.getGold() >= selected.getHiringCost()) {
                    playerGuild.setGold(playerGuild.getGold() - selected.getHiringCost());
                    playerGuild.addHero(tavern.hireHero(index));
                    
                    // Instant UI Updates for live feedback
                    updateTavernScreen();
                    updateHomeScreen();
                    JOptionPane.showMessageDialog(this, "Hired " + selected.getName() + "!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough gold!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAttemptQuest.addActionListener(e -> {
            int index = questList.getSelectedIndex();
            if (index != -1) {
                Quest selected;
                if (demonKingAppeared && index == dailyQuests.size()) {
                    selected = new Quest("Defeat the Demon King", 200, 5000, 100);
                } else {
                    selected = dailyQuests.get(index);
                }

                int power = playerGuild.calculateTotalPower();
                if (selected.attemptQuest(power)) {
                    playerGuild.setGold(playerGuild.getGold() + selected.getGoldReward());
                    playerGuild.setReputation(playerGuild.getReputation() + selected.getReputationReward());
                    JOptionPane.showMessageDialog(this, "Success! You completed: " + selected.getQuestName());
                    
                    if (playerGuild.getReputation() >= 100) {
                        JOptionPane.showMessageDialog(this, "LEGENDARY STATUS ACHIEVED! YOU WIN!");
                        System.exit(0);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failure! Your guild was defeated.", "Defeat", JOptionPane.WARNING_MESSAGE);
                    if (selected.getQuestName().contains("Demon King")) {
                        JOptionPane.showMessageDialog(this, "Your guild was completely wiped out by the Demon King. Game Over.");
                        System.exit(0);
                    }
                }
                
                if (index < dailyQuests.size()) dailyQuests.remove(index);
                else demonKingAppeared = false; 
                updateQuestScreen();
                updateHomeScreen();
            }
        });

        btnEndTurn.addActionListener(e -> {
            // Highly Active Rivals Simulation
            worldBoard.simulateRivalGuilds(rivals);
            // Extra activity burst to make rival guilds highly competitive
            for (Guild rival : rivals) {
                if (Math.random() < 0.50) { 
                    rival.setReputation(rival.getReputation() + (int)(Math.random() * 15) + 5);
                }
            }
            
            if (turnCounter >= 5) {
                checkFinalVictory();
            } else {
                turnCounter++;
                startNewTurn();
                updateRankingsScreen();
                JOptionPane.showMessageDialog(this, "Turn Ended. Rivals have accelerated their expansion plans!");
            }
        });

        // Initialize Phase
        startNewTurn();
    }

    private void startNewTurn() {
        turnLabel.setText("Turn " + turnCounter + " of 5 | Guild: " + playerGuild.getGuildName());
        
        // Strict limit: Only 3-4 heroes appear at a time
        tavern.getAvailableHeroes().clear();
        tavern.generateDailyHeroes();
        
        // Pull custom preferences heroes if configured
        if (customDatabase != null && !customDatabase.isEmpty()) {
            int pullCount = Math.min(customDatabase.size(), 2);
            for(int i = 0; i < pullCount; i++) {
                // Remove hired or displayed preferences heroes from database array pool so they don't loop repeats
                Hero pulled = customDatabase.remove((int)(Math.random() * customDatabase.size()));
                tavern.getAvailableHeroes().add(pulled);
            }
        }
        
        // Trim back window to ensure strict 3-4 caps
        while(tavern.getAvailableHeroes().size() > 4) {
            tavern.getAvailableHeroes().remove(tavern.getAvailableHeroes().size() - 1);
        }

        dailyQuests = worldBoard.generateDailyQuests();
        demonKingAppeared = Math.random() < 0.25;

        updateHomeScreen();
        updateTavernScreen();
        updateQuestScreen();
        updateRankingsScreen();
        cardLayout.show(displayPanel, "HOME");
    }

    private void updateHomeScreen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GUILD HQ STATUS ===\n");
        sb.append("Gold: ").append(playerGuild.getGold()).append("g\n");
        sb.append("Current Reputation: ").append(playerGuild.getReputation()).append("\n");
        sb.append("Total Combat Power: ").append(playerGuild.calculateTotalPower()).append("\n\n");
        
        sb.append("=== REGISTERED ROSTER (").append(playerGuild.getRoster().size()).append(") ===\n");
        for (Hero h : playerGuild.getRoster()) {
            sb.append("- ").append(h.getName()).append(" (").append(h.getHeroClass())
              .append(") | Power: ").append(h.getPowerLevel()).append("\n");
        }
        homeText.setText(sb.toString());
    }

    private void updateTavernScreen() {
        // Live Real-Time Gold Update
        tavernGoldLabel.setText("Current Gold: " + playerGuild.getGold() + "g");
        
        tavernListModel.clear();
        for (Hero h : tavern.getAvailableHeroes()) {
            tavernListModel.addElement(h.getName() + " | " + h.getHeroClass() + " | Power: " + h.getPowerLevel() + " | Cost: " + h.getHiringCost() + "g");
        }
    }

    private void updateQuestScreen() {
        questListModel.clear();
        for (Quest q : dailyQuests) {
            questListModel.addElement(q.toString());
        }
        if (demonKingAppeared) {
            questListModel.addElement("!!! [EPIC BOSS] Defeat the Demon King | Diff: 200 | Gold: 5000 | Rep: 100");
        }
    }

    private void updateRankingsScreen() {
        ArrayList<Guild> standings = new ArrayList<>();
        standings.add(playerGuild);
        standings.addAll(rivals);
        
        // Sort highest reputation to lowest
        standings.sort((g1, g2) -> Integer.compare(g2.getReputation(), g1.getReputation()));
        
        StringBuilder sb = new StringBuilder();
        sb.append("===================================================\n");
        sb.append("               OFFICIAL GUILD STANDINGS            \n");
        sb.append("===================================================\n");
        sb.append(String.format("%-6s %-25s %-12s\n", "Rank", "Guild Name", "Reputation"));
        sb.append("---------------------------------------------------\n");
        
        for (int i = 0; i < standings.size(); i++) {
            Guild g = standings.get(i);
            sb.append(String.format("%-6d %-25s %-12d\n", (i + 1), g.getGuildName(), g.getReputation()));
        }
        rankingText.setText(sb.toString());
    }

    private void checkFinalVictory() {
        boolean isWinner = true;
        for (Guild rival : rivals) {
            if (rival.getReputation() > playerGuild.getReputation()) {
                isWinner = false;
                break;
            }
        }
        if (isWinner) {
            JOptionPane.showMessageDialog(this, "🏆 Victory! Your guild holds supreme rank across the territory. YOU WIN!");
        } else {
            JOptionPane.showMessageDialog(this, "Defeat. A rival syndicate outperformed your operations. GAME OVER.");
        }
        System.exit(0);
    }
}