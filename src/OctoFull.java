import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo{

    public OctoFull(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animatedPeriod, resourceLimit, resourceCount);
    }


    public static OctoFull createOctoFull(String id, int resourceLimit,
                                        Point position, int actionPeriod, int animationPeriod,
                                        List<PImage> images)
    {
        return new OctoFull(id, position, images, actionPeriod, animationPeriod,
                resourceLimit, resourceLimit);
    }


@Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
    ActivateEntity octo = OctoNotFull.createOctoNotFull(this.id, this.resourceLimit,
            this.position, this.actionPeriod, this.animationPeriod,
            this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);

        return true;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (adjacent(this.position, target.position))
        {
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
        Optional<Entity> fullTarget = world.findNearest(position,
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveTo(world, fullTarget.get(), scheduler)) {
            //at atlantis trigger animation
            ((ActivateEntity)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transform(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

}
