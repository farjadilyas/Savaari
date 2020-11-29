package com.example.savaari;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavaariApplication extends Application {
    public ExecutorService executorService = Executors.newFixedThreadPool(4);
    public Repository repository = new Repository(executorService);

    public Repository getRepository() { return repository; }
}
