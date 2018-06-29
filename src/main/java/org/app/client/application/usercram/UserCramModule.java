package org.app.client.application.usercram;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UserCramModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(UserCramPresenter.class, UserCramPresenter.MyView.class, UserCramView.class, UserCramPresenter.MyProxy.class);
    }
}