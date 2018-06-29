package org.app.server.dispatch;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.User;
import org.app.shared.dispatch.LoginAction;
import org.app.shared.dispatch.LoginResult;
import org.app.toolbox.Crypto;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoginHandler implements ActionHandler<LoginAction, LoginResult> {

	public LoginResult execute(LoginAction action, ExecutionContext context) throws ActionException {
		String salt = "HelloUser";
		Session session = null;
		Transaction tx;
		User user;
		boolean found = false;
		String profile = "";
		String name = "";
		Logger logger = Logger.getLogger("MS");
		Date date = new Date();
		
		
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM User as user WHERE user.email ='" + action.getName() + "'";
				tx = session.beginTransaction();
				Query query = session.createQuery(hql);
				tx.commit();
				List results = query.list();
				if (results.size() != 0){
					user = (User) results.get(0);
					String password = Crypto.encryptPassword(action.getPassword() + salt);
					if ((user.getPassword().equals(password) && (user.getStatus().equals("enabled")))){
						found = true;
						profile = user.getProfile();
						name = user.getName()+" "+user.getSurname();
					}
						
				}
			} catch (Exception e) {
				logger.warning("[ "+date.toString()+" ] "+e.getMessage());
			}
		} finally {
			session.close();
		}
		
		if(action.getDate().getYear()!=date.getYear()) {
			logger.warning("[ "+date.toString()+" ] "+"User "+ action.getName()+" login failed");
			return new LoginResult("no", "", name);
		}
		if(action.getDate().getMonth()!=date.getMonth()){
			logger.warning("[ "+date.toString()+" ] "+"User "+ action.getName()+" login failed");
			return new LoginResult("no", "", name);
		}
		
		if (found){
			logger.info("[ "+date.toString()+" ] "+"User "+ action.getName()+" login success");
			return new LoginResult("ok", profile, name);
		}
		else{
			logger.warning( "[ "+date.toString()+" ] "+"User "+ action.getName()+" login failed");
			return new LoginResult("no", "", name);
		}
	}

	public void undo(LoginAction action, LoginResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoginAction> getActionType() {
		return LoginAction.class;
	}

}
