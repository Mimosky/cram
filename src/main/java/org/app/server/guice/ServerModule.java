package org.app.server.guice;

import org.app.server.dispatch.AddClientHandler;
import org.app.server.dispatch.AddFerierHandler;
import org.app.server.dispatch.AddGroupHandler;
import org.app.server.dispatch.AddUserActionHandler;
import org.app.server.dispatch.GeneratePdfHandler;
import org.app.server.dispatch.LoginHandler;
import org.app.server.dispatch.ModifyCongesHandler;
import org.app.server.dispatch.ModifyGroupHandler;
import org.app.server.dispatch.ModifyUserHandler;
import org.app.server.dispatch.NotifyCramHandler;
import org.app.server.dispatch.RetrieveClientHandler;
import org.app.server.dispatch.RetrieveCongesHandler;
import org.app.server.dispatch.RetrieveFerierHandler;
import org.app.server.dispatch.RetrieveGroupHandler;
import org.app.server.dispatch.RetrieveUsersHandler;
import org.app.server.dispatch.SaveCongesHandler;
import org.app.server.dispatch.SaveCramHandler;
import org.app.server.dispatch.SendEmailHandler;
import org.app.server.dispatch.UserInformationHandler;
import org.app.shared.dispatch.AddClientAction;
import org.app.shared.dispatch.AddFerierAction;
import org.app.shared.dispatch.AddGroupAction;
import org.app.shared.dispatch.AddUser;
import org.app.shared.dispatch.GeneratePdfAction;
import org.app.shared.dispatch.LoginAction;
import org.app.shared.dispatch.ModifyCongesAction;
import org.app.shared.dispatch.ModifyGroupAction;
import org.app.shared.dispatch.ModifyUserAction;
import org.app.shared.dispatch.NotifyCramAction;
import org.app.shared.dispatch.RetrieveClientAction;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveFerierAction;
import org.app.shared.dispatch.RetrieveGroupAction;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.SaveCongesAction;
import org.app.shared.dispatch.SaveCramAction;
import org.app.shared.dispatch.SendEmailAction;
import org.app.shared.dispatch.UserInformationAction;

//import com.gwtplatform.dispatch.rpc.server.guice.DispatchModule;
import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;


public class ServerModule extends HandlerModule {
    @Override
    protected void configureHandlers() {
        //install(new DispatchModule());
        bindHandler(AddUser.class, AddUserActionHandler.class);
        bindHandler(LoginAction.class, LoginHandler.class);
        bindHandler(RetrieveUsersAction.class, RetrieveUsersHandler.class);
        bindHandler(RetrieveClientAction.class, RetrieveClientHandler.class);
        bindHandler(AddClientAction.class, AddClientHandler.class);
        bindHandler(ModifyUserAction.class, ModifyUserHandler.class);
        bindHandler(UserInformationAction.class, UserInformationHandler.class);
        bindHandler(SaveCramAction.class, SaveCramHandler.class);
        bindHandler(SaveCongesAction.class, SaveCongesHandler.class);
        bindHandler(RetrieveCongesAction.class, RetrieveCongesHandler.class);
        bindHandler(ModifyCongesAction.class, ModifyCongesHandler.class);
        bindHandler(AddFerierAction.class, AddFerierHandler.class);
        bindHandler(RetrieveFerierAction.class, RetrieveFerierHandler.class);
        bindHandler(NotifyCramAction.class, NotifyCramHandler.class);
        bindHandler(SendEmailAction.class, SendEmailHandler.class);
        bindHandler(AddGroupAction.class, AddGroupHandler.class);
        bindHandler(RetrieveGroupAction.class, RetrieveGroupHandler.class);
        bindHandler(ModifyGroupAction.class, ModifyGroupHandler.class);
        bindHandler(GeneratePdfAction.class, GeneratePdfHandler.class);
    }
}