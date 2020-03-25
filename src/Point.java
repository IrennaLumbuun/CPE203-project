final class Point {
    private final int x;
    private final int y;

    public double f;
    public double g;
    public double h;

    Point parent;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getF() {
        return f;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public Point getParent() {
        return parent;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point &&
                ((Point) other).x == this.x &&
                ((Point) other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }
    public double computeF(Point thisPoint, Point parentOfNext,  Point destination){
        double fVal = computeG(thisPoint, parentOfNext) + computeH(this, destination);
        this.f = fVal;
        return fVal;
    }

    /**
     * @return the distance from this point to parent + parent to Start
     */
    private double computeG(Point startPoint, Point parentOfNext){
        double gVal;
        if (Movable.adjacent(startPoint, this)) {
            gVal = Math.sqrt(Math.pow(this.x - startPoint.x, 2) + Math.pow(this.y - startPoint.y, 2));
            this.g = gVal;
        } else{
            gVal = parentOfNext.g + Math.sqrt(Math.pow(this.x - parentOfNext.x, 2) + Math.pow(this.y - parentOfNext.y, 2));
            this.g = gVal;
        }
        return gVal;
    }

    /**
     * @return potential nextPoint distance to destination (using manhattan estimation)
     */
    double computeH(Point nextPoint, Point destination){
        double verDistance = Math.abs(destination.y - nextPoint.y);
        double horDistance = Math.abs(destination.x - nextPoint.x);
        double h = verDistance + horDistance;
        return h;
    }
}

