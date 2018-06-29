package org.app.client.application.congesvalidator;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CongesValidatorModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
		bindPresenter(CongesValidatorPresenter.class, CongesValidatorPresenter.MyView.class, CongesValidatorView.class, CongesValidatorPresenter.MyProxy.class);
    }
}