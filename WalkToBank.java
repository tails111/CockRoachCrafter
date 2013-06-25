package CockRoachCrafter;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

public class WalkToBank extends Node {

    Tile[] toBankPath = new Tile[] {
            new Tile(3074,3502,0), new Tile(3081, 3501, 0), new Tile(3087, 3498, 0), new Tile(3093, 3494, 0)};
    Tile bankTile = new Tile(3093,3494,0);
    Tile afterTeleport = new Tile(3067,3505,0);
    private final int[] ITEMS = {554,561,1734,379};

    @Override
    public boolean activate(){
        return (Calculations.distanceTo(afterTeleport)<=45) && !Inventory.containsAll(ITEMS) &&
                (Players.getLocal().getAnimation()==-1 && Calculations.distanceTo(bankTile)>=5);
    }

    @Override
    public void execute(){
        CockRoachCrafter.profit = "Walking to the Bank.";
        Walking.newTilePath(toBankPath).traverse();
    }
}