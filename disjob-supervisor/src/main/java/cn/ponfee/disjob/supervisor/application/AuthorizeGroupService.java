/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.disjob.supervisor.application;

import cn.ponfee.disjob.common.base.SingletonClassConstraint;
import cn.ponfee.disjob.common.collect.Collects;
import cn.ponfee.disjob.common.concurrent.ThreadPoolExecutors;
import cn.ponfee.disjob.core.exception.AuthenticationException;
import cn.ponfee.disjob.core.exception.KeyNotExistsException;
import cn.ponfee.disjob.supervisor.dao.mapper.SchedInstanceMapper;
import cn.ponfee.disjob.supervisor.dao.mapper.SchedJobMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Authorize group service
 *
 * @author Ponfee
 */
@Service
public class AuthorizeGroupService extends SingletonClassConstraint {

    /**
     * SQL中`group IN (a, b, ..., x)`允许的最大长度
     */
    public static final int SQL_GROUP_IN_MAX_SIZE = 50;

    private final SchedJobMapper schedJobMapper;
    private final SchedInstanceMapper schedInstanceMapper;

    private final Cache<Long, String> jobGroupCache = CacheBuilder.newBuilder()
        .initialCapacity(1_000)
        .maximumSize(1_000_000)
        // 当job被删除后，就不再被使用，2小时后没有访问会自动删除
        // 实际是惰性删除策略：[被访问时判断过期则被删除] 或 [超过容量时使用LRU算法选择删除]
        .expireAfterAccess(Duration.ofHours(3))
        .build();

    public AuthorizeGroupService(SchedJobMapper schedJobMapper,
                                 SchedInstanceMapper schedInstanceMapper) {
        this.schedJobMapper = schedJobMapper;
        this.schedInstanceMapper = schedInstanceMapper;

        ThreadPoolExecutors.commonScheduledPool().scheduleWithFixedDelay(jobGroupCache::cleanUp, 6, 6, TimeUnit.HOURS);
    }

    public static Set<String> authorizeAndTruncateGroup(String user, Set<String> paramGroups) {
        Set<String> permitGroups = SchedGroupService.myGroups(user);
        if (CollectionUtils.isEmpty(paramGroups)) {
            paramGroups = permitGroups;
        } else if (!permitGroups.containsAll(paramGroups)) {
            throw new AuthenticationException("Unauthorized group: " + Sets.difference(paramGroups, permitGroups));
        }
        return truncateGroup(paramGroups);
    }

    public static Set<String> truncateGroup(Set<String> paramGroups) {
        return Collects.truncate(paramGroups, SQL_GROUP_IN_MAX_SIZE);
    }

    public static void authorizeGroup(String user, String dataGroup) {
        if (!SchedGroupService.myGroups(user).contains(dataGroup)) {
            throw new AuthenticationException("Unauthorized group: " + dataGroup);
        }
    }

    public static void authorizeGroup(String user, String authGroup, String dataGroup) {
        if (!authGroup.equals(dataGroup)) {
            throw new AuthenticationException("Unmatched group: " + authGroup + " != " + dataGroup);
        }
        authorizeGroup(user, dataGroup);
    }

    public void authorizeJob(String user, long jobId) {
        String dataGroup = getGroup(jobId);
        if (StringUtils.isEmpty(dataGroup)) {
            throw new KeyNotExistsException("Job id not exists: " + jobId);
        }
        authorizeGroup(user, dataGroup);
    }

    public void authorizeJob(String user, String authGroup, long jobId) {
        String dataGroup = getGroup(jobId);
        if (StringUtils.isEmpty(dataGroup)) {
            throw new KeyNotExistsException("Job id not exists: " + jobId);
        }
        authorizeGroup(user, authGroup, dataGroup);
    }

    public void authorizeInstance(String user, long instanceId) {
        Long jobId = schedInstanceMapper.getJobId(instanceId);
        if (jobId == null) {
            throw new KeyNotExistsException("Instance id not exists: " + instanceId);
        }
        authorizeJob(user, jobId);
    }

    public void authorizeInstance(String user, String authGroup, long instanceId) {
        Long jobId = schedInstanceMapper.getJobId(instanceId);
        if (jobId == null) {
            throw new KeyNotExistsException("Instance id not exists: " + instanceId);
        }
        authorizeJob(user, authGroup, jobId);
    }

    private String getGroup(Long jobId) {
        String group = jobGroupCache.getIfPresent(jobId);
        if (group != null) {
            return group;
        }
        group = schedJobMapper.getGroup(jobId);
        if (group != null) {
            jobGroupCache.put(jobId, group);
        }
        return group;
    }

}
