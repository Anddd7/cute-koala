package com.koala;

import com.koala.services.RpcFirst;
import com.koala.services.RpcSecond;
import com.koala.services.impl.RpcSecondImpl;
import github.koala.core.annotation.Koala;
import github.koala.rpc.consumer.Consumer;
import github.koala.rpc.provider.Provider;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public class RpcBackModule {

  @Koala
  @Consumer
  RpcFirst rpcProvider;

  @Koala(RpcSecondImpl.class)
  @Provider
  RpcSecond rpcConsumer;
}
