package com.gurudev.aircnc.infrastructure.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;

@SpringBootTest(webEnvironment = NONE)
class TripStatusSchedulerTest {

  @Autowired
  private ScheduledTaskHolder scheduledTaskHolder;

  @Test
  public void 체크인_스케쥴_테스트() {
    // given
    Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
    scheduledTasks.forEach(
        scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());

    // when
    long count = scheduledTasks.stream()
        // 작업이 CronTask 인 작업들을 필터링
        .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)

        // CronTask로 형변환
        .map(scheduledTask -> (CronTask) scheduledTask.getTask())

        // 우리가 설정한 cron과 메서드명으로 필터링
        .filter(cronTask -> cronTask(cronTask,
            "0 0 0 * * *",
            "com.gurudev.aircnc.infrastructure.scheduler.TripStatusScheduler.bulkStatusUpdateToTravelling"))

        // 스케쥴이 잘 등록되었다면 count는 1이 될 것이다.
        .count();

    // then
    assertThat(count).isEqualTo(1L);
  }

  @Test
  public void 체크아웃_스케쥴_테스트() {
    // given
    Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
    scheduledTasks.forEach(
        scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());

    // when
    long count = scheduledTasks.stream()
        .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
        .map(scheduledTask -> (CronTask) scheduledTask.getTask())
        .filter(cronTask -> cronTask(cronTask,
            "0 0 0 * * *",
            "com.gurudev.aircnc.infrastructure.scheduler.TripStatusScheduler.bulkStatusUpdateToDone"))
        .count();

    // then
    assertThat(count).isEqualTo(1L);
  }

  private boolean cronTask(CronTask cronTask, String cron, String method) {
    return cronTask.getExpression().equals(cron) && cronTask.toString()
        .equals(method);
  }

}
