package com.cnuip.pmes2.domain.el;

import com.cnuip.pmes2.domain.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/16.
 * Time: 9:53
 */

@Data
public class User extends BaseModel {
    @ApiModelProperty(
            value = "ID系统自动生成",
            name = "id",
            dataType = "Long"
    )
    private Long id;
    @ApiModelProperty(
            value = "组织ID",
            name = "organizationId",
            dataType = "Long"
    )
    private Long organizationId;
    @ApiModelProperty(
            value = "组织",
            name = "organizationName",
            dataType = "String"
    )
    private String organizationName;
    @ApiModelProperty(
            value = "用户名",
            name = "username",
            dataType = "String",
            example = "admin"
    )
    private String username;
    @ApiModelProperty(
            value = "密码",
            name = "password",
            dataType = "String",
            example = "123456"
    )
    private String password;
    @ApiModelProperty(
            value = "姓名",
            name = "realName",
            dataType = "String",
            example = "张三"
    )
    private String realName;
    @ApiModelProperty(
            value = "昵称",
            name = "nickName",
            dataType = "String",
            example = "zhangdan"
    )
    private String nickName;
    @ApiModelProperty(
            value = "手机号",
            name = "phone",
            dataType = "String",
            example = "18000000000"
    )
    private String phone;
    @ApiModelProperty(
            value = "性别",
            name = "sex",
            dataType = "String",
            example = "男"
    )
    private String sex;
    @ApiModelProperty(
            value = "民族",
            name = "nation",
            dataType = "String",
            example = "汉族"
    )
    private String nation;
    @ApiModelProperty(
            value = "籍贯",
            name = "nativePlace",
            dataType = "String",
            example = "江苏南京"
    )
    private String nativePlace;
    @ApiModelProperty(
            value = "身份证号",
            name = "idCardNo",
            dataType = "String"
    )
    private String idCardNo;
    @ApiModelProperty(
            value = "出生日期",
            name = "birthday",
            dataType = "java.util.Date"
    )
    private Date birthday;
    @ApiModelProperty(
            value = "绑定微信号",
            name = "wchat",
            dataType = "String"
    )
    private String wchat;
    @ApiModelProperty(
            value = "证件照",
            name = "imageUrl",
            dataType = "String",
            example = "http://xxx.com/xxx.png"
    )
    private String imageUrl;
    @ApiModelProperty(
            value = "职称与头衔",
            name = "title",
            dataType = "String",
            example = "教授"
    )
    private String title;
    @ApiModelProperty(
            value = "部门ID",
            name = "departmentId",
            dataType = "Long"
    )
    private Long departmentId;
    @ApiModelProperty(
            value = "部门名称",
            name = "departmentName",
            dataType = "String"
    )
    private String departmentName;
    @ApiModelProperty(
            value = "岗位ID",
            name = "postId",
            dataType = "Long"
    )
    private Long postId;
    @ApiModelProperty(
            value = "岗位名称",
            name = "postName",
            dataType = "String"
    )
    private String postName;
    @ApiModelProperty(
            value = "职权ID",
            name = "powersId",
            dataType = "Long"
    )
    private Long powersId;
    @ApiModelProperty(
            value = "职权名称",
            name = "powersName",
            dataType = "String"
    )
    private String powersName;
    @ApiModelProperty(
            value = "科研方向",
            name = "direction",
            dataType = "String",
            example = "航天航空,飞机发动机,火星探测器"
    )
    private String direction;
    @ApiModelProperty(
            value = "简介",
            name = "introduction",
            dataType = "String",
            example = "又名南京大学医学院附属鼓楼医院"
    )
    private String introduction;
    @ApiModelProperty(
            value = "荣誉",
            name = "honor",
            dataType = "String"
    )
    private String honor;
    @ApiModelProperty(
            value = "是否删除 [ YES.是 NO.否 ]",
            name = "isDelete",
            dataType = "String",
            example = "NO"
    )
    private String isDelete;
    @ApiModelProperty(
            value = "操作人ID",
            name = "editorId",
            dataType = "Long"
    )
    private Long editorId;
    @ApiModelProperty(
            value = "操作人",
            name = "editorName",
            dataType = "String",
            example = "admin"
    )
    private String editorName;
    @ApiModelProperty(
            value = "创建日期",
            name = "createdTime",
            dataType = "java.util.Date"
    )
    private Date createdTime;
    @ApiModelProperty(
            value = "更新日期",
            name = "updatedTime",
            dataType = "java.util.Date"
    )
    private Date updatedTime;
}
