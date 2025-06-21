package brigadier_legacy.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import org.joml.Vector2f;
import org.joml.Vector3d;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class Util {

    public static final Vector2f ZERO_2F = new Vector2f(0.0F, 0.0F);
    public static final Vector3d ZERO_3D = new Vector3d(0.0F, 0.0F, 0.0F);

    public static <T> T make(T value, Consumer<T> applier) {
        applier.accept(value);
        return value;
    }

    public static Vector3d getPlayerPosition(EntityPlayer entity) {
        return new Vector3d(
            entity.posX,
            entity.posY + (entity.getEyeHeight() - entity.getDefaultEyeHeight()),
            entity.posZ);
    }

    public static <T> ToIntFunction<T> lastIndexGetter(List<T> values) {
        int i = values.size();
        if (i < 8) {
            return values::indexOf;
        } else {
            Object2IntMap<T> object2IntMap = new Object2IntOpenHashMap<>(i);
            object2IntMap.defaultReturnValue(-1);

            for (int j = 0; j < i; j++) {
                object2IntMap.put(values.get(j), j);
            }

            return object2IntMap;
        }
    }
}
