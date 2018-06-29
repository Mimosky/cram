package org.app.client.application.user;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UserModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(UserPresenter.class, UserPresenter.MyView.class, UserView.class);
    }
}