package others.thread;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * @author edliao on 2017/6/19.
 * @description Sync test
 */
public class SyncSample extends Thread {

  static Map<String, Integer> concurrentMap = Maps.newConcurrentMap();
  static Map<String, Integer> map = Maps.newHashMap();

  int count;
  int time;

  private static final String FLAG = "FLAG";

  public SyncSample(int count) {
    this.count = count;
    map.put(FLAG, 0);
    concurrentMap.put(FLAG, 0);
  }

  @Override
  public void run() {
    while (time < count) {
      map.computeIfPresent(FLAG, (s, integer) -> integer + 1);
      concurrentMap.computeIfPresent(FLAG, (s, integer) -> integer + 1);
      time++;
    }
  }

  public static void print() {
    System.out.println("Normal Map:" + map.get(FLAG));
    System.out.println("Concurrent Map:" + concurrentMap.get(FLAG));
  }

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new SyncSample(10000);
    Thread t2 = new SyncSample(10000);

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    print();
  }
}
