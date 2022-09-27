package cn.ponfee.scheduler.core.base;

import cn.ponfee.scheduler.common.base.model.CodeMsg;

/**
 * Defined code message for scheduler job
 *
 * @author Ponfee
 */
public enum JobCodeMsg implements CodeMsg {

    LOAD_HANDLER_ERROR(1000, "Load job handler error."),
    SPLIT_JOB_FAILED(1001, "Split job failed."),
    WORKER_NOT_FOUND(1002, "Cannot found worker group."),
    GROUP_NOT_MATCH(1003, "Group not match."),
    DISPATCH_TASK_FAILED(1004, "Dispatch task failed."),

    JOB_EXECUTE_FAILED(2001, "Job execute failed."),
    ;

    private final int code;
    private final String msg;

    JobCodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
