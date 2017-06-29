package api.exceptions;

public class HeaderMalformedException extends ApiException {

    private static final long serialVersionUID = -1344640670884805385L;

    public static final String DESCRIPTION = "Authorization header error due to wrong format, Auth Basic should be used";

    public static final int CODE = 3;

    public HeaderMalformedException() {
        this("");
    }

    public HeaderMalformedException(String detail) {
        super(DESCRIPTION + ". " + detail, CODE);
    }

}
