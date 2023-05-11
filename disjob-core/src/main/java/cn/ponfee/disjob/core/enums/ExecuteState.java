/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.disjob.core.enums;

import cn.ponfee.disjob.common.base.IntValueEnum;
import cn.ponfee.disjob.common.util.Enums;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The task executed state enum definition.
 * <p>mapped by sched_task.execute_state
 *
 * @author Ponfee
 */
public enum ExecuteState implements IntValueEnum<ExecuteState> {

    /**
     * 等待执行
     */
    WAITING(10, RunState.WAITING),

    /**
     * 正在执行
     */
    EXECUTING(20, RunState.RUNNING),

    /**
     * 暂停执行
     */
    PAUSED(30, RunState.PAUSED),

    /**
     * 正常完成
     */
    FINISHED(40, RunState.FINISHED),

    /**
     * 实例化失败取消
     */
    INSTANCE_FAILED(50, RunState.CANCELED),

    /**
     * 校验失败取消
     */
    VERIFY_FAILED(51, RunState.CANCELED),

    /**
     * 初始化异常取消
     */
    INIT_EXCEPTION(52, RunState.CANCELED),

    /**
     * 执行失败取消
     */
    EXECUTE_FAILED(53, RunState.CANCELED),

    /**
     * 执行异常取消
     */
    EXECUTE_EXCEPTION(54, RunState.CANCELED),

    /**
     * 执行超时取消
     */
    EXECUTE_TIMEOUT(55, RunState.CANCELED),

    /**
     * 执行冲突取消(sched_job.collision_strategy=3)
     */
    EXECUTE_COLLISION(56, RunState.CANCELED),

    /**
     * 手动取消
     */
    MANUAL_CANCELED(57, RunState.CANCELED),

    /**
     * 广播未执行取消(分配的worker机器消亡)
     */
    WAITING_CANCELED(58, RunState.CANCELED),
    ;

    /**
     * State list of can transit to PAUSED
     */
    public static final List<ExecuteState> PAUSABLE_LIST = ImmutableList.of(WAITING, EXECUTING);

    /**
     * State list of can transit to EXECUTING
     */
    public static final List<ExecuteState> EXECUTABLE_LIST = ImmutableList.of(WAITING, PAUSED);

    private static final Map<Integer, ExecuteState> MAPPING = Enums.toMap(ExecuteState.class, ExecuteState::value);

    private final int value;

    /**
     * mapped sched_instance.run_state
     */
    private final RunState runState;

    ExecuteState(int value, RunState runState) {
        this.value = value;
        this.runState = runState;
    }

    @Override
    public int value() {
        return value;
    }

    public boolean isTerminal() {
        return runState.isTerminal();
    }

    public boolean isFailure() {
        return runState.isFailure();
    }

    public RunState runState() {
        return runState;
    }

    public static ExecuteState of(Integer value) {
        return Objects.requireNonNull(MAPPING.get(value), () -> "Invalid execute state value: " + value);
    }

}