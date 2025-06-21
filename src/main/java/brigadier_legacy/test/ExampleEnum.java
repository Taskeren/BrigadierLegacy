package brigadier_legacy.test;

import brigadier_legacy.utils.StringIdentifiable;
import com.mojang.serialization.Codec;

public enum ExampleEnum implements StringIdentifiable {
    FOO,
    BAR,
    ;

    public static final Codec<ExampleEnum> CODEC = StringIdentifiable.createCodec(ExampleEnum::values);

    @Override
    public String asString() {
        return name();
    }
}
