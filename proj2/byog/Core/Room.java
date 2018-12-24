package byog.Core;

public class Room {
    static final int minWidth = 2;
    static final int maxWidth = 6;
    static final int minHeight = 2;
    static final int maxHeight = 6;

    public int blX;
    public int blY;
    public int roomWidth;
    public int roomHeight;
    public boolean first;
    public boolean success;
    public Hall fromHall;

    public Room() {
        roomWidth = RandomUtils.uniform(Map.random, minWidth, maxWidth + 1);
        roomHeight = RandomUtils.uniform(Map.random, minHeight, maxHeight + 1);
        blX = RandomUtils.uniform(Map.random, Map.WIDTH / 2 - 5, Map.WIDTH / 2);
        blY = RandomUtils.uniform(Map.random, Map.HEIGHT / 3 - 5, Map.HEIGHT / 3);
        first = true;
        success = true;
    }
    public Room(Hall hall) {
        first = false;
        success = false;
        fromHall = hall;
        roomWidth = RandomUtils.uniform(Map.random, minWidth, maxWidth + 1);
        roomHeight = RandomUtils.uniform(Map.random, minHeight, maxHeight + 1);
        int dir = fromHall.direction;
        switch(dir) {
            case 0: {
                blX = RandomUtils.uniform(Map.random, fromHall.endX() - roomWidth + 1, fromHall.endX() + 1);
                blY = fromHall.endY() + 1;
                break;
            }
            case 1: {
                blX = fromHall.endX() + 1;
                blY = RandomUtils.uniform(Map.random, fromHall.endY() - roomHeight + 1, fromHall.endY() + 1);
                break;
            }
            case 2: {
                blX = RandomUtils.uniform(Map.random, fromHall.endX() - roomWidth + 1, fromHall.endX() + 1);
                blY = fromHall.endY() - roomHeight;
                break;
            }
            case 3: {
                blX = fromHall.endX() - roomWidth;
                blY = RandomUtils.uniform(Map.random, fromHall.endY() - roomHeight + 1, fromHall.endY() + 1);
                break;
            }
        }
        if (blX > 0 && blX + roomWidth < Map.WIDTH  - 1 && blY > 0 && blY + roomHeight < Map.HEIGHT - 1){
            success = true;
        }

    }
    public int trX() {
        return blX + roomWidth - 1;
    }
    public int trY() {
        return blY + roomHeight - 1;
    }
    public int centerX() {
        return (blX + trX()) / 2;
    }
    public int centerY() {
        return (blY + trY()) / 2;
    }
    public boolean interfere(Room room) {
        // need room for walls.
        return this.blX <= room.trX() + 2 && this.trX() >= room.blX - 2 &&
                this.blY <= room.trY() + 2 && this.trY() >= room.blY - 2;
    }
}
