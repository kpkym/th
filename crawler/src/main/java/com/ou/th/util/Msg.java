package com.ou.th.util;

import lombok.Data;

/**
 * @author kpkym
 * Date: 2020-03-23 11:22
 */
@Data
public class Msg {
    private Integer code;
    private String message;
    private Object data;

    private Msg() {}

    public static Msg success() {
        Msg msg = new Msg();
        msg.code = 200;
        return msg;
    }

    public static Msg success(Object data) {
        Msg msg = success();
        msg.data = data;
        return msg;
    }
}
