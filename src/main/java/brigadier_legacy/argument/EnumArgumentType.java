package brigadier_legacy.argument;

import brigadier_legacy.CommandSource;
import brigadier_legacy.utils.StringIdentifiable;
import brigadier_legacy.utils.Text;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EnumArgumentType<T extends Enum<T> & StringIdentifiable> implements ArgumentType<T> {

    private static final DynamicCommandExceptionType INVALID_ENUM_EXCEPTION = new DynamicCommandExceptionType(value -> Text.stringifiedTranslatable(
        "argument.enum.invalid",
        value));

    private final Codec<T> codec;
    private final Supplier<T[]> valuesSupplier;

    protected EnumArgumentType(Codec<T> codec, Supplier<T[]> valuesSupplier) {
        this.codec = codec;
        this.valuesSupplier = valuesSupplier;
    }

    public T parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.readUnquotedString();
        return (T) this.codec.parse(JsonOps.INSTANCE, new JsonPrimitive(string))
            .result()
            .orElseThrow(() -> INVALID_ENUM_EXCEPTION.createWithContext(stringReader, string));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(
            Arrays.stream((Enum<?>[]) this.valuesSupplier.get())
                .map(enum_ -> ((StringIdentifiable) enum_).asString())
                .map(this::transformValueName)
                .collect(Collectors.toList()), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream((Enum<?>[]) this.valuesSupplier.get())
            .map(enum_ -> ((StringIdentifiable) enum_).asString())
            .map(this::transformValueName)
            .limit(2L)
            .collect(Collectors.toList());
    }

    protected String transformValueName(String name) {
        return name;
    }

}
