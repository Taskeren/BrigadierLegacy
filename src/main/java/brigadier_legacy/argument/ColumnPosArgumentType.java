package brigadier_legacy.argument;

import brigadier_legacy.CommandManager;
import brigadier_legacy.CommandSource;
import brigadier_legacy.ext.ICommandSenderExtension;
import brigadier_legacy.utils.BlockPos;
import brigadier_legacy.utils.ColumnPos;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@ExtensionMethod(ICommandSenderExtension.class)
public class ColumnPosArgumentType implements ArgumentType<PosArgument> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0");
    public static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("argument.pos2d.incomplete"));

    public static ColumnPosArgumentType columnPos() {
        return new ColumnPosArgumentType();
    }

    public static ColumnPos getColumnPos(CommandContext<ICommandSender> context, String name) {
        BlockPos blockPos = context.<PosArgument>getArgument(name, PosArgument.class).toAbsoluteBlockPos(context.getSource());
        return new ColumnPos(blockPos.x(), blockPos.z());
    }

    public PosArgument parse(StringReader stringReader) throws CommandSyntaxException {
        int i = stringReader.getCursor();
        if (!stringReader.canRead()) {
            throw INCOMPLETE_EXCEPTION.createWithContext(stringReader);
        } else {
            CoordinateArgument coordinateArgument = CoordinateArgument.parse(stringReader);
            if (stringReader.canRead() && stringReader.peek() == ' ') {
                stringReader.skip();
                CoordinateArgument coordinateArgument2 = CoordinateArgument.parse(stringReader);
                return new DefaultPosArgument(coordinateArgument, new CoordinateArgument(true, 0.0), coordinateArgument2);
            } else {
                stringReader.setCursor(i);
                throw INCOMPLETE_EXCEPTION.createWithContext(stringReader);
            }
        }
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
                collection = ((ICommandSender)context.getSource()).getBlockPositionSuggestions();
            }

            return CommandSource.suggestColumnPositions(string, collection, builder, CommandManager.getCommandValidator(this::parse));
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
