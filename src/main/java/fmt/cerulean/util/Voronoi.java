package fmt.cerulean.util;

import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

// Simple voronoi diagram
public final class Voronoi {
    private final long seed;
    private final ImprovedChunkRandom random = new ImprovedChunkRandom(0);

    public Voronoi(long seed) {
        this.seed = seed;
    }

    public long get(double x, double z) {
        int xStart = MathHelper.floor(x);
        int zStart = MathHelper.floor(z);

        long chosenColor = 0;
        double minDist = Double.MAX_VALUE;
        for (int x1 = -1; x1 <= 1; x1++) {
            for (int z1 = -1; z1 <= 1; z1++) {
                this.random.setPopulationSeed(this.seed, xStart + x1, zStart + z1);
                // [0.1, 0.9]
                double xCenter = (this.random.nextDouble(0.8) + 0.1) + xStart + x1;
                double zCenter = (this.random.nextDouble(0.8) + 0.1) + zStart + z1;

                double ax = x - xCenter;
                double az = z - zCenter;
                double dist = ax * ax + az * az;
                if (dist < minDist) {
                    minDist = dist;
                    chosenColor = this.random.nextLong();
                }
            }
        }

        return chosenColor;
    }

    // getCellPos(x / scale, z / scale, scale)
    public Vec2d getCellPos(double x, double z, double scale) {
        int xStart = MathHelper.floor(x);
        int zStart = MathHelper.floor(z);

        Vec2d vec = null;
        double minDist = Double.MAX_VALUE;

        for (int x1 = -1; x1 <= 1; x1++) {
            for (int z1 = -1; z1 <= 1; z1++) {
                this.random.setPopulationSeed(this.seed, xStart + x1, zStart + z1);
                // [0.1, 0.9]
                double cx = this.random.nextDouble(0.8) + 0.1;
                double cz = this.random.nextDouble(0.8) + 0.1;
                double xCenter = cx + xStart + x1;
                double zCenter = cz + zStart + z1;

                double ax = x - xCenter;
                double az = z - zCenter;
                double dist = ax * ax + az * az;
                if (dist < minDist) {
                    minDist = dist;
                    // Multiplying by the scale is needed to normalize the grid to the size that it's needed in
                    vec = new Vec2d(xCenter * scale, zCenter * scale);
                }
            }
        }

        return vec;
    }

    public Pair<Long, Long> get2(double x, double z) {
        int xStart = MathHelper.floor(x);
        int zStart = MathHelper.floor(z);

        long chosenColor = 0;
        long chosenColor2 = 0;
        double minDist = Double.MAX_VALUE;
        double minDist2 = Double.MAX_VALUE;

        for (int x1 = -1; x1 <= 1; x1++) {
            for (int z1 = -1; z1 <= 1; z1++) {
                this.random.setPopulationSeed(this.seed, xStart + x1, zStart + z1);
                // [0.1, 0.9]
                double xCenter = (this.random.nextDouble(0.8) + 0.1) + xStart + x1;
                double zCenter = (this.random.nextDouble(0.8) + 0.1) + zStart + z1;

                double ax = x - xCenter;
                double az = z - zCenter;
                double dist = ax * ax + az * az;
                if (dist < minDist) {
                    // Push below
                    chosenColor2 = chosenColor;
                    minDist2 = minDist;

                    minDist = dist;
                    chosenColor = this.random.nextLong();
                } else if (dist < minDist2) {
                    minDist2 = dist;
                    chosenColor2 = this.random.nextLong();
                }
            }
        }

        return new Pair<>(chosenColor, chosenColor2);
    }

    public Pair<Vec2d, Vec2d> getCellPos2(double x, double z, double scale) {
        int xStart = MathHelper.floor(x);
        int zStart = MathHelper.floor(z);

        Vec2d vec = null;
        Vec2d vec2 = null;
        double minDist = Double.MAX_VALUE;
        double minDist2 = Double.MAX_VALUE;

        for (int x1 = -1; x1 <= 1; x1++) {
            for (int z1 = -1; z1 <= 1; z1++) {
                this.random.setPopulationSeed(this.seed, xStart + x1, zStart + z1);
                // [0.1, 0.9]
                double cx = this.random.nextDouble(0.8) + 0.1;
                double cz = this.random.nextDouble(0.8) + 0.1;
                double xCenter = cx + xStart + x1;
                double zCenter = cz + zStart + z1;

                double ax = x - xCenter;
                double az = z - zCenter;
                double dist = ax * ax + az * az;

                if (dist < minDist) {
                    // Push below
                    vec2 = vec;
                    minDist2 = minDist;

                    minDist = dist;
                    // Multiplying by the scale is needed to normalize the grid to the size that it's needed in
                    vec = new Vec2d(xCenter * scale, zCenter * scale);
                } else if (dist < minDist2) {
                    minDist2 = dist;
                    vec2 = new Vec2d(xCenter * scale, zCenter * scale);
                }
            }
        }

        return new Pair<>(vec, vec2);
    }
}
