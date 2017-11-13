package com.edu.sky.promotion.service.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DaemonProgramTest {

    private static int queueCapacity = 10;
    private static BlockingQueue<String> logQueue = new ArrayBlockingQueue<String>(queueCapacity);

    public static void main(String[] args) {

        for (int i = 0; i < 2; i++) {
            LogWriter logwriter = new LogWriter();
            logwriter.start();
            LogCleaner logCleaner = new LogCleaner();
            logCleaner.setDaemon(true);
            logCleaner.start();
        }
    }

    private static class LogWriter extends Thread{
        @Override
        public void run() {
            for (int i = 0; i < queueCapacity; i++) {
                try {
                    logQueue.put("" + i);
                    System.out.println("日志已写入：内容为：" +this.getName()+ logQueue);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class LogCleaner extends Thread{
        int i = 0;
        @Override
        public void run() {
            while (true) {
                if (logQueue.size() > 5) {
                    try {
                        logQueue.take();
                        System.out.println("多余日志清除：当前内容为：" +this.getName()+ logQueue);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
