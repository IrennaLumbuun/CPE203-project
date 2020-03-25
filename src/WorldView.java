import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView {
    private PApplet screen;
    private  WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                     int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }
    public Viewport getViewport() {return viewport;}

    private int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }


    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(this.viewport.getCol() + colDelta, 0,
                this.world.getNumCols() - this.viewport.getNumCols());
        int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
                this.world.getNumRows() - this.viewport.getNumRows());

        this.viewport.shift(newCol, newRow);
    }

    /**
     * update the image of Entity and Background
     * @param entity is the entity that we want the image of
     * @return the image of that entity
     */
    private PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background) entity).getImages()
                    .get(((Background) entity).getImageIndex());
        } else if (entity instanceof Entity) {
            return ((Entity) entity).getImages().get(((Entity) entity).getImageIndex());
        } else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }

    /**
     * get background Image based on our current position
     * @param pos our current position
     * @return background image
     */
    private Optional<PImage> getBackgroundImage(Point pos) {
        if (world.withinBounds(pos)) {
            return Optional.of(getCurrentImage(world.getBackgroundCell(pos)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Calls on getBackgroundImage(). This method updates the background.
     */
    private void drawBackground() {
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld(col, row);
                Optional<PImage> image = getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }

    /**
     * Calls on getCurrentImage to get the image of our current entity. This method makes the entity looks like it's moving.
     */
    private void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if (this.viewport.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
                this.screen.image(getCurrentImage(entity),
                        viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight);
            }
        }
    }

    /**
     * This method calls on drawBackground and drawEntities to continuously animate our object.
     */
    public void drawViewport() {
        drawBackground();
        drawEntities();
    }

}
