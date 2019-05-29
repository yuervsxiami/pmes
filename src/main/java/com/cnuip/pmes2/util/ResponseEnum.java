package com.cnuip.pmes2.util;

/**
 * Create By Crixalis:
 * 2017年5月22日 下午1:47:06
 * 枚举业务逻辑异常
 */
public enum ResponseEnum {
    USER_LOGIN_PASSWORD(101, "您输入的密码错误"),
    USER_USERNAMEISEXIST(102, "该用户名已被占用"),
    USER_PHONEISEXIST(102, "该手机号已被占用"),
    USER_ISNOTEXIST(103, "该用户不存在"),
    USER_REGIST_ACCOUNT(104, "您未输入用户名"),
    USER_REGIST_PASSWORD(105, "您未输入密码"),
    USER_NEVERPAY(106, "您的用户等级不足或未购买课程"),
    USER_EMPTYPHONE(107, "手机号不能为空"),
    USER_EMPTYPASSWORD(108, "密码不能为空"),
    USER_WRONGOLDPASSWORD(109, "您输入的旧密码错误"),
    USER_NEVERACTIVATE(110, "您的账号从未激活,请前往激活"),
    USER_PASTDUEPAY(106, "您购买的用户等级已经过期,请重新激活"),
    USER_IOS_NEVERPAY(110, "您的账户需要激活,请前往激活"),
    USER_REGIST_IDNUMBER(112, "您未输入身份证号"),
    IOS_WRONG_RECEIPT(113, "错误的支付收据"),
    USER_IDNUMBERISEXIST(114, "该身份证号用户已存在"),
    USER_MISMATCH_ROLE(115, "该工单指派的用户和工单角色不匹配"),

    ROLE_ISNOTEXIST(121, "该角色已经不存在"),

    AUTH_HASSON(126, "该权限下有子权限,请删除子权限后再删除该权限"),
    ORGANIZATION_HASSON(127, "该组织机构下有子组织机构,请删除子组织机构后再删除该组织机构"),

    META_NOKEY(128, "未获取到该元数据的key,请检查之后重试"),
    META_UNIQUEKEY(129, "该元数据key已经被占用"),

    LABEL_NOKEY(130, "未获取到该标签的key,请检查之后重试"),
    LABEL_UNIQUEKEY(131, "该标签key已经被占用"),
    LABEL_HASUSERD(132, "该标签已经被使用,请取消环节下的标签"),

    LABELSET_NOTEXISTS(301, "该标签体系不存在"),
    LABELSET_COPY_ERROR(302, "标签体系生成新版本出错"),
    LABELSET_UPDATE_LABELS_ERROR(303, "标签体系更新标签出错"),
    LABELSET_UPDATE_STATE_ERROR(304, "更新标签体系状态出错"),
    LABELSET_MUST_HAVE_TYPE(305, "标签体系必须选择类型"),

    PROCESS_NOT_EXIST(400, "该流程不存在"),
    PROCESS_ADD_ERROR(401, "添加流程模版出错"),
    PROCESS_UPDATE_ERROR(402, "修改流程模版出错"),
    PROCESS_SET_LABELSET_ERROR(403, "设置流程模版标签体系出错"),
    PROCESS_SET_TIME_ERROR(404, "设置流程模版预警超时时间出错"),
    PROCESS_HAS_AVLICE_ORDER(405,"还有正在的定单,请等待所有定单执行完成后再禁用流程"),
    PROCESS_CANT_SET_LABELSET(406, "只有禁用状态下的流程才能调整标签体系"),

    PROCESSINSTANCE_NOTYPE(150, "未获取到您所选择流程类型"),

    TASK_ADD_ERROR(450, "添加环节出错"),
    TASK_UPDATE_ERROR(451, "添加环节标签出错"),
    TASK_SET_TIME_ERROR(452, "设置环节预警超时时间出错"),
    TASK_SET_ROLE_ERROR(453, "设置角色出错"),

    PATENT_ADD_ERROR(501, "添加专利出错"),
    PATENT_UPDATE_ERROR(502, "更新专利出错"),

    PROCESS_ORDER_ADD_ERROR(601, "添加定单出错"),
    PROCESS_ORDER_DELETE_ERROR(602, "删除定单出错"),
    PROCESS_ORDER_CHANGE_STATE_ERROR(603, "修改定单状态出错"),
    PROCESS_ORDER_BATCH_GET_ERROR(604, "批量流程批量获取出错"),
    PROCESS_ORDER_BATCH_CAL_ERROR(605, "批量流程批量计算出错"),
    PROCESS_ORDER_BATCH_INSERT_ERROR(607, "批量流程批量插入出错"),
    PROCESS_ORDER_ISEXIST(606, "该类型订单正在运行中,请等待完成之后再开启新的订单"),
    PROCESS_ORDER_FAIL_FLASHPHONE(608, "订单快照失败"),
    PROCESS_MISS_LABELSET(609, "找不到流程对应的标签体系,请检查流程是否绑定了标签体系或者标签体系是否启用"),
    PROCESS_NOT_ALIVE(610,"该流程未启用,请联系管理员启用流程"),

    TASK_ORDER_ADD_ERROR(651, "添加工单出错"),
    TASK_ORDER_DELETE_ERROR(652, "删除工单出错"),
    TASK_ORDER_CHANGE_STATE_ERROR(653, "修改工单状态出错"),
    TASK_ORDER_CHANGE_USER_ERROR(654, "修改工单操作人出错"),
    TASK_ORDER_ISNOT_YOURS(655, "您未拥有该工单的处理权限或者该工单已被别人处理"),
    TASK_ORDER_VALUEINDEX_ONLYPATNET(656, "只有专利才拥有价值标引环节"),
    TASK_ORDER_MISSMATING_PROCESS_TYPE(657, "流程类型与工单不匹配"),

    TASK_ORDER_LABEL_ADD_ERROR(701, "添加定单标签出错"),
    TASK_ORDER_LABEL_DELETE_ERROR(702, "删除定单标签出错"),
    TASK_ORDER_LABEL_UPDATE_ERROR(703, "修改定单标签出错"),

    ENTERPRISE_SAVE_ERROR(801, "添加企业信息出错"),
    ENTERPRISE_UPDATE_ERROR(802, "更新企业信息出错"),
    ENTERPRISE_DELETE_ERROR(803, "删除企业信息出错"),

    ENTERPRISE_REQUIREMENT_SAVE_ERROR(901, "添加企业需求出错"),
    ENTERPRISE_REQUIREMENT_UPDATE_ERROR(902, "更新企业需求出错"),
    ENTERPRISE_REQUIREMENT_DELETE_ERROR(903, "删除企业需求出错"),

    MATCH_SAVE_ERROR(1001, "添加匹配信息出错"),
    MATCH_UPDATE_ERROR(1002, "更新匹配信息出错"),
    MATCH_DELETE_ERROR(1003, "删除匹配信息出错"),
    MATCH_NOTGET_RESULT(1004, "未接受到筛选结果"),

    UPLOAD_FAIL(199, "文件上传失败"),
    UPLOAD_OVERSIZEFILE(200, "您上传的文件大小超出允许大小"),

    RESULT_NAME_NULL(2001,"成果名称不能为空"),
    RESULT_AREA_NULL(2002,"成果所属领域不能为空"),
    RESULT_INTRODUCE_NULL(2003,"成果介绍不能为空"),
    RESULT_MATURITY_NULL(2004,"成果成熟度不能为空"),
    RESULT_METHOD_NULL(2005,"成果合作方式不能为空")

    ;


    private int code;

    private String msg;

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
