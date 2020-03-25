import processing.core.PImage;

import java.util.List;
import java.util.Optional;

import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Trash extends ActivateEntity {

    public static final String TRASH_KEY = "trash";
    public static final String TRASH_ID = "trash";
    public static final int TRASH_ACTION_PERIOD = 999999999;

    public Trash(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    public static Trash createTrash(String id, Point position, int actionPeriod,
                                    List<PImage> images) {
        return new Trash(id, position, images,
                actionPeriod);
    }


    /**
     * This method parse background arounds trash and create SeaTurle in random position
     * Edit method!!
     */
    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {



        //find nearestAtlantis
        Optional<Entity>  nearestAtlantis = world.findNearest(this.position, Atlantis.class);
        Atlantis atlantis = (Atlantis) nearestAtlantis.get();
        Optional<Point> openPt = world.findOpenAround(atlantis.getPosition());

        //create Sea Turtle near Atlantis
        if (openPt.isPresent()) {
            SeaTurtle seaTurtle = SeaTurtle.createSeaTurtle(this.id,
                    openPt.get(), imageStore.getImageList(SeaTurtle.SEATURTLE_KEY), SeaTurtle.SEATURTLE_ACTION_PERIOD, SeaTurtle.SEATURTLE_ANIMATION_PERIOD
            );
            world.addEntity(seaTurtle);
            seaTurtle.scheduleActions(scheduler, world, imageStore);
        }
        scheduler.scheduleEvent(this,
                ActivityAction.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

}