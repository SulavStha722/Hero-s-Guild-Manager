import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filename = "heroes.csv";
        
        System.out.println("==========================================");
        System.out.println("   HERO'S GUILD MANAGER - SYSTEM LAUNCH   ");
        System.out.println("==========================================");
        System.out.println("\nSelect Interface Mode:");
        System.out.println("1. Graphical User Interface (GUI)");
        System.out.println("2. Text-Based Interface (Console)");
        System.out.print("Enter choice (1 or 2): ");
        
        String choice = scanner.nextLine();
        
        if (choice.equals("1")) {
            System.out.println("Loading database for GUI...");
            // Borrow the load method from your existing TextMain file!
            ArrayList<Hero> database = TextMain.loadHeroes(filename);
            
            System.out.println("Launching GUI Admin Panel...");
            // Run GUI on the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                GuildManagerGUI gui = new GuildManagerGUI(database, filename);
                gui.setLocationRelativeTo(null);
                gui.setVisible(true);
            });
        } else if (choice.equals("2")) {
            System.out.println("Launching Console Interface...\n");
            // Run your unmodified TextMain file exactly as it is!
            TextMain.main(args);
        } else {
            System.out.println("Invalid choice. Defaulting to Console Interface...\n");
            TextMain.main(args);
        }
        
        // We close the scanner here only if we launched the GUI, 
        // otherwise TextMain handles its own scanner.
        if (choice.equals("1")) {
            scanner.close();
        }
    }
}