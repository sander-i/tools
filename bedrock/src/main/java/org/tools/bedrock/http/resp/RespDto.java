package org.tools.bedrock.http.resp;

import org.tools.bedrock.http.HttpEnum;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 12:04
 * @description 响应模型
 * @scope response
 */
public class RespDto {

    /*** 错误码 */
    private Integer code;
    /*** 响应状态 */
    private String status;
    /*** 提示信息 */
    private String msg;
    /*** 具体内容 */
    private Object data;
    /*** 单例模式 */
    private static RespDto instance;

    /**
     * 私有无参构造方法
     */
    private RespDto() {
        /* 服务器内部错误 */
        this.setInstance(HttpEnum.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * 私有有参构造方法
     *
     * @param httpEnum 响应枚举
     * @param data     响应数据
     */
    private RespDto(HttpEnum httpEnum, Object data) {
        this.setInstance(httpEnum, data);
    }

    /**
     * 写入参数
     *
     * @param httpEnum 响应枚举
     * @param data     响应数据
     */
    private void setInstance(HttpEnum httpEnum, Object data) {
        this.code = httpEnum.getCode();
        this.status = httpEnum.getStatus();
        this.msg = httpEnum.getMsg();
        this.data = data;
    }

    /**
     * 重新赋值
     */
    private static void deassign() {
        instance = new RespDto();
    }

    /**
     * 重新赋值
     *
     * @param httpEnum 响应枚举
     * @param data     响应数据
     */
    private static void deassign(HttpEnum httpEnum, Object data) {
        instance = new RespDto(httpEnum, data);
    }

    /**
     * 单例模式-单检索
     *
     * @return RespDto  响应体
     */
    public static RespDto getInstance() {
        if (instance == null) {
            synchronized (RespDto.class) {
                instance = new RespDto();
            }
        } else {
            deassign();
        }
        return instance;
    }

    /**
     * 单例模式-单检索
     *
     * @param data 响应数据
     * @return RespDto  响应体
     */
    public static RespDto getInstance(Object data) {
        if (instance == null) {
            synchronized (RespDto.class) {
                instance = new RespDto(HttpEnum.OK, data);
            }
        } else {
            deassign(HttpEnum.OK, data);
        }
        return instance;
    }

    /**
     * 单例模式-单检索
     *
     * @param httpEnum 响应枚举
     * @param data     响应数据
     * @return RespDto  响应体
     */
    public static RespDto getInstance(HttpEnum httpEnum, Object data) {
        if (instance == null) {
            synchronized (RespDto.class) {
                instance = new RespDto(httpEnum, data);
            }
        } else {
            deassign(httpEnum, data);
        }
        return instance;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static void setInstance(RespDto instance) {
        RespDto.instance = instance;
    }

    @Override
    public String toString() {
        return "RespDto{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
