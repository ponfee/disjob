/*
 * Copyright 2022-2024 Ponfee (http://www.ponfee.cn/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ponfee.disjob.common.util;

import cn.ponfee.disjob.common.concurrent.LoggedUncaughtExceptionHandler;
import cn.ponfee.disjob.common.concurrent.NamedThreadFactory;
import cn.ponfee.disjob.common.concurrent.ThreadPoolExecutors;
import cn.ponfee.disjob.common.concurrent.Threads;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;

/**
 * Thread Test
 *
 * @author Ponfee
 */
class ThreadTest {
    private static final Logger log = LoggerFactory.getLogger(ThreadTest.class);

    @Test
    void testThreadDeath() throws InterruptedException {
        Thread t = new Thread(() -> {
            for (int i = 0; ; i++) {
                System.out.print(i + " ");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("\n\nInterruptedException\n");
                } catch (ThreadDeath e) {
                    System.out.println("\n\nThreadDeath: " + i + "\n");
                    if (i > 10) {
                        System.out.println("i > 10");
                        throw e;
                        //break;
                    }
                }
            }
        });

        t.setUncaughtExceptionHandler(new LoggedUncaughtExceptionHandler(log));

        Assertions.assertFalse(t.isAlive());
        t.start();
        Assertions.assertTrue(t.isAlive());
        Thread.sleep(500);
        Assertions.assertTrue(t.isAlive());
        t.stop();
        Assertions.assertTrue(t.isAlive());
        Thread.sleep(1000);
        Assertions.assertTrue(t.isAlive());
        t.stop();
        Thread.sleep(500);
        Assertions.assertFalse(t.isAlive());
        t.join();
        Assertions.assertFalse(t.isAlive());
    }

    @Test
    void testGetStackFrame() {
        System.out.println(Threads.getStackFrame(0));
        System.out.println(Threads.getStackFrame(1));
        System.out.println(Threads.getStackFrame(2));
        System.out.println(Threads.getStackFrame(3));
    }

    @Test
    void testMaximumPoolSize__CALLER_RUNS() {
        ExecutorService threadPool = ThreadPoolExecutors.builder()
            .corePoolSize(1)
            .maximumPoolSize(4)
            .workQueue(new SynchronousQueue<>())
            .keepAliveTimeSeconds(1)
            .rejectedHandler(ThreadPoolExecutors.CALLER_RUNS)
            .threadFactory(NamedThreadFactory.builder().prefix("notify_server").uncaughtExceptionHandler(log).build())
            .build();

        for (int i = 0; i < 15; i++) {
            final int x = i;
            threadPool.submit(() -> {
                try {
                    Thread.sleep("main".equals(Thread.currentThread().getName()) ? 2000 : 200);
                    System.out.println("----------" + Thread.currentThread().getName() + ", " + x);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ThreadPoolExecutors.shutdown(threadPool);
    }

    @Test
    void testMaximumPoolSize__CALLER_BLOCKS() {
        ExecutorService threadPool = ThreadPoolExecutors.builder()
            .corePoolSize(1)
            .maximumPoolSize(4)
            .workQueue(new SynchronousQueue<>())
            .keepAliveTimeSeconds(1)
            .rejectedHandler(ThreadPoolExecutors.CALLER_BLOCKS)
            .threadFactory(NamedThreadFactory.builder().prefix("notify_server").uncaughtExceptionHandler(log).build())
            .build();

        for (int i = 0; i < 15; i++) {
            final int x = i;
            threadPool.submit(() -> {
                try {
                    Thread.sleep("main".equals(Thread.currentThread().getName()) ? 2000 : 200);
                    System.out.println("----------" + Thread.currentThread().getName() + ", " + x);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ThreadPoolExecutors.shutdown(threadPool);
    }

    @Test
    void testKeepAliveTimeSeconds__CALLER_RUNS() {
        ExecutorService threadPool = ThreadPoolExecutors.builder()
            .corePoolSize(1)
            .maximumPoolSize(4)
            .workQueue(new SynchronousQueue<>())
            .keepAliveTimeSeconds(0)
            .rejectedHandler(ThreadPoolExecutors.CALLER_RUNS)
            .allowCoreThreadTimeOut(false)
            .threadFactory(NamedThreadFactory.builder().prefix("notify_server").uncaughtExceptionHandler(log).build())
            .build();

        for (int i = 0; i < 15; i++) {
            final int x = i;
            threadPool.submit(() -> {
                try {
                    Thread.sleep("main".equals(Thread.currentThread().getName()) ? 2000 : 200);
                    System.out.println("----------" + Thread.currentThread().getName() + ", " + x);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ThreadPoolExecutors.shutdown(threadPool);
    }

    @Test
    void testKeepAliveTimeSeconds__CALLER_BLOCKS() {
        ExecutorService threadPool = ThreadPoolExecutors.builder()
            .corePoolSize(1)
            .maximumPoolSize(4)
            .workQueue(new SynchronousQueue<>())
            .keepAliveTimeSeconds(0)
            .rejectedHandler(ThreadPoolExecutors.CALLER_BLOCKS)
            .allowCoreThreadTimeOut(false)
            .threadFactory(NamedThreadFactory.builder().prefix("notify_server").uncaughtExceptionHandler(log).build())
            .build();

        for (int i = 0; i < 15; i++) {
            final int x = i;
            threadPool.submit(() -> {
                try {
                    Thread.sleep("main".equals(Thread.currentThread().getName()) ? 2000 : 200);
                    System.out.println("----------" + Thread.currentThread().getName() + ", " + x);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ThreadPoolExecutors.shutdown(threadPool);
    }

}
