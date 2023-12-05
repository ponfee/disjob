/* __________              _____                                                *\
** \______   \____   _____/ ____\____   ____    Copyright (c) 2017-2023 Ponfee  **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \   http://www.ponfee.cn            **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/   Apache License Version 2.0      **
**  |____|   \____/|___|  /__|  \___  >\___  >  http://www.apache.org/licenses/ **
**                      \/          \/     \/                                   **
\*                                                                              */

package cn.ponfee.disjob.core.exception;

import cn.ponfee.disjob.common.exception.BaseRuntimeException;
import cn.ponfee.disjob.core.base.JobCodeMsg;

/**
 * Ket exists exception
 *
 * @author Ponfee
 */
public class KeyExistsException extends BaseRuntimeException {
    private static final long serialVersionUID = -6198294663343359692L;

    public KeyExistsException() {
        super(JobCodeMsg.INVALID_PARAM.getCode(), "Key already exists.");
    }

    public KeyExistsException(String message) {
        super(JobCodeMsg.UN_AUTHENTICATED.getCode(), message);
    }
}
