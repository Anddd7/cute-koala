package github.koala.core.thread;

import com.google.common.collect.Lists;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description 线程池
 */
@Slf4j
public class ThreadGroup {

  //守护进程 计时
  static class Timer extends Thread {

    public Timer() {
      this.setDaemon(true);//守护线程
    }

    @Override
    public void run() {
      log.info("Timer running~");
      Instant time = Instant.now();
      while (true) {
        try {
          Thread.sleep(1000);
          log.info(
              "Program has execute: " + Duration.between(time, Instant.now()).toMillis() + " ms");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  //工作者 执行任务
  static class Worker extends Thread {

    String name;
    int count = 0;

    boolean isFinish = false;

    public Worker(String name) {
      this.name = name;
    }

    @Override
    public void run() {
      log.info("{} running~", name);
      while (!isFinish) {
        try {
          Thread.sleep(1000);
          count++;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      log.info("{} 工作完成~", name);
    }

    public void finish() {
      this.isFinish = true;
    }
  }

  //指挥官 ,工人完成任务时停止
  static class Commander extends Thread {

    List<Worker> workers;
    int task;


    public Commander(int task, List<Worker> workers) {
      this.task = task;
      this.workers = workers;
    }

    @Override
    public void run() {
      log.info("Commander running~");
      while (true) {
        if (workers.stream().allMatch(worker -> worker.count > task)) {
          break;
        }
      }
      log.info("任务完成~");
    }

    public void print() {
      workers.forEach(worker -> log.info("{} has finish {} task .", worker.name, worker.count));
    }
  }

  public static void main(String[] a) throws InterruptedException {
    Timer timer = new Timer();
    List<Worker> workers = Lists.newArrayList();
    for (int i = 0; i < 5; i++) {
      workers.add(new Worker("Worker-" + i));
    }
    Commander commander = new Commander(10, workers);

    log.info("启动项目~");
    timer.start();
    commander.start();
    workers.forEach(worker -> worker.start());

    commander.join();

    if (!commander.isAlive()) {
      workers.forEach(worker -> worker.finish());
    }

    log.info("公布成绩~");
    commander.print();

  }
}
