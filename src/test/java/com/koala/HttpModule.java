package com.koala;

import com.koala.services.HttpService;
import github.koala.core.annotation.HttpKoala;
import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Module;

/**
 * @author edliao on 2017/6/21.
 * @description TODO
 */
@Module
public class HttpModule {

  @Koala
  HttpService httpService;

}
