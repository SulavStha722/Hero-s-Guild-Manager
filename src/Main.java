import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    
    public static ArrayList<Hero> loadHeroes(String filename) {
        ArrayList<Hero> heroes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String name = data[0];
                    String heroClass = data[1];
                    int power = Integer.parseInt(data[2]);
                    int cost = Integer.parseInt(data[3]);
                    
                    if (heroClass.equalsIgnoreCase("Warrior")) heroes.add(new Warrior(name, power, cost));
                    else if (heroClass.equalsIgnoreCase("Mage")) heroes.add(new Mage(name, power, cost));
                    else if (heroClass.equalsIgnoreCase("Archer")) heroes.add(new Archer(name, power, cost));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file or file not found. Creating a blank database.");
        }
        return heroes;
    }

    public static void saveHeroes(ArrayList<Hero> heroes, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Hero h : heroes) {
                bw.write(h.getName() + "," + h.getHeroClass() + "," + h.getPowerLevel() + "," + h.getHiringCost());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filename = "heroes.csv";
        
        System.out.println("==========================================");
        System.out.println("   HERO'S GUILD MANAGER - SYSTEM ADMIN    ");
        System.out.println("==========================================");
        
        boolean inAdminMode = true;
        ArrayList<Hero> database = loadHeroes(filename);
        System.out.println("Loaded " + database.size() + " heroes from " + filename);
        
        while (inAdminMode) {
            System.out.println("\n--- Admin Database Menu ---");
            System.out.println("1. View All Records");
            System.out.println("2. Add New Record");
            System.out.println("3. Update Record");
            System.out.println("4. Delete Record");
            System.out.println("5. Search/Query Records");
            System.out.println("6. Start Game (Exit Admin)");
            System.out.print("Select operation: ");
            
            int adminChoice = -1;
            try {
                adminChoice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }
            
            switch (adminChoice) {
                case 1:
                    System.out.printf("%-5s %-12s %-10s %-10s %-10s%n", "ID", "Name", "Class", "Power", "Cost");
                    System.out.println("---------------------------------------------------");
                    for (int i = 0; i < database.size(); i++) {
                        Hero h = database.get(i);
                        System.out.printf("%-5d %-12s %-10s %-10d %-10d%n", i, h.getName(), h.getHeroClass(), h.getPowerLevel(), h.getHiringCost());
                    }
                    break;
                case 2:
                    System.out.print("Enter Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter Class (Warrior/Mage/Archer): ");
                    String newClass = scanner.nextLine();
                    System.out.print("Enter Power Level: ");
                    int newPower = scanner.nextInt();
                    System.out.print("Enter Hiring Cost: ");
                    int newCost = scanner.nextInt();
                    
                    if (newClass.equalsIgnoreCase("Warrior")) database.add(new Warrior(newName, newPower, newCost));
                    else if (newClass.equalsIgnoreCase("Mage")) database.add(new Mage(newName, newPower, newCost));
                    else database.add(new Archer(newName, newPower, newCost));
                    
                    saveHeroes(database, filename);
                    System.out.println("Record added and saved to CSV.");
                    break;
                case 3:
                    System.out.print("Enter ID of record to update: ");
                    int upId = scanner.nextInt();
                    if (upId >= 0 && upId < database.size()) {
                        System.out.print("Enter New Power Level: ");
                        database.get(upId).setPowerLevel(scanner.nextInt());
                        saveHeroes(database, filename);
                        System.out.println("Record updated and saved.");
                    } else {
                        System.out.println("Invalid ID.");
                    }
                    break;
                case 4:
                    System.out.print("Enter ID of record to delete: ");
                    int delId = scanner.nextInt();
                    if (delId >= 0 && delId < database.size()) {
                        database.remove(delId);
                        saveHeroes(database, filename);
                        System.out.println("Record deleted and saved.");
                    } else {
                        System.out.println("Invalid ID.");
                    }
                    break;
                case 5:
                    System.out.print("Enter Hero Class to search for (Warrior/Mage/Archer): ");
                    String searchClass = scanner.nextLine();
                    ArrayList<Hero> searchResults = new ArrayList<>();
                    
                    System.out.println("\n--- Query Results ---");
                    for (Hero h : database) {
                        if (h.getHeroClass().equalsIgnoreCase(searchClass)) {
                            searchResults.add(h);
                            System.out.printf("%-12s %-10s %-10d %-10d%n", h.getName(), h.getHeroClass(), h.getPowerLevel(), h.getHiringCost());
                        }
                    }
                    
                    if (searchResults.isEmpty()) {
                        System.out.println("No heroes found matching that class.");
                    } else {
                        System.out.print("\nDo you want to save these query results to a file? (Y/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("Y")) {
                            try (BufferedWriter bw = new BufferedWriter(new FileWriter("query_results.txt"))) {
                                bw.write("Query Results for Class: " + searchClass + "\n");
                                bw.write("---------------------------------------------------\n");
                                for (Hero h : searchResults) {
                                    bw.write(h.getName() + " | Power: " + h.getPowerLevel() + " | Cost: " + h.getHiringCost() + "\n");
                                }
                                System.out.println("Results successfully saved to 'query_results.txt'.");
                            } catch (IOException e) {
                                System.out.println("Error saving query results.");
                            }
                        }
                    }
                    break;
                case 6:
                    inAdminMode = false;
                    System.out.println("\nBooting game system...\n");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        // --- THE ACTUAL GAME ENGINE ---
        Random rng = new Random();
        System.out.println("Welcome to Hero's Guild Manager!");
        System.out.print("Enter your Guild's Name: ");
        String playerGuildName = scanner.nextLine();
        
        int startingGold = 500;
        Guild playerGuild = new Guild(playerGuildName, startingGold);
        
        ArrayList<Guild> rivals = new ArrayList<>();
        rivals.add(new Guild("The Iron Vanguard", startingGold));
        rivals.add(new Guild("Shadow Syndicate", startingGold));
        rivals.add(new Guild("Crimson Blades", startingGold));
        rivals.add(new Guild("Arcane Order", startingGold));
        
        Tavern tavern = new Tavern();
        WorldBoard worldBoard = new WorldBoard();
        
        boolean gameIsRunning = true;
        int turnCounter = 1;
        
        while (gameIsRunning) {
            System.out.println("\n==================================");
            System.out.println("Turn " + turnCounter + " of 5 | " + playerGuild.getGuildName() + 
                               " | Gold: " + playerGuild.getGold() + 
                               " | Rep: " + playerGuild.getReputation() +
                               " | Heroes: " + playerGuild.getRoster().size());
            System.out.println("==================================");
            
            tavern.generateDailyHeroes();
            ArrayList<Quest> dailyQuests = worldBoard.generateDailyQuests();
            
            boolean demonKingAppeared = rng.nextInt(100) < 25; 
            Quest demonKingQuest = new Quest("Defeat the Demon King", 200, 5000, 100); 
            
            System.out.println("1. Visit Tavern (Hire Heroes)");
            System.out.println("2. View Bounty Board (Quests)");
            System.out.println("3. End Turn (Simulate Rivals)");
            System.out.println("4. Retire (Quit Game)");
            System.out.print("Choose an action: ");
            
            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\n*** Invalid input. Please enter a number. ***");
                scanner.nextLine(); 
                continue; 
            }
            
            switch (choice) {
                case 1:
                    tavern.displayHeroes();
                    System.out.print("Enter the number of the hero to hire (or 0 to cancel): ");
                    
                    int hireChoice = -1;
                    try {
                        hireChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input.");
                        scanner.nextLine();
                        break; 
                    }
                    
                    if (hireChoice > 0 && hireChoice <= tavern.getAvailableHeroes().size()) {
                        Hero targetHero = tavern.getAvailableHeroes().get(hireChoice - 1);
                        if (playerGuild.getGold() >= targetHero.getHiringCost()) {
                            playerGuild.setGold(playerGuild.getGold() - targetHero.getHiringCost());
                            playerGuild.addHero(tavern.hireHero(hireChoice - 1));
                            System.out.println("Successfully hired " + targetHero.getName() + "!");
                        } else {
                            System.out.println("Not enough gold!");
                        }
                    }
                    break;
                    
                case 2:
                    System.out.println("\n--- Today's Available Quests ---");
                    for (int i = 0; i < dailyQuests.size(); i++) {
                        System.out.println((i + 1) + ". " + dailyQuests.get(i).toString());
                    }
                    if (demonKingAppeared) System.out.println("4. [EPIC BOSS] " + demonKingQuest.toString() + " (EXTREME RISK!)");
                    
                    int myPower = playerGuild.calculateTotalPower();
                    System.out.println("\nYour Guild's Total Available Power: " + myPower);
                    System.out.print("Enter the number of the quest to attempt (or 0 to cancel): ");
                    
                    int questChoice = -1;
                    try {
                        questChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input.");
                        scanner.nextLine();
                        break;
                    }
                    
                    if (questChoice > 0 && questChoice <= 3) {
                        Quest selectedQuest = dailyQuests.get(questChoice - 1);
                        if (selectedQuest.attemptQuest(myPower)) {
                            System.out.println("Success! Your team conquered the objective.");
                            if(playerGuild.getRoster().size() > 0) {
                                playerGuild.getRoster().get(0).attack(); 
                            }
                            playerGuild.setGold(playerGuild.getGold() + selectedQuest.getGoldReward());
                            playerGuild.setReputation(playerGuild.getReputation() + selectedQuest.getReputationReward());
                        } else {
                            System.out.println("Failure! Your team wasn't strong enough and retreated.");
                        }
                    } else if (questChoice == 4 && demonKingAppeared) {
                        if (demonKingQuest.attemptQuest(myPower)) {
                            System.out.println("\n*** The Demon King has fallen! ***");
                            playerGuild.setGold(playerGuild.getGold() + demonKingQuest.getGoldReward());
                            playerGuild.setReputation(playerGuild.getReputation() + demonKingQuest.getReputationReward());
                        } else {
                            System.out.println("\n*** TRAGEDY! Your guild was wiped out by the Demon King! ***");
                            gameIsRunning = false;
                        }
                    }
                    break;
                    
                case 3:
                    System.out.println("\nEnding turn " + turnCounter + "...");
                    worldBoard.simulateRivalGuilds(rivals);
                    worldBoard.printLeaderboard(playerGuild, rivals);
                    
                    if (turnCounter >= 5) {
                        System.out.println("\n=== THE 5TH TURN HAS CONCLUDED! ===");
                        boolean isWinner = true;
                        for (Guild rival : rivals) {
                            if (rival.getReputation() > playerGuild.getReputation()) {
                                isWinner = false;
                                break;
                            }
                        }
                        if (isWinner) System.out.println("Congratulations! You outlasted the competition. YOU WIN!");
                        else System.out.println("Another guild surpassed you. GAME OVER.");
                        gameIsRunning = false;
                    } else {
                        turnCounter++;
                    }
                    break;
                    
                case 4:
                    gameIsRunning = false;
                    break;
                    
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            
            if (!gameIsRunning) continue;
            
            if (playerGuild.getGold() < 0) {
                System.out.println("You are bankrupt! GAME OVER.");
                gameIsRunning = false;
            } else if (playerGuild.getReputation() >= 100) {
                System.out.println("\n*** INCREDIBLE! Legendary status achieved! YOU WIN! ***");
                gameIsRunning = false;
            }
        }
        scanner.close();
    }
}