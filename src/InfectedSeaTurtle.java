import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class InfectedSeaTurtle extends SeaTurtle implements Infected {

    public static final String INFECTED_SEATURTLE_KEY = "infectedseaturtle";
    private boolean readyToInfect;

    public InfectedSeaTurtle(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod) {
        super(id, position, images, actionPeriod, animatedPeriod);


    }
    public static InfectedSeaTurtle createInfectedseaTurtle(String id,
                                                Point position, int actionPeriod, int animationPeriod,
                                                List<PImage> images) {
        return new InfectedSeaTurtle(id, position, images, actionPeriod, animationPeriod);
    }


    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (adjacent(this.position, target.position))
        {
            readyToInfect = true;
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.position);

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> turtleTarget = world.findNearest(this.position,
                Fish.class);

        if (!turtleTarget.isPresent() ||
                !moveTo(world, turtleTarget.get(), scheduler)) {
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
        if(readyToInfect == true){
            infectOther(turtleTarget.get(), imageStore, world, scheduler);
        }
    }


    public void infectOther(Entity target, ImageStore imageStore, WorldModel world, EventScheduler scheduler) {
        Point fishPos = target.getPosition();
            //initial fish is gone
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            //turtle can not live longer so it also dies
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            //create infectedFish in place of old fish
            Fish infectedFish = InfectedFish.createInfectedFish(target.getId(), fishPos,
                    imageStore.getImageList(InfectedFish.INFECTED_FISH_KEY), ((Fish) target).getActionPeriod());
            world.addEntity(infectedFish);
            infectedFish.scheduleActions(scheduler, world, imageStore);
    }

}


