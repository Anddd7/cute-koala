package github.koala.core;

import github.koala.bean.pool.KoalaZoo;
import java.lang.reflect.Field;

public abstract class AbstractRegistry {

  protected KoalaZoo zoo;

  public AbstractRegistry(KoalaZoo zoo) {
    this.zoo = zoo;
  }

  public abstract <T> T createInstance(Class<T> implClass);

  public abstract Class registerKoala(Field field);
}
