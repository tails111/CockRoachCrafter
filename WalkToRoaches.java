package CockRoachCrafter;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class WalkToRoaches extends Node {

    Tile[] toCrevice = new Tile[] {
            new Tile(3086,3489,0), new Tile(3080, 3481, 0), new Tile(3080, 3471, 0), new Tile(3087, 3465, 0),
            new Tile(3080,3462,0)};
    Tile[] toStairsOne = new Tile[] {
            new Tile(3157,4273,3), new Tile(3164,4273,3), new Tile(3170, 4271, 3)};
    Tile[] toStairsTwo = new Tile[] {
            new Tile(3174,4263,2), new Tile(3175,4251,2), new Tile(3174,4240,2), new Tile(3171,4231,2),
            new Tile(3162,4233,2), new Tile(3157,4244,2)};
    Tile[] toRoaches = new Tile[] {
            new Tile(3155,4253,1), new Tile(3155, 4262, 1), new Tile(3156, 4270, 1), new Tile(3165, 4274, 1)};

    Area roachArea = new Area(new Tile(3155,4295,1), new Tile(3177,4260,1));

    Tile endingTile = new Tile(3169,4277,1);
    Tile creviceTile = new Tile(3080,3462,0);
    Tile stairsOneTile = new Tile(3170,4271,3);
    Tile stairsTwoTile = new Tile(3157,4243,2);

    Integer creviceObj = 29728, stairsOneObj = 29671, stairsTwoObj = 29663;

    @Override
    public boolean activate(){
        return (Calculations.distanceTo(endingTile)>=8);
    }

    @Override
    public void execute(){

        Walking.newTilePath(toCrevice).traverse();
        if(Calculations.distanceTo(creviceTile)<=4){
            SceneObject toSecondLayer = SceneEntities.getNearest(creviceObj);
            if(toSecondLayer != null){
                if(!toSecondLayer.isOnScreen()){
                    Camera.turnTo(toSecondLayer);
                }
                CockRoachCrafter.profit="Clicking Crevice";
                toSecondLayer.interact("Enter");
                Task.sleep(1000,2000);
            }
        }
        if(Players.getLocal().getPlane() == 3){
            CockRoachCrafter.profit="Starting Walk to First Stairs.";
            Walking.newTilePath(toStairsOne).traverse();
        }
        if(Calculations.distanceTo(stairsOneTile)<=10 && Players.getLocal().getPlane() == 3){
            Task.sleep(1000,2000);
            CockRoachCrafter.profit="At Stairs, finding Stiars.";
            SceneObject toThirdLayer = SceneEntities.getNearest(stairsOneObj);
            Camera.setAngle('E');
            if(toThirdLayer != null){
                CockRoachCrafter.profit="Clicking stairs.";
                toThirdLayer.interact("Climb-down");
                Task.sleep(1000,2000);
            }
            CockRoachCrafter.profit="Should be down to second floor.";
        }
        if(Players.getLocal().getPlane() == 2){
            CockRoachCrafter.profit="Starting Walk to Second Stairs.";
            Walking.newTilePath(toStairsTwo).traverse();
        }
        if(Calculations.distanceTo(stairsTwoTile)<=4 && Players.getLocal().getPlane() == 2){
            CockRoachCrafter.profit="At Stairs, finding Stairs 2.";
            SceneObject toFourthlayer = SceneEntities.getNearest(stairsTwoObj);
            if(toFourthlayer != null){
                if(!toFourthlayer.isOnScreen()){
                    Camera.turnTo(toFourthlayer);
                }
                toFourthlayer.interact("Climb");
                CockRoachCrafter.profit="Clicked Stairs 2";
                Task.sleep(1000,2000);
            }
        }

        if(Calculations.distanceTo(new Tile(3171,4279,1))>=10 &&  Players.getLocal().getPlane() == 1 ||
                Calculations.distanceTo(new Tile(3176,4276,1))<=5 &&  Players.getLocal().getPlane() == 1 ||
                Calculations.distanceTo(new Tile(3162,4281,1))<=3 &&  Players.getLocal().getPlane() == 1){
            CockRoachCrafter.profit="Walking to the Roaches.";
            Walking.newTilePath(toRoaches).traverse();
            if(Players.getLocal().getPlane()==1){
                Camera.setAngle(Random.nextInt(250, 290));
            }
        }

    }

}
