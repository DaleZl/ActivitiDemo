package cn.dale.activitidemo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 申请状态枚举
 * @Author: dale
 * @Date: 2019/11/6 15:12
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplyStatusEnum {
    YES(1, "同意"), NO(0, "拒绝"), AUDITING(3, "审核中");

    private int value;

    private String name;

    ApplyStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static ApplyStatusEnum getItem(@JsonProperty("value") int value) {
        for (ApplyStatusEnum item : values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
