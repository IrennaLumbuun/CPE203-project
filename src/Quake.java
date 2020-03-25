import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {
    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod) {
        super(id, position, images, actionPeriod, animatedPeriod);
    }


    public static Quake createQuake(String id, Point position, List<PImage> images)
    {
        return new Quake(id, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this,
                ActivityAction.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent( this,
                AnimationAction.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());

    }

@Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
