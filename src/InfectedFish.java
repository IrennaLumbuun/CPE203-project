import processing.core.PImage;

import java.util.List;

public class InfectedFish extends Fish implements Infected

{
    public static final String INFECTED_FISH_KEY = "infectedfish";

    public InfectedFish(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    public static InfectedFish createInfectedFish(String id, Point position, List<PImage> imageList, int actionPeriod) {
        return new InfectedFish(id, position, imageList,
                actionPeriod);
    }


    public static void infectOther(Entity target, ImageStore imageStore,WorldModel world, EventScheduler scheduler) {
        OctoNotFull isTarget = (OctoNotFull) target;
        Point pos = isTarget.getPosition();
        //remove target
        world.removeEntity(target);
        world.removeEntity(isTarget);
        scheduler.unscheduleAllEvents(target);
        scheduler.unscheduleAllEvents(isTarget);
        //create infectedOcto
        InfectedOcto infectedOcto = InfectedOcto.createInfectedOcto(InfectedOcto.INFECTED_OCTO_KEY, pos, imageStore.getImageList(InfectedOcto.INFECTED_OCTO_KEY),
                isTarget.getActionPeriod(), isTarget.getAnimationPeriod(), isTarget.getResourceLimit(), isTarget.getResourceCount());
        world.addEntity(infectedOcto);
        infectedOcto.scheduleActions(scheduler, world, imageStore);
    }

}
