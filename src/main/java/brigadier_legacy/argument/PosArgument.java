package brigadier_legacy.argument;

import brigadier_legacy.utils.BlockPos;
import net.minecraft.command.ICommandSender;
import org.joml.Vector2f;
import org.joml.Vector3d;

public interface PosArgument {

    Vector3d getPos(ICommandSender sender);

    Vector2f getRot(ICommandSender sender);

    default BlockPos toAbsoluteBlockPos(ICommandSender sender) {
        return new BlockPos(this.getPos(sender));
    }

    boolean isXRelative();

    boolean isYRelative();

    boolean isZRelative();

}
