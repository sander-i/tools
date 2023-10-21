package org.tools.bedrock.uniqueid.uuid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sander
 * @version 1.0
 * @date 2023/09/11 16:19
 * @description uuid 工具
 * @scope bedrock
 */
public class UniqueUtils {

    /**
     * 获取uuid
     *
     * @return StringBuilder
     */
    public StringBuilder getUnique() {
        return new StringBuilder(String.valueOf(UUID.randomUUID()));
    }

    /**
     * 获取多个uuid
     *
     * @param size 数量
     * @return List<StringBuilder>
     */
    public List<StringBuilder> getUnique(Integer size) {
        List<StringBuilder> uuidList = new ArrayList<>();
        while (0 <= size--) {
            uuidList.add(uuidFormat(getUnique()));
        }
        return uuidList;
    }

    /**
     * 获取格式化后的uuid
     *
     * @return StringBuilder
     */
    public StringBuilder getUniqueFormatPrint() {
        return uuidFormat(getUnique());
    }

    /**
     * 获取格式化后的多个uuid
     *
     * @param size 数量
     * @return List<StringBuilder>
     */
    public List<StringBuilder> getUniqueFormatPrint(Integer size) {
        List<StringBuilder> uuidList = new ArrayList<>();
        while (0 <= size--) {
            uuidList.add(getUnique());
        }
        return uuidList;
    }

    /**
     * 获取uuid
     *
     * @return String
     */
    public String getUniqueStr() {
        return getUnique().toString();
    }

    /**
     * 获取多个uuid
     *
     * @param size 数量
     * @return List<String>
     */
    public List<String> getUniqueStr(Integer size) {
        List<String> uuidList = new ArrayList<>();
        while (0 <= size--) {
            uuidList.add(getUnique().toString());
        }
        return uuidList;
    }

    /**
     * 获取格式化后的uuid
     *
     * @return String
     */
    public String getUniqueFormatPrintStr() {
        return getUniqueFormatPrint().toString();
    }

    /**
     * 获取格式化后的多个uuid
     *
     * @param size 数量
     * @return List<String>
     */
    public List<String> getUniqueFormatPrintStr(Integer size) {
        List<String> uuidList = new ArrayList<>();
        while (0 <= size--) {
            uuidList.add(uuidFormat(getUnique()).toString());
        }
        return uuidList;
    }

    /**
     * uuid 格式化
     *
     * @param uuid 唯一键
     * @return StringBuilder
     */
    private StringBuilder uuidFormat(StringBuilder uuid) {
        return new StringBuilder(uuid.toString().replace("-", ""));
    }

}
