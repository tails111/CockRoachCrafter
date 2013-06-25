package CockRoachCrafter;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

public class TeleportToBank  extends Node {

    Tile edgevilleLoad = new Tile(3067,3505,0);
    Area edgeville = new Area(new Tile(3063,3509,0), new Tile(3100,3486,0));
    private final int[] ITEMS = {554,561,379};

    @Override
    public boolean activate(){
        return !edgeville.contains(Players.getLocal().getLocation()) &&
                Players.getLocal().getAnimation() == -1 && !Inventory.containsAll(ITEMS);
    }

    @Override
    public void execute(){
        CockRoachCrafter.profit = "Homeporting to Edgeville.";
        if(!Widgets.get(1092).getChild(0).visible()){
            Widgets.get(640).getChild(113).click(true);
            Task.sleep(750,1250);
        }
        if(Widgets.get(1092).getChild(0).visible()){
            Widgets.get(1092).getChild(45).click(true);
            do{
                Task.sleep(2000);
            }while(Calculations.distanceTo(edgevilleLoad)>=5);
        }

    }
}