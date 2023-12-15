/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.disjob.supervisor.application.request;

import cn.ponfee.disjob.common.model.PageRequest;
import cn.ponfee.disjob.supervisor.application.AuthorizeGroupService;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Sched job page request
 *
 * @author Ponfee
 */
@Getter
@Setter
public class SchedJobPageRequest extends PageRequest {
    private static final long serialVersionUID = -6482618667917024367L;

    private Set<String> groups;
    private String jobName;
    private Integer jobType;
    private Integer jobState;

    public void authorizeAndTruncateGroup(String user) {
        this.groups = AuthorizeGroupService.authorizeAndTruncateGroup(user, this.groups);
    }

}
