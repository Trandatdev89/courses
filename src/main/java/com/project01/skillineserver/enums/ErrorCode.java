package com.project01.skillineserver.enums;

public enum ErrorCode {

    UNAUTHOUCATED(401,"Tài khoản chưa được xác thực,Yêu cầu đăng nhập!"),
    BAD_REQUEST(400,"Dữ liệu chưa hợp lệ!"),
    USER_NOTFOUND(404,"User không tồn tại!"),
    PHONE_EXITSED(400,"SDT này đã tồn tại!"),
    PASSWORD_WRONG(400,"Mật khẩu sai.Hãy nhập lại!"),
    VERIFY_ACCOUNT(401,"Chưa xác thực tài khoan .Hãy xác thực tài khoản trong email!"),
    INVALID_TOKEN(401,"Token đã hết hiệu lực hoặc sai!"),
    USER_EXITSED(400,"Username này đã tồn tại.Vui lòng chọn tên đăng nhập khác"),
    UNAUTHOUZATED(403,"Bạn không có quyền truy cập vào tài nguyên này"),
    PRODUCT_NOT_MATCH(400,"Sản phẩm không phu hop !"),
    INVENTORY_NOT_ENOUGH(400,"The quantity of products in stock is not enough"),
    LECTURE_NOT_FOUND(404,"Khong co khoa hoc"),
    PASSWORD_NOT_MATCH(400,"Mật khẩu mới và mật khẩu nhập lại không khớp với nhau.Xin vui lòng thử lại !"),
    CATEGORY_NOTFOUND(404,"Danh muc không tồn tại !"),
    LIST_ID_EMPTY(400,"ID trong danh sach rong!"),
    COURSE_EMPTY(404,"Course is empty!"),
    COURSE_NOT_FOUND(404,"Course is not found!"),
    ACCOUNT_IS_LOGOUT(401,"Tài khoản của bạn đã logout!"),
    TOKEN_INVALID(401,"Token không hợp lệ hoac hết hạn.Vui lòng đăng nhap lai !"),
    PRODUCT_NOTFOUND(404,"Sản phẩm không tồn tại !");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
