package com.gurudev.aircnc.infrastructure.scheduler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;

@SpringBootTest
class SchedulerTest {

  @Autowired
  private ScheduledTaskHolder scheduledTaskHolder;

  @Test
  public void testYearlyCronTaskScheduled() {
    Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
    scheduledTasks.forEach(
        scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
    long count = scheduledTasks.stream()
        .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
        .map(scheduledTask -> (CronTask) scheduledTask.getTask())
        .filter(cronTask -> cronTask.getExpression().equals("0 0 12 * * *") && cronTask.toString()
            .equals("com.gurudev.aircnc.infrastructure.scheduler.TripStatusScheduler.startCheckIn"))
        .count();
    assertThat(count).isEqualTo(1L);
  }

}
