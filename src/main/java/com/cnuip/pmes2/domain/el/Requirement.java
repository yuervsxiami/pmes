package com.cnuip.pmes2.domain.el;

import com.cnuip.pmes2.domain.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2018/10/16.
 * Time: 16:23
 */
@Data
public class Requirement extends BaseModel {

    /** ID系统自动生成 */
    private Long id;

    /** 分类(必须到第三级) sys_ys_classify.id */

    private Long classifyId;

    /** sys_ys_classify.code */

    private String code;

    /** 需求类型(PROJECT_DECLARATION.项目申报 PATENT_APPLICATION.专利申请 INTELLECTUAL_PROPERTY_RIGHT.知识产权贯标 LEGAL_AID.法律援助 ANALYSIS_OF_PATENT_INFORMATION.专利情报分析 EVALUATION_OF_PATENT_VALUE.专利价值评估) */
    private String requirementType;

    /** 企业类型(HIGH_NEW_TECHNOLOGY.高新技术企业 INNOVATIVE.创新型企业 SCIENCE_TECHNOLOGY.科技型中小企业 PRIVATE_SCIENCE_TECHNOLOGY.民营科技企业 LARGE_MIDDLE_SIZED.大中型企业) */
    private String enterpriseType;

    /** 需求标题 */
    private String title;

    /** 标签 */
    private String label;

    /** 需求内容 */
    private String content;

    /** 回复内容 */
    private String replyContent;

    /** 留言人sys_ys_user.id */

    private Long userId;

    /** 留言人sys_ys_user.usernaername */
    private String username;

    /** 回复人sys_ys_user.id */

    private Long replyUserId;

    /** 店铺shp_hp_shop.id */

    private Long shopId;

    /** 留言日期 */
    private Date createdTime;

    /** 回复日期 */
    private Date updatedTime;

    /** 回复次数 */
    private Long number;
}
