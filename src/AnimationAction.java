public class AnimationAction implements Action {
    private AnimatedEntity entity;
    private int repeatCount;

    public AnimationAction(AnimatedEntity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public static AnimationAction createAnimationAction(AnimatedEntity entity, int repeatCount)
    {
        return new AnimationAction(entity, repeatCount);
    }
    @Override
    public void execute(EventScheduler scheduler) {
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    new AnimationAction(this.entity,
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }

    }
}
