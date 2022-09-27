package cn.ponfee.scheduler.supervisor.config;

import cn.ponfee.scheduler.common.base.tuple.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Test transaction manager abstract service class
 *
 * @author Ponfee
 */
public abstract class AbstractTxManagerTestService<E, I> {

    private final TransactionTemplate transactionTemplate;
    private final BiFunction<I, I, List<E>> query;
    private final BiFunction<I, String, Integer> update;
    private final Function<E, Tuple2<I, String>> extractMapper;

    public AbstractTxManagerTestService(TransactionTemplate transactionTemplate,
                                        BiFunction<I, I, List<E>> query,
                                        BiFunction<I, String, Integer> update,
                                        Function<E, Tuple2<I, String>> extractMapper) {
        this.transactionTemplate = transactionTemplate;
        this.query = query;
        this.update = update;
        this.extractMapper = extractMapper;
    }

    // ----------------------------------------
    // 不能加final
    // 有final时当前对象为CGLIB代理类，此时query对象为null：cn.ponfee.scheduler.service.JobTxManagerTestService$$EnhancerBySpringCGLIB$$df6596f5
    // 无final则正常：cn.ponfee.scheduler.service.JobTxManagerTestService
    public Map<I, String> queryData(I id1, I id2) {
        Map<I, String> res = query.apply(id1, id2)
                .stream()
                .map(extractMapper)
                .collect(Collectors.toMap(t -> t.a, t -> StringUtils.defaultString(t.b), (v1, v2) -> v2));
        Assertions.assertEquals(2, res.size());
        Assertions.assertTrue(res.containsKey(id1));
        Assertions.assertTrue(res.containsKey(id2));
        return res;
    }

    // ----------------------------------------测试事务有异常场景
    public void testWithoutTxHasError(I id1, I id2) {
        action(id1, id2, true);
    }

    public void testWithAnnotationTxHasError(I id1, I id2) {
        action(id1, id2, true);
    }

    public void testWithTemplateTxHasError(I id1, I id2) {
        transactionTemplate.executeWithoutResult(tx -> action(id1, id2, true));
    }

    // ----------------------------------------测试事务有异常场景
    public void testWithoutTxNoneError(I id1, I id2) {
        action(id1, id2, false);
    }

    public void testWithAnnotationTxNoneError(I id1, I id2) {
        action(id1, id2, false);
    }

    public void testWithTemplateTxNoneError(I id1, I id2) {
        transactionTemplate.executeWithoutResult(tx -> action(id1, id2, false));
    }

    // ----------------------------------------private methods
    private void action(I id1, I id2, boolean testError) {
        int row;
        try {
            row = update.apply(id1, uuid());
            if (row < 1) {
                throw new IllegalStateException("Invalid row " + row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (testError) {
            throw new RuntimeException("Test error in db operation");
        }

        try {
            row = update.apply(id2, uuid());
            if (row < 1) {
                throw new IllegalStateException("Invalid row " + row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }

}
