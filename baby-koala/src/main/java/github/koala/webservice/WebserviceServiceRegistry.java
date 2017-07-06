package github.koala.webservice;

import github.koala.zookeeper.AbstractServiceRegistry;
import github.koala.zookeeper.KoalaWatcher;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public abstract class WebserviceServiceRegistry extends AbstractServiceRegistry {

   protected static final String WEBSERVICE_PATH = SERVICE_PATH + "/webservice";

  protected WebserviceServiceRegistry(KoalaWatcher watcher) {
    super(watcher);
  }
}
