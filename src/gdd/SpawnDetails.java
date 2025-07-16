package gdd;


public class SpawnDetails {
    private SpawnType type;
    private int x;
    private int y;
    private int count;
    private int spacing;

    public SpawnDetails(SpawnType type, int x, int y, int count, int spacing) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.count = count;
        this.spacing = spacing;
    }

    public SpawnType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCount() {
        return count;
    }

    public int getSpacing() {
        return spacing;
    }
}
