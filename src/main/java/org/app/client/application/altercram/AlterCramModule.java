package org.app.client.application.altercram;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AlterCramModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(AlterCramPresenter.class, AlterCramPresenter.MyView.class, AlterCramView.class, AlterCramPresenter.MyProxy.class);
    }
}