package org.app.client.application.conges;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CongesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(CongesPresenter.class, CongesPresenter.MyView.class, CongesView.class, CongesPresenter.MyProxy.class);
    }
}