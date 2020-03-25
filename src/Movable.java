import processing.core.PImage;

import java.util.List;

public abstract class Movable extends AnimatedEntity{
    public Movable(String id, Point position, List<PImage> images, int actionPeriod, int animatedPeriod) {
        super(id, position, images, actionPeriod, animatedPeriod);
    }
    public static boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) ||
                (p1.getY() == p2.getY() && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    //nextPosition implementing Single Step Pathing Strategy
/*
    public Point nextPosition(WorldModel world, Point destPos) {
        SingleStepPathingStrategy singleStep = new SingleStepPathingStrategy();
        List<Point> path = singleStep.computePath(getPosition(), destPos, SingleStepPathingStrategy.canPassThrough(world),
                SingleStepPathingStrategy.withinReach(), SingleStepPathingStrategy.CARDINAL_NEIGHBORS);

        //return the new pos if single step return a path, else return the previous position
        if(path.size()!= 0){
            return path.get(0);
        }else{
            return getPosition();
        }
    }
    */
    public Point nextPosition(WorldModel world, Point destPos) {
    AStarPathingStrategy aStar = new AStarPathingStrategy();
    List<Point> path = aStar.computePath(getPosition(), destPos, SingleStepPathingStrategy.canPassThrough(world),
            SingleStepPathingStrategy.withinReach(), SingleStepPathingStrategy.CARDINAL_NEIGHBORS);

    //return the new pos if single step return a path, else return the previous position
    if(path.size()!= 0){
        return path.get(0);
    }else{
        return getPosition();
    }
}
}
