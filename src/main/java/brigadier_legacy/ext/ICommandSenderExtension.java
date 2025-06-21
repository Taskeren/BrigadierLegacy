package brigadier_legacy.ext;

import brigadier_legacy.CommandSource;
import brigadier_legacy.argument.EntityAnchorArgumentType;
import brigadier_legacy.utils.Util;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import java.util.Collection;
import java.util.Collections;

import static brigadier_legacy.utils.Util.ZERO_2F;
import static brigadier_legacy.utils.Util.ZERO_3D;

public class ICommandSenderExtension {

    @Nullable
    public static Entity getEntity(ICommandSender sender) {
        if (sender instanceof EntityPlayer player) {
            return player;
        }
        return null;
    }

    public static Vector2f getRotation(ICommandSender sender) {
        if (sender instanceof EntityPlayer player) {
            return new Vector2f(player.rotationYaw, player.rotationPitch);
        }
        return ZERO_2F;
    }

    public static Vector3d getPosition(ICommandSender sender) {
        if (sender instanceof EntityPlayer player) {
            return Util.getPlayerPosition(player);
        }
        return ZERO_3D;
    }

    public static EntityAnchorArgumentType.EntityAnchor getEntityAnchor(ICommandSender sender) {
        return EntityAnchorArgumentType.EntityAnchor.FEET;
    }

    public static Collection<CommandSource.RelativePosition> getPositionSuggestions(ICommandSender sender) {
        return Collections.singleton(CommandSource.RelativePosition.ZERO_WORLD);
    }

    public static Collection<CommandSource.RelativePosition> getBlockPositionSuggestions(ICommandSender sender) {
        return Collections.singleton(CommandSource.RelativePosition.ZERO_WORLD);
    }

}
