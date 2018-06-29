package org.app.client.application.groups;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class GroupsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(GroupsPresenter.class, GroupsPresenter.MyView.class, GroupsView.class, GroupsPresenter.MyProxy.class);
    }
}