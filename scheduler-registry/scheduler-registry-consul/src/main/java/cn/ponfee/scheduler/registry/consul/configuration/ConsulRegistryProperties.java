/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.scheduler.registry.consul.configuration;

import cn.ponfee.scheduler.core.base.JobConstants;
import cn.ponfee.scheduler.registry.AbstractRegistryProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Consul registry configuration properties.
 *
 * @author Ponfee
 */
@Data
@ConfigurationProperties(prefix = JobConstants.SCHEDULER_REGISTRY_KEY_PREFIX + ".consul")
public class ConsulRegistryProperties extends AbstractRegistryProperties {
    private static final long serialVersionUID = -851364562631134942L;

    /**
     * Consul client host
     */
    private String host = "localhost";

    /**
     * Consul client port
     */
    private int port = 8500;

    /**
     * Consul token
     */
    private String token;

}
