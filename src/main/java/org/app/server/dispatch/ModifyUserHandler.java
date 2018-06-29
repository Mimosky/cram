package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Client;
import org.app.shared.User;
import org.app.shared.dispatch.ModifyUserAction;
import org.app.shared.dispatch.ModifyUserResult;
import org.app.toolbox.Crypto;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ModifyUserHandler implements ActionHandler<ModifyUserAction, ModifyUserResult> {

	@Override
	public ModifyUserResult execute(ModifyUserAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		String salt = "HelloUser";
		Date date = new Date();
		Logger logger = Logger.getLogger("MS");
		boolean modification = false;
		if (action.getMyPassword()) {
			session = HibernateUtil.getSessionFactory().openSession();
			String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
			Query query = session.createQuery(hql);
			if (query.list().size() != 0) {
				User person = (User) query.list().get(0);
				person.setPassword(Crypto.encryptPassword(action.getPassword() + salt));
				Transaction tx = session.beginTransaction();
				session.update(person);
				tx.commit();
			}
		} else {
			Collection<Client> clients = new ArrayList<Client>();
			try {
				try {
					session = HibernateUtil.getSessionFactory().openSession();
					String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
					Query query = session.createQuery(hql);
					if (query.list().size() != 0) {
						User person = (User) query.list().get(0);
						String[] tokens = action.getClientsName().split("/");
						for (String t : tokens) {
							hql = "FROM Client as client WHERE client.name='" + t + "'";
							query = session.createQuery(hql);
							if (query.list().size() == 0) {
								logger.warning("[ "+date.toString()+" ] "+"Error while fetching client Name in DB");
								// Need to add an action to stop the user
								// creation
							} else {
								clients.add((Client) query.list().get(0));
							}
						}
						person.setClient(clients);
						person.setName(action.getName());
						person.setSurname(action.getSurname());
						person.setProfile(action.getProfile());
						person.setStatus(action.getStatus());
						person.setTypeDeContrat(action.getTypeDeContrat());
						if (!action.getPassword().isEmpty())
							person.setPassword(Crypto.encryptPassword(action.getPassword() + salt));
						Transaction tx = session.beginTransaction();
						session.update(person);
						tx.commit();
						modification = true;
					}

				} catch (Exception e) {
					logger.warning("[ "+date.toString()+" ] "+e.getMessage());
				}
			} finally {
				session.close();
			}
		}
		if (modification){
			logger.info("[ "+date.toString()+" ] "+"User "+action.getEmail() +" is successfully updated");
			return new ModifyUserResult("User Modified Succefully");
		}else{
			logger.warning("[ "+date.toString()+" ] "+"User update failed");
			return new ModifyUserResult("User does not exists");
		}
	}

	@Override
	public void undo(ModifyUserAction action, ModifyUserResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<ModifyUserAction> getActionType() {
		return ModifyUserAction.class;
	}
}
