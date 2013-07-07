package CockRoachCrafter.Nodes.LootHandlers;

import CockRoachCrafter.CockRoachCrafter;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

public class CraftingHandler extends Node {

    @Override
    public boolean activate(){

        if(Inventory.getCount(25551)>=2 && Inventory.contains(1734) &&
                !Players.getLocal().isInCombat() && Inventory.isFull()){
            return true;
        }
        return false;
    }


    //WIDGET 1179, child 16, child 0 == Texture ID 8754
    //WIDGET 1371, child 44, child 12 == Texture ID 15199 //SELECTING CARAPCE LEGS
    //WIDGET 1370, child 40, child 0 == Text == Craft // CRAFTING LEGS

    @Override
    public void execute(){
        CockRoachCrafter.profit="Clicking Carapace";
        Inventory.getItem(25551).getWidgetChild().interact("Craft");
        CockRoachCrafter.profit="Waiting for screens.";
        Task.sleep(750,1250);
        if(Widgets.get(1179).validate()){
            CockRoachCrafter.profit="Clicking Needle.";
            Widgets.get(1179,16).getChild(1).click(true);
        }
        Task.sleep(250,500);
        if(Widgets.get(1371,44).getChild(13).validate()){
            CockRoachCrafter.profit="Clicking Legs.";
            Widgets.get(1371,44).getChild(12).click(true);;
        }
        Task.sleep(250,500);
        if(Widgets.get(1370,40).getChild(0).validate()){
            CockRoachCrafter.profit="Clicking Craft.";
            Widgets.get(1370,40).getChild(0).click(true);;
        }
        Task.sleep(1500,2000);

        while(Widgets.get(1251).validate()){
            Task.sleep(250,500);
        }

    }
}
