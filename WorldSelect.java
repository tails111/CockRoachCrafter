package CockRoachCrafter;


import org.powerbot.core.randoms.Login;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import java.awt.*;

public class WorldSelect {
    private final String MembersMessage = "You need a member's account to log in to this world.";
    private final String AlreadyLoggedOnMessage = "Your account has not logged out from its last session.";
    private final String SkillWorldMessage = "1500";
    private final String LoginLimit = "Login limit exceeded";
    private final String ProfileTransferMessage = "You have only just left another world";
    private final String OfflineWorldMessage = "Could not connect you to the chosen world";
    private final String FullWorld = "This world is full. Please use a different world.";
    private final String session = "game session";

    public boolean endSession() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(session);
    }

    public boolean fullWorld() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(FullWorld);
    }

    public boolean offlineWorld() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(OfflineWorldMessage);
    }

    public boolean transferAccount() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(ProfileTransferMessage);
    }

    public boolean loginLimit() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(LoginLimit);
    }

    public boolean validateEmail() {
        return Widgets.get(906, 381).validate() && Widgets.get(906, 381).isOnScreen();
    }

    public boolean skipEmail() {
        if (validateEmail()) {
            return Widgets.get(906, 381).click(true);
        }
        return false;
    }

    public boolean loginErrorVisible() {
        return endSession() || isMembers() || alreadyLoggedIn() || isSkillTotalWorld() || loginLimit() || offlineWorld() || fullWorld();
    }

    public boolean isMembers() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(MembersMessage);
    }

    public boolean alreadyLoggedIn() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(AlreadyLoggedOnMessage);
    }

    public boolean isSkillTotalWorld() {
        return isAtLobby() && Widgets.get(906, 252).getText().contains(SkillWorldMessage);
    }

    public boolean selectWorldTab() {
        WidgetChild tab = Widgets.get(906, 28);
        if (tab != null && tab.getTextureId() != 4671) {
            if (tab.click(true)) {
                Task.sleep(1500);
                return tab.getTextureId() == 4671;
            }
        }
        return false;
    }

    public void logoutLobby() {
        Environment.enableRandom(Login.class, false);
        Game.logout(true);
        for (int i = 0; i < 30 && !Widgets.get(907).getChild(1).isOnScreen(); i++) {
            Task.sleep(500, 1000);
            if (Game.getClientState() == 3) {
                Environment.enableRandom(Login.class, true);
            }
        }
        if (!isAtLobby()) {
            Environment.enableRandom(Login.class, true);
        }
    }

    public boolean areFavouritWorldsSelected() {
        if (isAtLobby() && !loginErrorVisible() && !isLoggingIn()) {
            if (Widgets.get(910, 21).validate() && Widgets.get(910, 21).getChildren().length > 2) {
                return Widgets.get(910, 21).getChild(4).validate();
            }
        }
        return false;
    }

    public void removeFavouritedWorlds() {
        if (isAtLobby() && !loginErrorVisible() && !isLoggingIn()) {
            if (Widgets.get(910, 22).validate() && Widgets.get(910, 22).getChildren().length > 2 && Widgets.get(910, 22).getChild(4).validate()) {
                if (Widgets.get(910, 22).getChild(4).click(true)) {
                    Task.sleep(50, 100);
                }
            }
            if (Widgets.get(910, 21).validate() && Widgets.get(910, 21).getChildren().length > 2 && Widgets.get(910, 21).getChild(4).validate()) {
                if (Widgets.get(910, 21).getChild(4).click(true)) {
                    Task.sleep(50, 100);
                }
            }
        }
    }

    public boolean isWorldMembers(int world) {
        int worldID = WorldToID(world);
        if (worldID != -1) {
            WidgetChild w = Widgets.get(910, 74).getChildren()[worldID];
            return w.getText().endsWith("Members");
        }
        return false;
    }

    public String getWorldActivity(int world) {
        int worldID = WorldToID(world);
        if (worldID != -1) {
            WidgetChild w = Widgets.get(910, 72).getChildren()[worldID];
            return w.getText();
        }
        return "";
    }

    public int getWorldPopulation(int world) {
        int worldID = WorldToID(world);
        if (worldID != -1) {
            WidgetChild w = Widgets.get(910, 71).getChildren()[worldID];
            return Integer.parseInt(w.getText());
        }
        return -1;
    }

    public boolean isLoggingIn() {
        return Game.getClientState() == 9 || transferAccount();
    }

    public boolean isAtLobby() {
        return Game.getClientState() == 7;
    }

    public boolean enterGame() {
        Mouse.click(Widgets.get(906,199).getCentralPoint(), true);
        Task.sleep(200,300);
        return Game.isLoggedIn();
    }

    public boolean worldOnScreen(int world) {
        int worldID = WorldToID(world);
        WidgetChild w = Widgets.get(910, 77).getChildren()[worldID];
        Rectangle screen = new Rectangle(70, 137, 612, 297);
        int yP = w.getAbsoluteY();
        Rectangle WorldRect = new Rectangle(150, yP, w.getWidth() - 200, w.getHeight());
        return !screen.contains(WorldRect);
    }

    public boolean selectWorld(int world, boolean DragScroll) {
        if (isAtLobby() && !loginErrorVisible() && !isLoggingIn()) {
            if (areFavouritWorldsSelected()) {
                removeFavouritedWorlds();
            }
            int worldID = WorldToID(world);
            if (worldID != -1) {
                WidgetChild w = Widgets.get(910, 77).getChildren()[worldID];
                WidgetChild s = Widgets.get(910, 86).getChildren()[1];
                int worldPos = w.getRelativeY();
                int worldScrollPos = (int) ((worldPos) / 8.4);
                int scrollPos = s.getRelativeY();
                Timer safeTimer = new Timer(30000);
                if (DragScroll) {
                    if (worldOnScreen(world)) {
                        int scrollOffset = Random.nextInt(2, s.getHeight() - 3);
                        Point StartPt = new Point(Random.nextInt(s.getAbsoluteX() + 2, s.getAbsoluteX() + s.getWidth() - 4), s.getAbsoluteY() + scrollOffset);
                        Point EndPoint = new Point(Random.nextInt(s.getAbsoluteX() + 2, s.getAbsoluteX() + s.getWidth() - 4), Widgets.get(910, 86).getAbsoluteY() + worldScrollPos + scrollOffset + Random.nextInt(-4, -1));
                        Task.sleep(20, 200);
                        Mouse.move(StartPt);
                        Mouse.drag(EndPoint);
                        Task.sleep(20, 200);
                    }
                } else {
                    if (worldScrollPos > scrollPos) {
                        while (worldOnScreen(world) && scrollPos < 247 && safeTimer.isRunning() && !loginErrorVisible() && !isLoggingIn()) {
                            scrollPos = Widgets.get(910, 86).getChildren()[1].getRelativeY();
                            Mouse.move((Widgets.get(910, 86).getChildren()[5]).getCentralPoint().x, (Widgets.get(910, 86).getChildren()[5]).getCentralPoint().y, (Widgets.get(910, 86).getChildren()[5]).getWidth() / 2, (Widgets.get(910, 86).getChildren()[5]).getHeight() / 2);
                            Mouse.hold(Random.nextInt(200, 600), true);
                        }
                    } else {
                        while (worldOnScreen(world) && scrollPos > 16 && safeTimer.isRunning() && !loginErrorVisible() && !isLoggingIn()) {
                            scrollPos = Widgets.get(910, 86).getChildren()[1].getRelativeY();
                            Mouse.move((Widgets.get(910, 86).getChildren()[4]).getCentralPoint().x, (Widgets.get(910, 86).getChildren()[4]).getCentralPoint().y, (Widgets.get(910, 86).getChildren()[4]).getWidth() / 2, (Widgets.get(910, 86).getChildren()[4]).getHeight() / 2);
                            Mouse.hold(Random.nextInt(200, 600), true);
                        }
                    }
                }
                WidgetChild worldBox = Widgets.get(910, 62);
                w = Widgets.get(910, 77).getChildren()[worldID];
                Rectangle screen = worldBox.getBoundingRectangle();
                Point worldPt = new Point(Random.nextInt(100, w.getWidth() / 2), (w.getAbsoluteY() + Random.nextInt(1, w.getHeight() - 3)));
                if (screen.contains(worldPt)) {
                    Mouse.click(worldPt, true);
                    String currentWorld = Widgets.get(910, 11).getText();
                    return currentWorld.endsWith(String.valueOf(world));
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public int getCurrentWorld() {
        if(Widgets.get(910,11) != null){
            String currentWorld = Widgets.get(910, 11).getText();
            if (Tabs.FRIENDS.open()) {
                return Integer.parseInt(currentWorld.substring(6));
            }
        }
        return 0;
    }

    public int WorldToID(int world) {
        WidgetChild worlds[] = Widgets.get(910, 69).getChildren();
        for (WidgetChild w : worlds) {
            if (Integer.decode(w.getText()) == world) {
                return w.getIndex();
            }
        }
        return -1;
    }
}