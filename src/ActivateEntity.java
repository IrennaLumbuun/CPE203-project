import processing.core.PImage;

import java.util.List;

public abstract class ActivateEntity extends Entity{

    protected int actionPeriod;

    public ActivateEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }


    public int getActionPeriod() {
        return actionPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                ActivityAction.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public abstract void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
