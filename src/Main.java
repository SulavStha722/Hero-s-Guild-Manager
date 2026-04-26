import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            // Demon King grants exactly 100 Reputation to trigger the auto-win!
            Quest demonKingQuest = new Quest("Defeat the Demon King", 200, 5000, 100); 
            
            System.out.println("1. Visit Tavern (Hire Heroes)");
            System.out.println("2. View Bounty Board (Quests)");
            System.out.println("3. End Turn (Simulate Rivals)");
            System.out.println("4. Retire (Quit Game)");
            System.out.print("Choose an action: ");
            
            // --- EXCEPTION HANDLING STARTS HERE ---
            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\n*** Invalid input. Please enter a number. ***");
                scanner.nextLine(); // Clear the invalid input from the buffer
                continue; // Skip the rest of the loop and start the turn menu over
            }
            // --- EXCEPTION HANDLING ENDS HERE ---
            
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
                        break; // Break out of the switch and return to the main menu
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
                    
                    if (demonKingAppeared) {
                        System.out.println("4. [EPIC BOSS] " + demonKingQuest.toString() + " (EXTREME RISK!)");
                    }
                    
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
                            playerGuild.setGold(playerGuild.getGold() + selectedQuest.getGoldReward());
                            playerGuild.setReputation(playerGuild.getReputation() + selectedQuest.getReputationReward());
                        } else {
                            System.out.println("Failure! Your team wasn't strong enough and retreated.");
                        }
                    } else if (questChoice == 4 && demonKingAppeared) {
                        System.out.println("\nYou challenge the Demon King...");
                        if (demonKingQuest.attemptQuest(myPower)) {
                            System.out.println("\n*** The Demon King has fallen! ***");
                            playerGuild.setGold(playerGuild.getGold() + demonKingQuest.getGoldReward());
                            playerGuild.setReputation(playerGuild.getReputation() + demonKingQuest.getReputationReward());
                            // The 100 Rep check at the end of the loop will catch this and win the game!
                        } else {
                            System.out.println("\n*** TRAGEDY! Your guild was wiped out by the Demon King! ***");
                            System.out.println("The realm falls into darkness. GAME OVER.");
                            gameIsRunning = false;
                        }
                    } else if (questChoice != 0) {
                        System.out.println("Invalid choice. You stepped away from the board.");
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
                        
                        if (isWinner) {
                            System.out.println("Congratulations! " + playerGuild.getGuildName() + " secured the #1 Rank!");
                            System.out.println("You outlasted the competition. YOU WIN!");
                        } else {
                            System.out.println("Another guild surpassed you in reputation.");
                            System.out.println("You failed to become the top guild. GAME OVER.");
                        }
                        gameIsRunning = false;
                    } else {
                        turnCounter++;
                    }
                    break;
                    
                case 4:
                    System.out.println("Retiring from guild management. Final Reputation: " + playerGuild.getReputation());
                    gameIsRunning = false;
                    break;
                    
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            
            // --- End of Turn State Checks ---
            if (!gameIsRunning) {
                continue; // Skip the checks below if the game already ended (e.g. Turn 5 or Demon King loss)
            }
            
            if (playerGuild.getGold() < 0) {
                System.out.println("You are bankrupt! GAME OVER.");
                gameIsRunning = false;
            } else if (playerGuild.getReputation() >= 100) {
                // This catches the 100 Rep Early Win (whether from normal quests or the Demon King)
                System.out.println("\n*** INCREDIBLE! " + playerGuild.getGuildName() + " reached " + playerGuild.getReputation() + " Reputation! ***");
                System.out.println("You achieved legendary status and won the game early! YOU WIN!");
                gameIsRunning = false;
            }
        }
        scanner.close();
    }
}