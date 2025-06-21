package brigadier_legacy.argument;

import brigadier_legacy.CommandSource;
import brigadier_legacy.utils.Text;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ColorArgumentType implements ArgumentType<EnumChatFormatting> {

    private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");
    public static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType(color -> Text.stringifiedTranslatable(
        "argument.color.invalid",
        color));

    private ColorArgumentType() {
    }

    public static ColorArgumentType color() {
        return new ColorArgumentType();
    }

    public static EnumChatFormatting getColor(CommandContext<ICommandSender> context, String name) {
        return context.getArgument(name, EnumChatFormatting.class);
    }

    public EnumChatFormatting parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.readUnquotedString();
        EnumChatFormatting formatting = EnumChatFormatting.getValueByName(string);
        if (formatting != null && !formatting.isFancyStyling()) {
            return formatting;
        } else {
            throw INVALID_COLOR_EXCEPTION.createWithContext(stringReader, string);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(EnumChatFormatting.getValidValues(true, false), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
