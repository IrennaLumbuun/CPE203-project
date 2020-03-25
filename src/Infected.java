public interface Infected {
    /**
     * This method creates a new infected-version of target entity.
     * @param target the entity that we want to infect
     */
    static void infectOther(Entity target, ImageStore imagestore, WorldModel world, EventScheduler scheduler){
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

}
