public class GameData {
    public static int selectedWeapon = 0; // 0 = Sword, 1 = Bow
    public static int selectedBoss = 0;   // 0 = Teostra, 1 = Velkhana
    public static boolean gameWon = false;
    public static int gameTime = 0; // Time taken to win in seconds
    public static int totalGameTime = 60; // Total game time in seconds (1 minute)
    
    public static void reset() {
        selectedWeapon = 0;
        selectedBoss = 0;
        gameWon = false;
        gameTime = 0;
        totalGameTime = 60;
    }
}