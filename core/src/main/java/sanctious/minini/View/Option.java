package sanctious.minini.View;

public class Option<T> {
    public final String displayText;
    public final T value;

    public Option(String displayText, T value) {
        this.displayText = displayText;
        this.value = value;
    }

    @Override
    public String toString() {
        return displayText;
    }
}

