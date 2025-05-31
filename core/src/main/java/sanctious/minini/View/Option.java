package sanctious.minini.View;

public class Option<T> {
    private final String displayText;
    private final T value;

    public Option(String displayText, T value) {
        this.displayText = displayText;
        this.value = value;
    }

    @Override
    public String toString() {
        return displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public T getValue() {
        return value;
    }
}

