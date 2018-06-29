package org.app.client.application.ferier;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class FerierModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(FerierPresenter.class, FerierPresenter.MyView.class, FerierView.class, FerierPresenter.MyProxy.class);
    }
}