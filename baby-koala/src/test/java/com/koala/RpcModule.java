package com.koala;

import com.koala.normal.services.RpcFirst;
import com.koala.normal.services.RpcSecond;
import com.koala.normal.services.impl.RpcFirstImpl;
import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Module;
import github.koala.rpc.consumer.Consumer;
import github.koala.rpc.provider.Provider;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
@Module
public class RpcModule {

  @Koala(RpcFirstImpl.class)
  @Provider
  RpcFirst rpcProvider;

  @Koala
  @Consumer
  RpcSecond rpcConsumer;
}
