import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
     * not feeling confident with this class
     */
    public class SeaTurtle extends Movable {

        public static final String SEATURTLE_KEY = "seaturtle";
        public static final String SEATURTLE_ID = "seaturtle";
        public static final int SEATURTLE_ACTION_PERIOD = 500;
        public static final int SEATURTLE_ANIMATION_PERIOD = 500;


        public static final Random rand = new Random();
        boolean isInfected;

        public SeaTurtle(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod) {
            super(id, position, images, actionPeriod, animatedPeriod);
        }

        public static SeaTurtle createSeaTurtle(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod){
            return new SeaTurtle(id, position, images, actionPeriod,  animatedPeriod);
        }

        @Override
        public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
            if (adjacent(this.position, target.position))
            {
                world.removeEntity(target);

                scheduler.unscheduleAllEvents(target);
                isInfected = true;
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
                    Trash.class);

            if (!turtleTarget.isPresent() ||
                    !moveTo(world, turtleTarget.get(), scheduler) ||
                    isInfected == false) {
                scheduler.scheduleEvent(this,
                        ActivityAction.createActivityAction(this, world, imageStore),
                        this.getActionPeriod());
            } else {
                becomeInfected(world, imageStore, scheduler);
            }
        }

        private boolean becomeInfected(WorldModel world, ImageStore imageStore,EventScheduler scheduler ){
            if (isInfected == true) {
                InfectedSeaTurtle infected = InfectedSeaTurtle.createInfectedseaTurtle(this.id,
                        this.position, this.actionPeriod, this.animationPeriod,
                        imageStore.getImages(InfectedSeaTurtle.INFECTED_SEATURTLE_KEY));

                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                world.addEntity(infected);
                infected.scheduleActions(scheduler, world, imageStore);

                return true;
            }

            return false;
        }


}

