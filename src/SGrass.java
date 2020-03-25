import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SGrass extends ActivateEntity {
    public static final String SGRASS_KEY = "seaGrass";
    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;

    public SGrass(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }


    public static SGrass createSgrass(String id, Point position, int actionPeriod,
                               List<PImage> images) {
        return new SGrass(id, position, images,
                actionPeriod);
    }


    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent()) {
            ActivateEntity fish = Fish.createFish(Fish.FISH_ID_PREFIX + this.id,
                    openPt.get(), Fish.FISH_CORRUPT_MIN +
                            Fish.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN),
                    imageStore.getImageList(Fish.FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                ActivityAction.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }


}
