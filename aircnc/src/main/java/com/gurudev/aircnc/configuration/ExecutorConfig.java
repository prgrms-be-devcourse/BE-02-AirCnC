package com.gurudev.aircnc.configuration;

import com.gurudev.aircnc.infrastructure.event.TripEventQueue;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class ExecutorConfig extends AsyncConfigurerSupport {

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(30);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix("ASYNC-THREAD");
    executor.initialize();
    return executor;
  }

  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
    taskScheduler.setThreadNamePrefix("Scheduler-Thread-");
    taskScheduler.initialize();

    return taskScheduler;
  }

  @Bean
  public TripEventQueue tripEventQueue() {
    return new TripEventQueue(1000);
  }
}
