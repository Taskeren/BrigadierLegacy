package brigadier_legacy;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.function.Predicate;

public class CommandManager {

    @FunctionalInterface
    public interface CommandParser {
        void parse(StringReader reader) throws CommandSyntaxException;
    }

    public static Predicate<String> getCommandValidator(CommandParser parser) {
        return string -> {
            try {
                parser.parse(new StringReader(string));
                return true;
            } catch (CommandSyntaxException var3) {
                return false;
            }
        };
    }

}
