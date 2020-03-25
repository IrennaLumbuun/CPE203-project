import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActivateEntity {
    protected int animationPeriod;


    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animatedPeriod;
    }


    public int getAnimationPeriod() {
        return this.animationPeriod;
    }


    public void nextImage() {
        setImageIndex((getImageIndex() + 1) % getImages().size());
    }
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent( this,
                ActivityAction.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this, AnimationAction.createAnimationAction(this, 0),
                getAnimationPeriod());
    }
}
