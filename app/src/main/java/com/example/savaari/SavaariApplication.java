package com.example.savaari;

import android.app.Application;

import com.example.savaari.services.location.LocationUpdateUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SavaariApplication extends Application {
    public ExecutorService executorService;
    public Repository repository;
    public ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);

    public SavaariApplication() {
        executorService = Executors.newFixedThreadPool(4);
        repository = new Repository(executorService);
        LocationUpdateUtil.setRepository(repository);
    }

    public Repository getRepository() { return repository; }
}
