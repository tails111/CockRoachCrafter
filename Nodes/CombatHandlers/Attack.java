package CockRoachCrafter.Nodes.CombatHandlers;

import CockRoachCrafter.CockRoachCrafter;
import CockRoachCrafter.Nodes.LootHandlers.*;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.interactive.Character;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

import java.awt.*;

public class Attack extends Node {

    FoodHandler eating = new FoodHandler();
    EquipmentChecker equipment = new EquipmentChecker();
    Rectangle screen = new Rectangle(1,55,518,258);
    Point clickPoint = new Point();
    Character me;
    NPC theRoach;

    public void altCameraTurnTo(Entity e){
        int x = 0;
        do{
            Camera.setAngle(Camera.getYaw() + Random.nextInt(35, 55));
            x++;
            if(x>=15){
                break;
            }
        }while(!altIsOnScreen(e));
    }


    public boolean altIsOnScreen(Entity e){
        Integer numOfPoints;
        for(final Polygon p : e.getBounds()){
            numOfPoints = p.xpoints.length + p.ypoints.length;
            if(screen.contains(p.getBounds()) && numOfPoints>=4){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean activate(){
        Area roachArea = new Area(new Tile(3100,4350,1), new Tile(3200,4100,1));   //NEED ROACH AREA TILES
        me = Players.getLocal();
        theRoach = NPCs.getNearest("Cockroach Soldier");

        return (theRoach != null && roachArea.contains(me.getLocation())
                && me.getInteracting()==null && theRoach.getHealthPercent()!=0 &&
                me.getPlane()==1 && Inventory.contains(379));
    }

    @Override
    public void execute(){
        CockRoachCrafter.profit="Attacking Roach.";

        ActionBarHandler.momentumCheck();
        if(equipment.activate()){
            equipment.execute();
        }

        if(theRoach != null){
            if(Calculations.distanceTo(theRoach)>=4){
                if(!altIsOnScreen(theRoach)){
                    altCameraTurnTo(theRoach);
                }
            }
            if(!altIsOnScreen(theRoach)){
                altCameraTurnTo(theRoach);
            }
            if(Calculations.distanceTo(theRoach)>=8){
                Walking.walk(theRoach);
            }
            if(theRoach.interact("Attack")){
                Walking.walk(theRoach);
                theRoach.interact("Attack");
            }
            altCameraTurnTo(theRoach);
            if(!Players.getLocal().isInCombat()){
                theRoach.interact("Attack");
            }
        }

        if(eating.activate()){
            eating.execute();
        }
    }

}
