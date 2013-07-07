package CockRoachCrafter;

import CockRoachCrafter.Nodes.BankingHandlers.*;
import CockRoachCrafter.Nodes.CombatHandlers.*;
import CockRoachCrafter.Nodes.LootHandlers.*;

import org.powerbot.core.Bot;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.client.Client;

import java.awt.*;


@Manifest(name = "Cockroach Killer and Body Crafter", authors = "tails111", description = "Kills Cockroaches, collects hides, crafts bodies, high alchs.", version = 1.0)
public class CockRoachCrafter extends ActiveScript implements PaintListener {

    private Client client = Bot.client();
    RandomHandler randoms = new RandomHandler();
    public static long startTime = System.currentTimeMillis();
    private int getPerHour(final int value){
        return (int) (value * 3600000D / (System.currentTimeMillis() - startTime));
    }

    private Tree script = new Tree(new Node[]{
            new TeleportToBank(),
            new WalkToBank(),
            new BankHandler(),
            new WalkToRoaches(),
            new Attack(),
            new LootHandler(),
            new AlchHandler(),
            new CraftingHandler()
    });

    public static int actualProfit = 0;
    public static int postedProfitPerMath = 0;
    public static int postedAlchs = 0;
    public static int postedAlchsPerMath = 0;
    public static int postedTimeRun = 0;

    public static String profit = "Hello Stephen.";

    @Override
    public void onRepaint(Graphics g1){

        postedProfitPerMath = getPerHour(actualProfit);
        postedAlchsPerMath = getPerHour(postedAlchs);
        postedTimeRun = getPerHour(1000);

        String second =""+ ((System.currentTimeMillis() - startTime) / 1000) % 60;
        String minute =""+ ((System.currentTimeMillis() - startTime) / (1000 * 60)) % 60;
        String hour = ""+((System.currentTimeMillis() - startTime) / (1000 * 60 * 60)) % 24;

        String time=hour+":"+minute+":"+second;
        Graphics2D g = (Graphics2D)g1;

        g.setColor(Color.ORANGE);
        int mouseY = (int) Mouse.getLocation().getY();
        int mouseX = (int) Mouse.getLocation().getX();
        g.drawLine(mouseX - 999, mouseY + 999, mouseX + 999, mouseY - 999);
        g.drawLine(mouseX + 999, mouseY + 999, mouseX - 999, mouseY - 999);
        g.drawOval(mouseX-4,mouseY-4, 8, 8);
        g.setColor(Color.GREEN);
        g.fillOval(mouseX-2,mouseY-2,5,5);

        Font fontNormal = new Font("Comic Sans MS", Font.PLAIN, 12);
        Font fontTitle = new Font("Comic Sans MS", Font.BOLD, 12);
        g.setFont(fontTitle);
        g.setColor(Color.BLACK);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g.fillRect(10, 75, 200, 84);
        g.setColor(Color.CYAN);
        g.drawLine(10, 75, 10, 159);//LEFT
        g.drawLine(10,75,210,75);//TOP
        g.drawLine(210,75,210,159);//RIGHT
        g.drawLine(10,159,210,159);//BOTTOM
                 //x1,y1,x2,y2
        g.drawString("    Cockroach Killer and Crafter",11,90);
        g.setFont(fontNormal);
        g.drawString(("Status: " + profit), 15, 105);
        g.drawString(("Time Run: " + time), 15, 122);
        g.drawString(("Profit: " + actualProfit + "(" + postedProfitPerMath + ")"), 15, 139);
        g.drawString(("Alchs/H: "+ postedAlchs + "(" + postedAlchsPerMath + ")"),15, 156);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        final NPC interacting = NPCs.getNearest("Cockroach Soldier");

        if(interacting != null){
            if(interacting.getHealthPercent() >= 75){
                g.setColor(Color.GREEN);
            } else if(interacting.getHealthPercent() >= 50){
                g.setColor(Color.YELLOW);
            } else if(interacting.getHealthPercent() >= 25){
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.RED);
            }

            for(final Polygon p : interacting.getBounds()){
                g.fillPolygon(p);
            }
        }
    }

    @Override
    public void onStart(){
        Mouse.setSpeed(Mouse.Speed.VERY_FAST);
        Camera.setPitch(Random.nextInt(37,45));

        int x = 0;
        do{
            x++;
            if(ActionBarHandler.abilityReady(1)){
                ActionBarHandler.executeAbility(1);
            }
            if(Players.getLocal().getMessage().matches("Momentum is now active.")){
                break;
            }
        }while(x<=10);
    }

    @Override
    public int loop(){
        final Node stateNode = script.state();
        if(stateNode != null && Game.isLoggedIn()){
            script.set(stateNode);
            final Node setNode = script.get();
            if(setNode != null){
                getContainer().submit(setNode);
                setNode.join();
            }
        }

        randoms.loop();
        if (client != Bot.client()) {
            WidgetCache.purge();
            Bot.context().getEventManager().addListener(this);
            client = Bot.client();
        }

        return Random.nextInt(150, 200);
    }
}