import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AStarPathingStrategy implements PathingStrategy {
    /**
     * Logic:
     * 1. Add the starting square (or node) to the open list.
     * 2. Repeat the following:
     * A) Look for the lowest F cost square on the open list. We refer to this as the hasMinF.
     * B). Switch it to the closed list.
     * C) For each of the 4 squares adjacent to this current square …
     * If it is not walkable or if it is on the closed list, ignore it. Otherwise do the following.
     * If it isn’t on the open list, add it to the open list. Make the current square the parent of this square. Record the F, G, and H costs of the square.
     * If it is on the open list already, check to see if this path to that square is better, using G cost as the measure. A lower G cost means that this is a better path. If so, change the parent of the square to the current square, and recalculate the G and F scores of the square. If you are keeping your open list sorted by F score, you may need to resort the list to account for the change.
     * D) Stop when:
     * Add the target square to the closed list, in which case the path has been found, or
     * Fail to find the target square, and the open list is empty. In this case, there is no path.
     */

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {

        ArrayList<Point> openList = new ArrayList<>();
        ArrayList<Point> closedList = new ArrayList<>();

        openList.add(start); //put starting node with f = 0
        start.setF(start.computeH(start, end)); //distance from start to finish
        start.setParent(start); //start's parent is itself

        //while not empty
        while (!openList.isEmpty()) {
            //find point with least f
            Point hasMinF  = openList.get(openList.size() - 1);
            double isMinF = hasMinF.getF();
            for (int i = openList.size() - 1; i >= 0; i--) {
                if (openList.get(i).getF() < isMinF) {
                    hasMinF = openList.get(i);
                    isMinF = hasMinF.getF();
                }
            }
            //pop from openList
            openList.remove(hasMinF);
            closedList.add(hasMinF);

            //generate successor for node that has MinF
            List<Point> filteredList = potentialNeighbors.apply(hasMinF) //get 4 neighbors
                    .filter(canPassThrough) //only neighbors that is withinbound and not an obstacle
                    .collect(Collectors.toList());

            //Analyse neighbors
            for (int i = 0; i< filteredList.size(); i++) {
                //Check if neighbor is the goal
                // if successor == goal: stop searching
                if (withinReach.test(filteredList.get(i), end)) {
                    closedList.add(filteredList.get(i));
                    filteredList.get(i).setParent(hasMinF);
                    return backtrack(filteredList.get(i));
                }

                // See if there is duplicate in closed list
                boolean duplicateCL = false;
                for (int j =0; j < closedList.size(); j++) {
                    if (closedList.get(j).getX() == filteredList.get(i).getX() && closedList.get(j).getY() == filteredList.get(i).getY()) {
                        duplicateCL = true;
                    }
                }
                //if there is no duplicate in closedList
                if (!duplicateCL) {
                    //check if it's in openList
                    boolean duplicateOL = false;
                    Point dPoint = null;
                    for (int j = 0; j < openList.size(); j++) {
                        if (openList.get(j).getX() == filteredList.get(i).getX() && openList.get(j).getY() == filteredList.get(i).getY()) {
                            duplicateOL = true;
                            dPoint = openList.get(j);
                        }
                    }

                    double fValue = filteredList.get(i).computeF(start, hasMinF, end);
                    //put to openList if they're not in openList already
                    if (!duplicateOL) {
                        //compute f
                        filteredList.get(i).setParent(hasMinF);
                        openList.add(filteredList.get(i));
                        filteredList.get(i).setF((fValue));
                    } else {
                        //check to see if this is the better path
                        if (filteredList.get(i).g < dPoint.g) {
                            filteredList.get(i).setParent(hasMinF);
                            openList.remove(dPoint); //remove the old one
                            openList.add(filteredList.get(i));
                        }
                    }
                }
            }
        }


        if (openList.isEmpty()) {
            return new LinkedList<>();
        }

        return new LinkedList<>();
    }

    public LinkedList<Point> backtrack(Point end) {
        LinkedList<Point> realPath = new LinkedList<>();
        Point toPut = end;
        while(!toPut.getParent().equals(toPut)) {
            realPath.add(toPut);
            toPut = toPut.getParent();
        }
        Collections.reverse(realPath);
        return realPath;
    }
}

