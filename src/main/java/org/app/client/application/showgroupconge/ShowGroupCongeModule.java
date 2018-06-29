package org.app.client.application.showgroupconge;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ShowGroupCongeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ShowGroupCongePresenter.class, ShowGroupCongePresenter.MyView.class, ShowGroupCongeView.class, ShowGroupCongePresenter.MyProxy.class);
    }
}