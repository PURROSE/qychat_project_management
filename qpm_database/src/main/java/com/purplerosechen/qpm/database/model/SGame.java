package com.purplerosechen.qpm.database.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName s_game
 */
@TableName(value ="s_game")
@Data
public class SGame implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 
     */
    @TableField(value = "g_name")
    private String gName;

    /**
     * 
     */
    @TableField(value = "g_name_y")
    private String gNameY;

    /**
     * 
     */
    @TableField(value = "g_note")
    private String gNote;

    /**
     * 
     */
    @TableField(value = "g_create_time")
    private Date gCreateTime;

    /**
     * 是否启用
     */
    @TableField(value = "g_enable")
    private Integer gEnable;

    /**
     * 下载链接
     */
    @TableField(value = "g_url")
    private String gUrl;

    /**
     * 版本
     */
    @TableField(value = "g_bb")
    private String gBb;

    /**
     * 
     */
    @TableField(value = "g_pwd")
    private String gPwd;

    /**
     * 
     */
    @TableField(value = "g_other")
    private String gOther;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", gName=").append(gName);
        sb.append(", gNameY=").append(gNameY);
        sb.append(", gNote=").append(gNote);
        sb.append(", gCreateTime=").append(gCreateTime);
        sb.append(", gEnable=").append(gEnable);
        sb.append(", gUrl=").append(gUrl);
        sb.append(", gBb=").append(gBb);
        sb.append(", gPwd=").append(gPwd);
        sb.append(", gOther=").append(gOther);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}