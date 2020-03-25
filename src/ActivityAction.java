import java.util.Optional;

public class ActivityAction implements Action {
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = 0;
    }

    public static ActivityAction createActivityAction(Entity entity, WorldModel world,
                                              ImageStore imageStore)
    {
        return new ActivityAction(entity, world, imageStore);
    }


    @Override
    public void execute(EventScheduler scheduler) {
        if (entity instanceof ActivateEntity) {
            ((ActivateEntity) entity).execute(world, imageStore, scheduler);
        }
        else{
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                entity.getClass()));
            }
        }
    }

