package org.tools.bedrock.http.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author sander
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class RQ<T> extends RR {

    private QD<T> queryData;

    private RQ(int code, String msg) {
        super(code, msg);
    }

    public static RR success() {
        return new RQ<>(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg());
    }

    public static RR success(int code, String msg) {
        return new RQ<>(code, msg);
    }

    public static RR success(RE re) {
        return new RQ<>(re.getCode(), re.getMsg());
    }

    private RQ(int code, String msg, QD<T> queryData) {
        super(code, msg);
        this.queryData = queryData;
    }

    public static <T> RQ<T> success(QD<T> queryData) {
        return new RQ<>(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg(), queryData);
    }

    public static <T> RQ<T> success(int code, String msg, QD<T> queryData) {
        return new RQ<>(code, msg, queryData);
    }

    public static <T> RQ<T> success(RE re, QD<T> queryData) {
        return new RQ<>(re.getCode(), re.getMsg(), queryData);
    }

    public static RR fail() {
        return new RQ<>(RE.SUCCESS.getCode(), RE.SUCCESS.getMsg());
    }

    public static RR fail(int code, String msg) {
        return new RQ<>(code, msg);
    }

    public static RR fail(RE re) {
        return new RQ<>(re.getCode(), re.getMsg());
    }

    public static <T> RQ<T> fail(QD<T> queryData) {
        return new RQ<>(RE.FAIL.getCode(), RE.FAIL.getMsg(), queryData);
    }

    public static <T> RQ<T> fail(int code, String msg, QD<T> queryData) {
        return new RQ<>(code, msg, queryData);
    }

    public static <T> RQ<T> fail(RE re, QD<T> queryData) {
        return new RQ<>(re.getCode(), re.getMsg(), queryData);
    }

}
