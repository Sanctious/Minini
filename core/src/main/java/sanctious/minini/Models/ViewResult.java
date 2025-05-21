package sanctious.minini.Models;

public class ViewResult<T> {
    private final boolean status;
    private final T data;
    private final String message;

    public ViewResult(boolean status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ViewResult<T> success(T data){
        return new ViewResult<T>(true, data, null);
    }

    public static <T> ViewResult<T> failure(String message){
        return new ViewResult<T>(false, null, message);
    }

    public static ViewResult<Void> empty(boolean status) { return new ViewResult<>(status, null, null); }

    public boolean isSuccess(){
        return this.status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
