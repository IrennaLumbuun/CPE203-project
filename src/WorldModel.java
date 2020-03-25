import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel {
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;

    public static final int PROPERTY_KEY = 0;


    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    /**
     * Check if a point is within the bounds of our world.
     * @param pos the point that we want ot check (usually a location of an entity)
     * @return true if it is within bounds. False otherwise.
     */
    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows &&
                pos.getX() >= 0 && pos.getX() < this.numCols;
    }

    /**
     * check if the point is within bound and is not null (there is something there)
     * @param pos the point we want to check (location of an entity)
     * @return True is there is something there. False otherwise.
     */
    public boolean isOccupied(Point pos) {
        return withinBounds(pos) &&
                getOccupancyCell(pos) != null;
    }

    /**
     * throw an exception if we want to put an entity in a place that is already occupied.
     * @param entity the entity we want to add
     */
    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    /**
     * draw background when program starts
     * @param properties properties of the background
     * @param imageStore contains background image
     * @return true if successful, false if not
     */
    private boolean parseBackground(String [] properties,
                                         ImageStore imageStore)
    {
        if (properties.length == Background.BGND_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
                    Integer.parseInt(properties[Background.BGND_ROW]));
            String id = properties[Background.BGND_ID];
            setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Background.BGND_NUM_PROPERTIES;
    }
    /**
     * draw Octo when program starts
     * @param properties properties of the octo
     * @param imageStore contains octo image
     * @return true if successful, false if not
     */
    private boolean parseOcto(String [] properties,
                                    ImageStore imageStore)
    {
        if (properties.length == Octo.OCTO_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Octo.OCTO_COL]),
                    Integer.parseInt(properties[Octo.OCTO_ROW]));
            Entity entity = OctoNotFull.createOctoNotFull(properties[Octo.OCTO_ID],
                    Integer.parseInt(properties[Octo.OCTO_LIMIT]),
                    pt,
                    Integer.parseInt(properties[Octo.OCTO_ACTION_PERIOD]),
                    Integer.parseInt(properties[Octo.OCTO_ANIMATION_PERIOD]),
                    imageStore.getImageList(Octo.OCTO_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Octo.OCTO_NUM_PROPERTIES;
    }
    /**
     * draw Obstacle when program starts
     * @param properties properties of the obstacle
     * @param imageStore contains obstacle image
     * @return true if successful, false if not
     */
    private boolean parseObstacle(String [] properties,
                                        ImageStore imageStore)
    {
        if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
                    Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));
            Entity entity = Obstacle.createObstacle(properties[Obstacle.OBSTACLE_ID],
                    pt, imageStore.getImageList(Obstacle.OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
    }
    /**
     * draw fish when program starts
     * @param properties properties of the fish
     * @param imageStore contains fish image
     * @return true if successful, false if not
     */
    private boolean parseFish(String [] properties,
                                    ImageStore imageStore)
    {
        if (properties.length == Fish.FISH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Fish.FISH_COL]),
                    Integer.parseInt(properties[Fish.FISH_ROW]));
            Entity entity = Fish.createFish(properties[Fish.FISH_ID],
                    pt, Integer.parseInt(properties[Fish.FISH_ACTION_PERIOD]),
                    imageStore.getImageList(Fish.FISH_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Fish.FISH_NUM_PROPERTIES;
    }
    /**
     * draw Atlantis when program starts
     * @param properties properties of the Atlantis
     * @param imageStore contains Atlantis image
     * @return true if successful, false if not
     */
    private boolean parseAtlantis(String [] properties,
                                        ImageStore imageStore)
    {
        if (properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Atlantis.ATLANTIS_COL]),
                    Integer.parseInt(properties[Atlantis.ATLANTIS_ROW]));
            Entity entity = Atlantis.createAtlantis(properties[Atlantis.ATLANTIS_ID],
                    pt, imageStore.getImageList(Atlantis.ATLANTIS_KEY));
            tryAddEntity(entity);
        }

        return properties.length == Atlantis.ATLANTIS_NUM_PROPERTIES;
    }
    /**
     * draw Sgrass when program starts
     * @param properties properties of the Sgrass
     * @param imageStore contains sgrass image
     * @return true if successful, false if not
     */
    private boolean parseSgrass(String [] properties,
                                      ImageStore imageStore)
    {
        if (properties.length == SGrass.SGRASS_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SGrass.SGRASS_COL]),
                    Integer.parseInt(properties[SGrass.SGRASS_ROW]));
            Entity entity = SGrass.createSgrass(properties[SGrass.SGRASS_ID],
                    pt,
                    Integer.parseInt(properties[SGrass.SGRASS_ACTION_PERIOD]),
                    imageStore.getImageList(SGrass.SGRASS_KEY));
            tryAddEntity(entity);
        }

        return properties.length == SGrass.SGRASS_NUM_PROPERTIES;
    }

    /**
     * Parse things in order
     * @param line tells the method which image to parse
     * @param imageStore access to the images
     * @return true if successful, false if not
     */
    private boolean processLine(String line,
                                      ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0)
        {
            switch (properties[PROPERTY_KEY])
            {
                case Background.BGND_KEY:
                    return parseBackground(properties, imageStore);
                case Octo.OCTO_KEY:
                    return parseOcto(properties,  imageStore);
                case Obstacle.OBSTACLE_KEY:
                    return parseObstacle(properties, imageStore);
                case Fish.FISH_KEY:
                    return parseFish(properties, imageStore);
                case Atlantis.ATLANTIS_KEY:
                    return parseAtlantis(properties, imageStore);
                case SGrass.SGRASS_KEY:
                    return parseSgrass(properties, imageStore);
            }
        }

        return false;
    }

    /**
     * Call process line and parse things in when we open the program
     * @param in read the line that is later used to process & parse image
     * @param imageStore access to images
     */
    public void load(Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(String.format("invalid entry on line %d",
                        lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(String.format("issue on line %d: %s",
                        lineNumber, e.getMessage()));
            }
            lineNumber++;
        }
    }

    /**
     * Given a list of entity, find the one with the nearest location
     * @param entities list of entities
     * @param pos position of this entity
     * @return the instance of the nearest entity
     */
    private Optional<Entity> nearestEntity(List<Entity> entities,
                                           Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = pos.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = pos.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    /**
     *
     * @param pos a position within the bound
     * @return an entity in that position or an empty instance
     */
    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -Fish.FISH_REACH; dy <= Fish.FISH_REACH; dy++) {
            for (int dx = -Fish.FISH_REACH; dx <= Fish.FISH_REACH; dx++) {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (withinBounds(newPt) &&
                        !isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * get all available instance of an entity
     * @param pos current position of an object
     * @return return the instance of entity (of the type we are looking for) closest to our point
     */
    public Optional<Entity> findNearest(Point pos, Class kind) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities) {
            if (entity.getClass().equals(kind)) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */

    /**
     * check if the position is within bounds, & set Occupancy cell
     * @param entity entity that we want to add
     */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    /**
     * if new position is different than the old one & it is within bounds, move the entity
     * @param entity the entity we want to move
     * @param pos new position
     */
    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    /**
     * calls on method removeEntityAt to remove the entity at their position
     * @param entity the entity we want to remove
     * @return
     */
    public boolean removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
        return true;
    }

    /**
     * remove entity if they exists
     * @param pos
     */
    private void removeEntityAt(Point pos) {
        if (withinBounds(pos)
                && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }
    //end moved

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    Entity getOccupancyCell(Point pos) {
        return occupancy[pos.getY()][pos.getX()];
    }

    void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }


    public void setBackground(Point pos, Background background) {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    private void setBackgroundCell(Point pos, Background background) {
        this.background[pos.getY()][pos.getX()] = background;
    }


}
