package com.gurudev.aircnc.configuration;

import com.gurudev.aircnc.infrastructure.event.TripEventQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class EventConfig {

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
