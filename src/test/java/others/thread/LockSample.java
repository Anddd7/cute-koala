package others.thread;

/**
 * @author edliao on 2017/6/27.
 * @description TODO
 */
public class LockSample {

  Object object = new Object();

  public void process1() {
    System.out.println("开始");
    lock();
    System.out.println("结束");
  }

  public void lock() {
    synchronized (object) {
      try {
        object.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void unlock() {
    synchronized (object) {
      object.notify();
    }
  }


  public static void main(String[] a) {
    LockSample lockSample = new LockSample();
    Thread thread = new Thread(() -> {
      try {
        Thread.sleep(1000 * 5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      lockSample.unlock();
    });

    thread.start();
    lockSample.process1();
  }
}
