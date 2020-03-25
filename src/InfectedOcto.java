import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class InfectedOcto extends OctoFull implements Infected{
    public static final String INFECTED_OCTO_KEY = "infectedocto";


    public InfectedOcto(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animatedPeriod, resourceLimit, resourceCount);
    }

    /**
     * This method create a new infected-version of entity target
     * @param target is the nearest entity that will get infected
     */


    public void infectOther(Entity target, ImageStore imageStore, WorldModel world, EventScheduler scheduler) {
        //if come across octo, infect octo
        if(target instanceof OctoNotFull || target instanceof OctoFull){
            //create infected Octo
            Octo infectedOcto = InfectedOcto.createInfectedOcto(target.getId(), target.getPosition(),
                    imageStore.getImageList(InfectedOcto.INFECTED_OCTO_KEY), ((Octo) target).getActionPeriod(), ((Octo) target).getAnimationPeriod(), ((Octo) target).getResourceLimit(), ((Octo) target).getResourceCount());
            //remove this entity
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            //add new entity
            world.addEntity(infectedOcto);
            infectedOcto.scheduleActions(scheduler, world, imageStore);

        }
    }
    public static InfectedOcto createInfectedOcto(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod, int resourceLimit, int resourceCount){
        return new InfectedOcto(id, position, images, actionPeriod, animatedPeriod, resourceLimit, resourceCount);
    }



    /**
     * Override from Octofull. InfectedOcto will still go to Atlantis but will not transform to unfull.
     * Instead, will disappear and create quake
     */
    @Override
    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> targetOcto = world.findNearest(position,
                OctoNotFull.class);
        if(!targetOcto.isPresent()){
            targetOcto = world.findNearest(position,
                    OctoFull.class);
        }
        Optional<Entity> targetAtlantis = world.findNearest(position, Atlantis.class);
        Optional<Entity> target;

            if (Math.sqrt(targetAtlantis.get().getPosition().distanceSquared(targetAtlantis.get().getPosition(), this.getPosition()))
                    < Math.sqrt(targetOcto.get().getPosition().distanceSquared(targetOcto.get().getPosition(), this.getPosition()))) {
                //if Atlantis is closer than octo
                target = targetAtlantis;
            } else {
                target = targetOcto;
            }

        if (target.isPresent() &&
                moveTo(world, target.get(), scheduler)) {
            
            //if target equals Atlantis
            if(target.get() instanceof Atlantis) {
                ((ActivateEntity) target.get()).scheduleActions(scheduler, world, imageStore);

                Point quakePos = this.position;
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);

                Quake quake = Quake.createQuake(Quake.QUAKE_ID, quakePos,
                        imageStore.getImageList(Quake.QUAKE_KEY));

                world.addEntity(quake);
                quake.scheduleActions(scheduler, world, imageStore);
            } else if(target.get() instanceof OctoFull || target.get() instanceof OctoNotFull) {
                infectOther(target.get(), imageStore, world, scheduler);
                this.execute(world, imageStore, scheduler);
            }
        }
        else
        {
            scheduler.scheduleEvent(this,
                    ActivityAction.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }
}
