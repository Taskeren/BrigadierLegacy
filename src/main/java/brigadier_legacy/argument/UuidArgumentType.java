package brigadier_legacy.argument;

import brigadier_legacy.utils.Text;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.ICommandSender;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UuidArgumentType implements ArgumentType<UUID> {

    public static final SimpleCommandExceptionType INVALID_UUID = new SimpleCommandExceptionType(Text.translatable(
        "argument.uuid.invalid"));

    private static final Collection<String> EXAMPLES = List.of("dd12be42-52a9-4a91-a8a1-11c01849e498");
    private static final Pattern VALID_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");

    public static UUID getUuid(CommandContext<ICommandSender> context, String name) {
        return context.getArgument(name, UUID.class);
    }

    public static UuidArgumentType uuid() {
        return new UuidArgumentType();
    }

    public UUID parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.getRemaining();
        Matcher matcher = VALID_CHARACTERS.matcher(string);
        if (matcher.find()) {
            String string2 = matcher.group(1);

            try {
                UUID uUID = UUID.fromString(string2);
                stringReader.setCursor(stringReader.getCursor() + string2.length());
                return uUID;
            } catch (IllegalArgumentException ignored) {
            }
        }

        throw INVALID_UUID.createWithContext(stringReader);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
