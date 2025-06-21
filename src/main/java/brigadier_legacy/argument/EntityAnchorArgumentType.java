package brigadier_legacy.argument;

import brigadier_legacy.CommandSource;
import brigadier_legacy.ext.ICommandSenderExtension;
import brigadier_legacy.ext.EntityExtension;
import brigadier_legacy.utils.Text;
import brigadier_legacy.utils.Util;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.experimental.ExtensionMethod;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@ExtensionMethod({ ICommandSenderExtension.class, EntityExtension.class })
public class EntityAnchorArgumentType implements ArgumentType<EntityAnchorArgumentType.EntityAnchor> {

    private static final Collection<String> EXAMPLES = Arrays.asList("eyes", "feet");

    private static final DynamicCommandExceptionType INVALID_ANCHOR_EXCEPTION = new DynamicCommandExceptionType(name -> Text.stringifiedTranslatable("argument.anchor.invalid",
        name));

    public static EntityAnchorArgumentType.EntityAnchor getEntityAnchor(CommandContext<ICommandSender> context,
        String name) {
        return context.getArgument(name, EntityAnchorArgumentType.EntityAnchor.class);
    }

    public static EntityAnchorArgumentType entityAnchor() {
        return new EntityAnchorArgumentType();
    }

    public EntityAnchorArgumentType.EntityAnchor parse(StringReader stringReader) throws CommandSyntaxException {
        int i = stringReader.getCursor();
        String string = stringReader.readUnquotedString();
        EntityAnchorArgumentType.EntityAnchor entityAnchor = EntityAnchorArgumentType.EntityAnchor.fromId(string);
        if (entityAnchor == null) {
            stringReader.setCursor(i);
            throw INVALID_ANCHOR_EXCEPTION.createWithContext(stringReader, string);
        } else {
            return entityAnchor;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(EntityAnchorArgumentType.EntityAnchor.ANCHORS.keySet(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public enum EntityAnchor {
        FEET("feet", (pos, entity) -> pos),
        EYES("eyes", (pos, entity) -> new Vector3d(pos.x, pos.y + (double) entity.getEyeHeight(), pos.z));

        static final Map<String, EntityAnchor> ANCHORS = Util.make(
            Maps.<String, EntityAnchorArgumentType.EntityAnchor>newHashMap(), map -> {
                for (EntityAnchorArgumentType.EntityAnchor entityAnchor : values()) {
                    map.put(entityAnchor.id, entityAnchor);
                }
            });
        private final String id;
        private final BiFunction<Vector3d, Entity, Vector3d> offset;

        EntityAnchor(final String id, final BiFunction<Vector3d, Entity, Vector3d> offset) {
            this.id = id;
            this.offset = offset;
        }

        @Nullable
        public static EntityAnchorArgumentType.EntityAnchor fromId(String id) {
            return ANCHORS.get(id);
        }

        public Vector3d positionAt(Entity entity) {
            return this.offset.apply(entity.getPos(), entity);
        }

        public Vector3d positionAt(ICommandSender source) {
            Entity entity = source.getEntity();
            return entity == null ? source.getPosition() : this.offset.apply(source.getPosition(), entity);
        }
    }

}
