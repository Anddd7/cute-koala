package com.koala;

import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Koala.ScopeEnum;
import github.koala.core.annotation.Module;

/**
 * @author edliao on Auto generate.
 */
@Module
public class UserModule {

	@Koala
	com.koala.services.HttpService httpService;

	@Koala(impl = com.koala.services.impl.UserServiceImpl.class)
	com.koala.services.UserService userService;

	@Koala(scope = ScopeEnum.NOSCOPE)
	com.koala.utils.RandomTool randomTool;

}