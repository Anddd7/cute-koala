package github.koala.zookeeper;

/**
 * @author edliao on 2017/7/5.
 * @description TODO
 */
public abstract class AbstractEventHandler {

  abstract void handleChange(String path, byte[] data);

}
