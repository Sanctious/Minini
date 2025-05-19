package sanctious.minini.Models;

public class ViewResult {
    private final boolean status;
    private final String message;

    public ViewResult(String message, boolean status) {
        this.status = status;
        this.message = message;
    }

    public static ViewResult success(String message){
        return new ViewResult(message, true);
    }

    public static ViewResult failure(String message){
        return new ViewResult(message, false);
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
