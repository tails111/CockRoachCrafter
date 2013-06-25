package CockRoachCrafter;


import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;

public class EquipmentChecker extends Node {
    @Override
    public boolean activate(){
        if(Inventory.contains(25831)){
            return true;
        }
        return false;
    }

    @Override
    public void execute(){
        CockRoachCrafter.profit = "Fixing equipment.";
        Inventory.getItem(25831).getWidgetChild().interact("Wear");
        Task.sleep(250,500);
    }
}
