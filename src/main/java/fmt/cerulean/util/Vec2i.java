package fmt.cerulean.util;

public record Vec2i(int x, int z) {
    public int distSqr(Vec2i other) {
        int dx = this.x - other.x;
        int dy = this.z - other.z;
        return dx * dx + dy * dy;
    }
    public long distSqrJ(Vec2i other) {
        long dx = this.x - other.x;
        long dy = this.z - other.z;
        return dx * dx + dy * dy;
    }
    public int magSqr() {
        return this.x * this.x + this.z * this.z;
    }
    public int y() {
        return z;
    }
    public static Vec2i of(int x, int z) {
        return new Vec2i(x, z);
    }
}
