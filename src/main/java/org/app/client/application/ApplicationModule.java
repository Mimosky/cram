package org.app.client.application;

import org.app.client.application.addonedaywork.AddOneDayWorkModule;
import org.app.client.application.addwork.AddWorkModule;
import org.app.client.application.admin.AdminModule;
import org.app.client.application.client.ClientModule;
import org.app.client.application.confirm.ConfirmModule;
import org.app.client.application.confirmconges.ConfirmCongesModule;
import org.app.client.application.conges.CongesModule;
import org.app.client.application.congesvalidator.CongesValidatorModule;
import org.app.client.application.ferier.FerierModule;
import org.app.client.application.header.HeaderModule;
import org.app.client.application.home.HomeModule;
import org.app.client.application.login.LoginModule;
import org.app.client.application.notify.NotifyModule;
import org.app.client.application.user.UserModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.app.client.application.validconge.ValidCongeModule;
import org.app.client.application.allconges.AllCongesModule;
import org.app.client.application.groups.GroupsModule;
import org.app.client.application.usercram.UserCramModule;
import org.app.client.application.altercram.AlterCramModule;
import org.app.client.application.showgroupconge.ShowGroupCongeModule;
import org.app.client.application.initconges.InitCongesModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
		install(new InitCongesModule());
		install(new ShowGroupCongeModule());
		install(new AlterCramModule());
		install(new UserCramModule());
		install(new GroupsModule());
		install(new AllCongesModule());
		install(new ValidCongeModule());
		install(new ConfirmCongesModule());
		install(new ConfirmModule());
		install(new NotifyModule());
		install(new FerierModule());
		install(new CongesValidatorModule());
		install(new CongesModule());
		install(new ClientModule());
		install(new UserModule());
		install(new AddOneDayWorkModule());
		install(new AddWorkModule());
		install(new HeaderModule());
		install(new LoginModule());
		install(new HomeModule());
		install(new AdminModule());
		//install(new RpcDispatchAsyncModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}