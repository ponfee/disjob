/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.scheduler.registry.etcd;

import cn.ponfee.scheduler.core.base.Supervisor;
import cn.ponfee.scheduler.core.base.Worker;
import cn.ponfee.scheduler.registry.WorkerRegistry;
import cn.ponfee.scheduler.registry.etcd.configuration.EtcdRegistryProperties;

/**
 * Registry worker based Etcd.
 *
 * @author Ponfee
 */
public class EtcdWorkerRegistry extends EtcdServerRegistry<Worker, Supervisor> implements WorkerRegistry {

    public EtcdWorkerRegistry(EtcdRegistryProperties config) {
        super(config);
    }

}
