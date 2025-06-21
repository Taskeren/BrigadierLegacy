package brigadier_legacy.utils;

import net.minecraft.util.StatCollector;

public class TranslatableText implements Text {

    private final String translateKey;
    private final Object[] formats;

    public TranslatableText(String translateKey) {
        this(translateKey, new Object[0]);
    }

    public TranslatableText(String translateKey, Object[] formats) {
        this.translateKey = translateKey;
        this.formats = formats;
    }

    @Override
    public String getString() {
        return StatCollector.translateToLocalFormatted(translateKey, formats);
    }
}
