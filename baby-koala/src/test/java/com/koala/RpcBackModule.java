package com.koala;

import com.koala.normal.services.RpcFirst;
import com.koala.normal.services.RpcSecond;
import com.koala.normal.services.impl.RpcSecondImpl;
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
