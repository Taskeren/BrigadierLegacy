package brigadier_legacy.argument;

import brigadier_legacy.CommandManager;
import brigadier_legacy.CommandSource;
import brigadier_legacy.ext.ICommandSenderExtension;
import brigadier_legacy.utils.Text;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.experimental.ExtensionMethod;
import net.minecraft.command.ICommandSender;
import org.joml.Vector3d;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@ExtensionMethod(ICommandSenderExtension.class)
public class Vec3ArgumentType implements ArgumentType<PosArgument> {

    private static final Collection<String> EXAMPLES = Arrays.asList(
        "0 0 0",
        "~ ~ ~",
        "^ ^ ^",
        "^1 ^ ^-5",
        "0.1 -0.5 .9",
        "~0.5 ~1 ~-5");

    public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable(
        "argument.pos3d.incomplete"));
    public static final SimpleCommandExceptionType MIXED_COORDINATE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable(
        "argument.pos.mixed"));

    private final boolean centerIntegers;

    public Vec3ArgumentType(boolean centerIntegers) {
        this.centerIntegers = centerIntegers;
    }

    public static Vec3ArgumentType vec3() {
        return new Vec3ArgumentType(true);
    }

    public static Vec3ArgumentType vec3(boolean centerIntegers) {
        return new Vec3ArgumentType(centerIntegers);
    }

    public static Vector3d getVec3(CommandContext<ICommandSender> context, String name) {
        return context.<PosArgument>getArgument(name, PosArgument.class)
            .getPos(context.getSource());
    }

    public static PosArgument getPosArgument(CommandContext<ICommandSender> context, String name) {
        return context.getArgument(name, PosArgument.class);
    }

    public PosArgument parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.canRead() && stringReader.peek() == '^'
            ? LookingPosArgument.parse(stringReader)
            : DefaultPosArgument.parse(stringReader, this.centerIntegers);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof ICommandSender)) {
            return Suggestions.empty();
        } else {
            String string = builder.getRemaining();
            Collection<CommandSource.RelativePosition> collection;
            if (!string.isEmpty() && string.charAt(0) == '^') {
                collection = Collections.singleton(CommandSource.RelativePosition.ZERO_LOCAL);
            } else {
                collection = ((ICommandSender) context.getSource()).getPositionSuggestions();
            }

            return CommandSource.suggestPositions(
                string,
                collection,
                builder,
                CommandManager.getCommandValidator(this::parse));
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
