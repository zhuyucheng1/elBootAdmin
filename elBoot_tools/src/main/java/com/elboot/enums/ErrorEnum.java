package com.elboot.enums;

/**
 * @Author zhuyuc
 * @Date by 2019/2/16
 * @Company:亚银网络科技有限公司
 * @Description:错误枚举
 */
public enum ErrorEnum {

    SUCCESS(200, "ok"),
    SERVER_ERROR(500, "服务器发生错误"),
    param_is_null(10001, "参数为空"),
    img_is_null(10002, "无效图片"),
    upload_is_err(10003, "上传失败"),
    number_format_err(10004, "数据类型转换错误"),
    PROCCESS_NODE_ERR(10005, "流程节点未配置"),
    PROCCESS_ERR(10006, "流程未配置"),
    app_auth_err(10007,"账号权限有误!"),
    img_type_err(10008,"图片类型有误!"),
    ;
    private String message;
    private int code;
    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
