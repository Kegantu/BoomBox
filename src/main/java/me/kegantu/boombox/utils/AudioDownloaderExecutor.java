package me.kegantu.boombox.utils;

import net.minecraft.util.thread.ThreadExecutor;

import java.util.concurrent.locks.LockSupport;

public class AudioDownloaderExecutor extends ThreadExecutor<Runnable> {

    private Thread thread;
    private volatile boolean stopped;

    public AudioDownloaderExecutor(String name) {
        super(name);

        thread = new Thread(this::waitForTasks);
        thread.setDaemon(true);
        thread.setName("Audio Downloader");
        thread.start();
    }

    @Override
    protected Runnable createTask(Runnable runnable) {
        return runnable;
    }

    @Override
    protected boolean canExecute(Runnable task) {
        return !stopped;
    }

    @Override
    protected Thread getThread() {
        return thread;
    }

    @Override
    protected void waitForTasks() {
        LockSupport.park("waiting for tasks");
    }

    private void waitForStop() {
        while (!this.stopped) {
            this.runTasks(() -> this.stopped);
        }
    }
}
