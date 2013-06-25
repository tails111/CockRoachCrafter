package CockRoachCrafter;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class BankHandler extends Node {

    private final int[] BANKS = {42378,42377,42217};
    private final int[] ITEMS = {554,561,1734,379};
    private final int[] ITEM_AMOUNTS = {15000,3000,1000,12};

    Tile bankTile = new Tile(3093,3494,0);

    @Override
    public boolean activate(){
        return Calculations.distanceTo(bankTile)<=8 &&
                !Inventory.containsAll(ITEMS);
    }

    @Override
    public void execute(){
        SceneObject Booth = SceneEntities.getNearest(BANKS);

        if(!Bank.isOpen()){
            CockRoachCrafter.profit = "Opening bank booth.";
            Booth.interact("Bank");
            Task.sleep(1000,1500);
        }
        CockRoachCrafter.profit = "Depositing inventory.";
        Bank.depositInventory();
        for(int x = 0; x<= ITEMS.length-1; x++){
            CockRoachCrafter.profit = "Withdrawing items.";
            Bank.withdraw(ITEMS[x], ITEM_AMOUNTS[x]);
        }
        CockRoachCrafter.profit = "Closing bank.";
        Bank.close();
        Task.sleep(500,1000);

    }
}