package brigadier_legacy.utils;

import org.joml.Vector2dc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class ColumnPos extends Vector2i {

    public ColumnPos() {
    }

    public ColumnPos(int x, int y) {
        super(x, y);
    }

    public ColumnPos(Vector2ic v) {
        super(v);
    }

    public ColumnPos(double x, double y, int mode) {
        super(x, y, mode);
    }

    public ColumnPos(Vector2dc v, int mode) {
        super(v, mode);
    }

    public BlockPos toBlockPos(int y) {
        return new BlockPos(this.x, y, this.y /* Z */);
    }

    public long pack() {
        return pack(this.x, this.y);
    }

    public static long pack(int x, int z) {
        return (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
    }

    public static int getX(long packed) {
        return (int) (packed & 4294967295L);
    }

    public static int getZ(long packed) {
        return (int) (packed >>> 32 & 4294967295L);
    }

    public static ColumnPos unpack(long packed) {
        return new ColumnPos(getX(packed), getZ(packed));
    }
}
