package brigadier_legacy.argument;

import brigadier_legacy.ext.ICommandSenderExtension;
import brigadier_legacy.utils.Util;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector3d;

@ExtensionMethod(ICommandSenderExtension.class)
@EqualsAndHashCode
public class LookingPosArgument implements PosArgument {

    public static final char CARET = '^';

    private final double x;
    private final double y;
    private final double z;

    public LookingPosArgument(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3d getPos(ICommandSender source) {
        Vector2f vec2f = source.getRotation();
        Vector3d vec3d = source.getEntityAnchor()
            .positionAt(source);
        float f = MathHelper.cos((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float g = MathHelper.sin((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(-vec2f.x * (float) (Math.PI / 180.0));
        float i = MathHelper.sin(-vec2f.x * (float) (Math.PI / 180.0));
        float j = MathHelper.cos((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        float k = MathHelper.sin((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        Vector3d vec3d2 = new Vector3d((double) (f * h), (double) i, (double) (g * h));
        Vector3d vec3d3 = new Vector3d((double) (f * j), (double) k, (double) (g * j));
        Vector3d vec3d4 = vec3d2.cross(vec3d3, new Vector3d())
            .mul(-1.0);
        double d = vec3d2.x * this.z + vec3d3.x * this.y + vec3d4.x * this.x;
        double e = vec3d2.y * this.z + vec3d3.y * this.y + vec3d4.y * this.x;
        double l = vec3d2.z * this.z + vec3d3.z * this.y + vec3d4.z * this.x;
        return new Vector3d(vec3d.x + d, vec3d.y + e, vec3d.z + l);
    }

    @Override
    public Vector2f getRot(ICommandSender source) {
        return Util.ZERO_2F;
    }

    @Override
    public boolean isXRelative() {
        return true;
    }

    @Override
    public boolean isYRelative() {
        return true;
    }

    @Override
    public boolean isZRelative() {
        return true;
    }

    public static LookingPosArgument parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        double d = readCoordinate(reader, i);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            double e = readCoordinate(reader, i);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                double f = readCoordinate(reader, i);
                return new LookingPosArgument(d, e, f);
            } else {
                reader.setCursor(i);
                throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
            }
        } else {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
    }

    private static double readCoordinate(StringReader reader, int startingCursorPos) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw CoordinateArgument.MISSING_COORDINATE.createWithContext(reader);
        } else if (reader.peek() != '^') {
            reader.setCursor(startingCursorPos);
            throw Vec3ArgumentType.MIXED_COORDINATE_EXCEPTION.createWithContext(reader);
        } else {
            reader.skip();
            return reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0;
        }
    }

}
