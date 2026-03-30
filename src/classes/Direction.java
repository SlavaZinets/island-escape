package classes;

public class Direction {

    private int x;
    private int y;

    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
        normalize();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
        normalize();
    }

    public void setY(int y) {
        this.y = y;
        normalize();
    }

    public void setDirection(int x, int y) {
        this.x = x;
        this.y = y;
        normalize();
    }

    private void normalize() {
        Integer newX = this.x;
        Integer newY = this.y;

        this.x = newX.compareTo(0);
        this.y = newY.compareTo(0);
    }

}
