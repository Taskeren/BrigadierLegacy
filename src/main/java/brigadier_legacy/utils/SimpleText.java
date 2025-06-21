package brigadier_legacy.utils;

public class SimpleText implements Text {

    private final String message;

    public SimpleText(String message) {
        this.message = message;
    }

    @Override
    public String getString() {
        return message;
    }
}
