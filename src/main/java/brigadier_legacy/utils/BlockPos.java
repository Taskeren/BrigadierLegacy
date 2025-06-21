package brigadier_legacy.utils;

import org.joml.RoundingMode;
import org.joml.Vector3dc;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class BlockPos extends Vector3i {

    public static final BlockPos ZERO = new BlockPos(0, 0, 0);

    public BlockPos() {}

    public BlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public BlockPos(Vector3ic v) {
        super(v);
    }

    public BlockPos(double x, double y, double z) {
        super(x, y, z, RoundingMode.FLOOR);
    }

    public BlockPos(Vector3dc v) {
        super(v, RoundingMode.FLOOR);
    }

    public BlockPos offset(int x, int y, int z) {
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }

}
