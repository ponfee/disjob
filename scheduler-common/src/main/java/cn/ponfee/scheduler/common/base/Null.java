package cn.ponfee.scheduler.common.base;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * The {@code Null} class is representing unable instance object
 * 
 * @author Ponfee
 */
public final class Null implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Constructor<Null> BROKEN_CONSTRUCTOR;
    public static final Method BROKEN_METHOD;
    static {
        try {
            BROKEN_CONSTRUCTOR = Null.class.getDeclaredConstructor();
            BROKEN_METHOD = Null.class.getDeclaredMethod("broken");
        } catch (Exception e) {
            // cannot happen
            throw new RuntimeException(e);
        }
    }

    private Null() {
        throw new AssertionError("Null cannot create instance.");
    }

    private void broken() {
        throw new AssertionError("Forbid invoke this method.");
    }

    private Object readResolve() {
        return null;
    }
}
