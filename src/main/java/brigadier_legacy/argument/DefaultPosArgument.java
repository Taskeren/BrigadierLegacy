package brigadier_legacy.argument;

import brigadier_legacy.ext.ICommandSenderExtension;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.EqualsAndHashCode;
import lombok.experimental.ExtensionMethod;
import net.minecraft.command.ICommandSender;
import org.joml.Vector2f;
import org.joml.Vector3d;

@ExtensionMethod(ICommandSenderExtension.class)
@EqualsAndHashCode
public class DefaultPosArgument implements PosArgument {

    private final CoordinateArgument x;
    private final CoordinateArgument y;
    private final CoordinateArgument z;

    public DefaultPosArgument(CoordinateArgument x, CoordinateArgument y, CoordinateArgument z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3d getPos(ICommandSender source) {
        Vector3d vec3d = source.getPosition();
        return new Vector3d(
            this.x.toAbsoluteCoordinate(vec3d.x),
            this.y.toAbsoluteCoordinate(vec3d.y),
            this.z.toAbsoluteCoordinate(vec3d.z));
    }

    @Override
    public Vector2f getRot(ICommandSender source) {
        Vector2f vec2f = source.getRotation();
        return new Vector2f((float) this.x.toAbsoluteCoordinate(vec2f.x), (float) this.y.toAbsoluteCoordinate(vec2f.y));
    }

    @Override
    public boolean isXRelative() {
        return this.x.isRelative();
    }

    @Override
    public boolean isYRelative() {
        return this.y.isRelative();
    }

    @Override
    public boolean isZRelative() {
        return this.z.isRelative();
    }

    public static DefaultPosArgument parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        CoordinateArgument coordinateArgument = CoordinateArgument.parse(reader);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            CoordinateArgument coordinateArgument2 = CoordinateArgument.parse(reader);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                CoordinateArgument coordinateArgument3 = CoordinateArgument.parse(reader);
                return new DefaultPosArgument(coordinateArgument, coordinateArgument2, coordinateArgument3);
            } else {
                reader.setCursor(i);
                throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
            }
        } else {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
    }

    public static DefaultPosArgument parse(StringReader reader, boolean centerIntegers) throws CommandSyntaxException {
        int i = reader.getCursor();
        CoordinateArgument coordinateArgument = CoordinateArgument.parse(reader, centerIntegers);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            CoordinateArgument coordinateArgument2 = CoordinateArgument.parse(reader, false);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                CoordinateArgument coordinateArgument3 = CoordinateArgument.parse(reader, centerIntegers);
                return new DefaultPosArgument(coordinateArgument, coordinateArgument2, coordinateArgument3);
            } else {
                reader.setCursor(i);
                throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
            }
        } else {
            reader.setCursor(i);
            throw Vec3ArgumentType.INCOMPLETE_EXCEPTION.createWithContext(reader);
        }
    }

    public static DefaultPosArgument absolute(double x, double y, double z) {
        return new DefaultPosArgument(
            new CoordinateArgument(false, x),
            new CoordinateArgument(false, y),
            new CoordinateArgument(false, z));
    }

    public static DefaultPosArgument absolute(Vector2f vec) {
        return new DefaultPosArgument(
            new CoordinateArgument(false, vec.x),
            new CoordinateArgument(false, vec.y),
            new CoordinateArgument(true, 0.0));
    }

}
