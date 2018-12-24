package byog.Core;
public class Hall {
    private final int minLength = 2;
    private final int maxLength = 10;
    int startX;
    int startY;
    int length;
    // 0: north; 1: east; 2: south; 3: west
    int direction;
    boolean success;
    public Hall(Room fromRoom) {
        success = false;
        length = RandomUtils.uniform(Map.random, minLength, maxLength + 1);
        direction = RandomUtils.uniform(Map.random, 0, 4);
        if (fromRoom.first) {
            success = true;
        } else {
            if ((direction + fromRoom.fromHall.direction) % 2 == 0) {
                direction = (direction + 1) % 4;
            }
        }
        switch (direction) {
            case 0: {
                startX = RandomUtils.uniform(Map.random, fromRoom.blX, fromRoom.trX() + 1);
                startY = fromRoom.trY() + 1;
                if (startY + length < Map.HEIGHT - 1 - fromRoom.maxHeight) {
                    success = true;
                }
                break;
            }
            case 1: {
                startX = fromRoom.trX() + 1;
                startY = RandomUtils.uniform(Map.random, fromRoom.blY, fromRoom.trY() + 1);
                if (startX + length < Map.WIDTH - 1 - fromRoom.maxWidth) {
                    success = true;
                }
                break;
            }
            case 2: {
                startX = RandomUtils.uniform(Map.random, fromRoom.blX, fromRoom.trX() + 1);
                startY = fromRoom.blY - 1;
                if (startY - length > fromRoom.maxHeight) {
                    success = true;
                }
                break;
            }
            case 3: {
                startX = fromRoom.blX - 1;
                startY = RandomUtils.uniform(Map.random, fromRoom.blY, fromRoom.trY() + 1);
                if (startX - length > fromRoom.maxWidth) {
                    success = true;
                }
                break;
            }
            default: break;
        }
    }
    public int endX() {
        int endX = startX;
        switch (direction) {
            case 0: {
                break;
            }
            case 1: {
                endX += length - 1;
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                endX -= length - 1;
                break;
            }
            default: break;
        }
        return endX;
    }
    public int endY() {
        int endY = startY;
        switch (direction) {
            case 0: {
                endY += length - 1;
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                endY -= length - 1;
                break;
            }
            case 3: {
                break;
            }
            default: break;
        }
        return endY;
    }
}
