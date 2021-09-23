package top.bento.blog.vo;

/**
 * Result error code
 */
public enum ErrorCode {
    INVALID_PARAMS_ERROR(10001, "Invalid Params Given"),
    ACCOUNT_PWD_NOT_EXIST(10002, "用户名或密码不存在"),
    ACCOUNT_ALREADY_EXIST(10003, "Account already exists"),
    INVALID_TOKEN_ERROR(10004, "Invalid Token Given"),
    EMPTY_TOKEN_ERROR(10005, "Empty Token Given"),
    NO_PERMISSION(70001, "无访问权限"),
    SESSION_TIME_OUT(90001, "会话超时"),
    USER_NOT_LOGIN(90002, "User haven't login yet"),
    ;

    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
