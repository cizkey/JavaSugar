package com.twiceyuan.sugar;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by twiceYuan on 4/29/16.
 * <p>
 * TaskValve unit test
 */
public class TaskValveTest {

    @Test
    public void test() {
        final TaskValve valve = TaskValve.create();

        for (int i = 1; i <= 20; i++) {
            delayAddTask((long) (Math.random() * 5000), valve, "步骤" + i, 100);
        }

        // 较晚加入，较高的优先级
        delayAddTask(2490, valve, "步骤21", 0);

        delayAndRun(2500, new Runnable() {
            @Override
            public void run() {
                System.out.println("---> \t阀门打开\t\t" + TaskValveTest.this.markTime());
                valve.openValve();
            }
        });

        sleep(5100);
    }

    private void delayAndRun(final long ms, final Runnable runnable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TaskValveTest.this.sleep(ms);
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).start();
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void delayAddTask(final long ms, final TaskValve valve, final String taskName, final int priority) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TaskValveTest.this.sleep(ms);
                System.out.println(taskName + "\t被创建\t\t" + TaskValveTest.this.markTime());
                valve.addTask(priority, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(taskName + "\t被执行\t\t" + TaskValveTest.this.markTime());
                    }
                });
            }
        }).start();
    }

    private String markTime() {
        return new SimpleDateFormat("HH:mm:ss SSS").format(new Date());
    }
}
