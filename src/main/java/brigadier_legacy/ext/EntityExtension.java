package brigadier_legacy.ext;

import net.minecraft.entity.Entity;
import org.joml.Vector3d;

public class EntityExtension {

    public static Vector3d getPos(Entity entity) {
        return new Vector3d(entity.posX, entity.posY, entity.posZ);
    }

}
