package brigadier_legacy.utils;

import com.mojang.brigadier.Message;

public interface Text extends Message {

    static Text text(String message) {
        return new SimpleText(message);
    }

    static Text translatable(String translateKey) {
        return new TranslatableText(translateKey);
    }

    static Text translatable(String translateKey, Object... formats) {
        return new TranslatableText(translateKey, formats);
    }

    @SuppressWarnings("SpellCheckingInspection")
    static Text stringifiedTranslatable(String translateKey, Object... args) {
        for(int i = 0; i < args.length; i++) {
            Object object = args[i];
            if(!isPrimitive(object) && !(object instanceof Text)) {
                args[i] = String.valueOf(object);
            }
        }

        return translatable(translateKey, args);
    }

    static boolean isPrimitive(Object argument) {
        return argument instanceof Number || argument instanceof Boolean || argument instanceof String;
    }

}
