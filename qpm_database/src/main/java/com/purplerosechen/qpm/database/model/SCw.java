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
 * @TableName s_cw
 */
@TableName(value ="s_cw")
@Data
public class SCw implements Serializable {
    /**
     * 
     */
    @TableId(value = "cw_id")
    private Integer cwId;

    /**
     * 
     */
    @TableField(value = "auth_id")
    private String authId;

    /**
     * 
     */
    @TableField(value = "gj")
    private Integer gj;

    /**
     * 
     */
    @TableField(value = "fy")
    private Integer fy;

    /**
     * 
     */
    @TableField(value = "xl")
    private Integer xl;

    /**
     * 
     */
    @TableField(value = "jy")
    private Integer jy;

    /**
     * 
     */
    @TableField(value = "dj")
    private Integer dj;

    /**
     * 
     */
    @TableField(value = "tk")
    private Integer tk;

    /**
     * 
     */
    @TableField(value = "hj")
    private Integer hj;

    /**
     * 
     */
    @TableField(value = "ht")
    private Integer ht;

    /**
     * 
     */
    @TableField(value = "hb")
    private Integer hb;

    /**
     * 
     */
    @TableField(value = "wq")
    private Integer wq;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "jq")
    private Integer jq;

    /**
     * 
     */
    @TableField(value = "group_id")
    private String groupId;

    /**
     * 
     */
    @TableField(value = "cw_name")
    private String cwName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SCw other = (SCw) that;
        return (this.getCwId() == null ? other.getCwId() == null : this.getCwId().equals(other.getCwId()))
            && (this.getAuthId() == null ? other.getAuthId() == null : this.getAuthId().equals(other.getAuthId()))
            && (this.getGj() == null ? other.getGj() == null : this.getGj().equals(other.getGj()))
            && (this.getFy() == null ? other.getFy() == null : this.getFy().equals(other.getFy()))
            && (this.getXl() == null ? other.getXl() == null : this.getXl().equals(other.getXl()))
            && (this.getJy() == null ? other.getJy() == null : this.getJy().equals(other.getJy()))
            && (this.getDj() == null ? other.getDj() == null : this.getDj().equals(other.getDj()))
            && (this.getTk() == null ? other.getTk() == null : this.getTk().equals(other.getTk()))
            && (this.getHj() == null ? other.getHj() == null : this.getHj().equals(other.getHj()))
            && (this.getHt() == null ? other.getHt() == null : this.getHt().equals(other.getHt()))
            && (this.getHb() == null ? other.getHb() == null : this.getHb().equals(other.getHb()))
            && (this.getWq() == null ? other.getWq() == null : this.getWq().equals(other.getWq()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getJq() == null ? other.getJq() == null : this.getJq().equals(other.getJq()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCwName() == null ? other.getCwName() == null : this.getCwName().equals(other.getCwName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCwId() == null) ? 0 : getCwId().hashCode());
        result = prime * result + ((getAuthId() == null) ? 0 : getAuthId().hashCode());
        result = prime * result + ((getGj() == null) ? 0 : getGj().hashCode());
        result = prime * result + ((getFy() == null) ? 0 : getFy().hashCode());
        result = prime * result + ((getXl() == null) ? 0 : getXl().hashCode());
        result = prime * result + ((getJy() == null) ? 0 : getJy().hashCode());
        result = prime * result + ((getDj() == null) ? 0 : getDj().hashCode());
        result = prime * result + ((getTk() == null) ? 0 : getTk().hashCode());
        result = prime * result + ((getHj() == null) ? 0 : getHj().hashCode());
        result = prime * result + ((getHt() == null) ? 0 : getHt().hashCode());
        result = prime * result + ((getHb() == null) ? 0 : getHb().hashCode());
        result = prime * result + ((getWq() == null) ? 0 : getWq().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getJq() == null) ? 0 : getJq().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCwName() == null) ? 0 : getCwName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", cwId=").append(cwId);
        sb.append(", authId=").append(authId);
        sb.append(", gj=").append(gj);
        sb.append(", fy=").append(fy);
        sb.append(", xl=").append(xl);
        sb.append(", jy=").append(jy);
        sb.append(", dj=").append(dj);
        sb.append(", tk=").append(tk);
        sb.append(", hj=").append(hj);
        sb.append(", ht=").append(ht);
        sb.append(", hb=").append(hb);
        sb.append(", wq=").append(wq);
        sb.append(", createTime=").append(createTime);
        sb.append(", jq=").append(jq);
        sb.append(", groupId=").append(groupId);
        sb.append(", cwName=").append(cwName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}