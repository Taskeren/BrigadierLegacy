package brigadier_legacy.test;

import brigadier_legacy.argument.EnumArgumentType;
import brigadier_legacy.utils.Test;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.ICommandSender;

@Test
public class ExampleEnumArgumentType extends EnumArgumentType<ExampleEnum> {

    private ExampleEnumArgumentType() {
        super(ExampleEnum.CODEC, ExampleEnum::values);
    }

    public static ExampleEnumArgumentType exampleEnum() {
        return new ExampleEnumArgumentType();
    }

    public static ExampleEnum getExampleEnum(CommandContext<ICommandSender> context, String id) {
        return context.getArgument(id, ExampleEnum.class);
    }

}
