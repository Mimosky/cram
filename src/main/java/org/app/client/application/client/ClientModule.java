package org.app.client.application.client;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(ClientPresenter.class, ClientPresenter.MyView.class, ClientView.class);
    }
}