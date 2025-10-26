import java.awt.*;
import java.awt.event.KeyEvent;

public class GameState {
    public enum State {
        TITLE_SCREEN,
        WEAPON_SELECTION,
        BOSS_SELECTION,
        PLAYING,
        GAME_OVER
    }
    
    private State currentState;
    private TitleScreen titleScreen;
    private WeaponSelection weaponSelection;
    private BossSelection bossSelection;
    private GamePlay gamePlay;
    private GameOver gameOver;
    
    public GameState() {
        currentState = State.TITLE_SCREEN;
        titleScreen = new TitleScreen(this);
        weaponSelection = new WeaponSelection(this);
        bossSelection = new BossSelection(this);
        gamePlay = new GamePlay(this);
        gameOver = new GameOver(this);
    }
    
    public void update() {
        switch (currentState) {
            case TITLE_SCREEN:
                titleScreen.update();
                break;
            case WEAPON_SELECTION:
                weaponSelection.update();
                break;
            case BOSS_SELECTION:
                bossSelection.update();
                break;
            case PLAYING:
                gamePlay.update();
                break;
            case GAME_OVER:
                gameOver.update();
                break;
        }
    }
    
    public void render(Graphics2D g2d) {
        switch (currentState) {
            case TITLE_SCREEN:
                titleScreen.render(g2d);
                break;
            case WEAPON_SELECTION:
                weaponSelection.render(g2d);
                break;
            case BOSS_SELECTION:
                bossSelection.render(g2d);
                break;
            case PLAYING:
                gamePlay.render(g2d);
                break;
            case GAME_OVER:
                gameOver.render(g2d);
                break;
        }
    }
    
    public void keyPressed(int keyCode) {
        switch (currentState) {
            case TITLE_SCREEN:
                titleScreen.keyPressed(keyCode);
                break;
            case WEAPON_SELECTION:
                weaponSelection.keyPressed(keyCode);
                break;
            case BOSS_SELECTION:
                bossSelection.keyPressed(keyCode);
                break;
            case PLAYING:
                gamePlay.keyPressed(keyCode);
                break;
            case GAME_OVER:
                gameOver.keyPressed(keyCode);
                break;
        }
    }
    
    public void keyReleased(int keyCode) {
        switch (currentState) {
            case TITLE_SCREEN:
                titleScreen.keyReleased(keyCode);
                break;
            case WEAPON_SELECTION:
                weaponSelection.keyReleased(keyCode);
                break;
            case BOSS_SELECTION:
                bossSelection.keyReleased(keyCode);
                break;
            case PLAYING:
                gamePlay.keyReleased(keyCode);
                break;
            case GAME_OVER:
                gameOver.keyReleased(keyCode);
                break;
        }
    }
    
    public void setState(State newState) {
        this.currentState = newState;
    }
    
    public State getCurrentState() {
        return currentState;
    }
}