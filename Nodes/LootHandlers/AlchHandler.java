package CockRoachCrafter.Nodes.LootHandlers;

import CockRoachCrafter.CockRoachCrafter;
import CockRoachCrafter.Nodes.CombatHandlers.ActionBarHandler;
import CockRoachCrafter.RandomHandler;


import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.tab.Inventory;

public class AlchHandler extends Node {

    RandomHandler randoms = new RandomHandler();

    public final int[] ALCH_TABLE = {1333,1143,1159,1181,1185,25861};

    @Override
    public boolean activate(){
        if(Inventory.contains(ALCH_TABLE)){
            return true;
        }
        return false;
    }

    @Override
    public void execute(){
        randoms.loop();
        CockRoachCrafter.profit = "Alching item.";
        if(ActionBarHandler.abilityReady(0)){
            ActionBarHandler.executeAbility(0);
            if(Inventory.getItem(ALCH_TABLE).getWidgetChild() != null){
                Inventory.getItem(ALCH_TABLE).getWidgetChild().interact("Cast");
            }
        }
        CockRoachCrafter.postedAlchs=(CockRoachCrafter.postedAlchs+1);
        CockRoachCrafter.actualProfit=(CockRoachCrafter.actualProfit-(LootHandler.getPrices(25551)*2)-(LootHandler.getPrices(561))+2100);
        Task.sleep(500,1000);

    }
}
