package com.koala;

/**
 * @author edliao on Auto generate.
 */
@github.koala.core.annotation.Module
public class UserHttpModule {

	@github.koala.core.annotation.Koala
	com.koala.services.HttpService httpService;

	@github.koala.core.annotation.Koala(value = com.koala.services.impl.UserServiceImpl.class)
	com.koala.services.UserService userService;

	@github.koala.core.annotation.Koala(scope = github.koala.core.annotation.Koala.ScopeEnum.NOSCOPE)
	com.koala.utils.RandomTool randomTool;

}