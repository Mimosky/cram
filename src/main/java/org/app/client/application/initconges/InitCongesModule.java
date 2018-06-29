package org.app.client.application.initconges;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class InitCongesModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(InitCongesPresenter.class, InitCongesPresenter.MyView.class, InitCongesView.class, InitCongesPresenter.MyProxy.class);
    }
}