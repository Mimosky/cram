package org.app.server.dispatch;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Client;
import org.app.shared.User;
import org.app.shared.dispatch.AddUser;
import org.app.shared.dispatch.AddUserResult;
import org.app.toolbox.Crypto;
import org.app.toolbox.SendMail;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class AddUserActionHandler implements ActionHandler<AddUser, AddUserResult> {


	@Override
	public AddUserResult execute(AddUser action, ExecutionContext context) throws ActionException {
		String salt = "HelloUser";
		Session session = null;
		Collection<Client> clients = new ArrayList<Client>();
		boolean added = false;
		Date date = new Date();
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
				Query query = session.createQuery(hql);
				if (query.list().size() == 0){
					//
				//else {
					String[] tokens = action.getClientsName().split("/");
					for (String t : tokens){
						hql = "FROM Client as client WHERE client.name='" + t + "'";
						query = session.createQuery(hql);
						if (query.list().size() == 0){
							logger.warning("[ "+date.toString()+" ] "+"Error while fetching clients name in database");
							// Need to add an action to stop the user creation
						}else {
							clients.add((Client) query.list().get(0));
						}
					}
					User person = new User(action.getEmail(),
							action.getName(), 
							action.getSurname(),
							Crypto.encryptPassword(action.getPassword() + salt),
							new Date(action.getDate()),"enabled",action.getProfile(),action.getTypeDeContrat());
					person.setClient(clients);
					Transaction tx = session.beginTransaction();
					session.save(person);
					tx.commit();
					
					String message = "<html>\n" + "<body>\n" + "<br />" +
							"Bonjour "+action.getName() +",<br />Votre compte sur la plateforme CRAM a été créé: <br /> \t"
							+ "Lien de connexion  :  http://cram.olcya.com <br />\t"
							+ "Login  : "+action.getEmail()+"<br /> \t"
							+ "Mot de passe  : "+action.getPassword()+"<br /> \t"
							+"\n" + "</body>\n" + "</html>";
					SendMail.execute(action.getEmail(), "CRAM APP", message, "");
					added = true;
				}

			} catch (Exception e) {
				logger.warning("[ "+date.toString()+" ] "+e.getMessage());
			}
		} finally {
			session.close();
		}
		if(added){
			logger.info("[ "+date.toString()+" ] "+"User "+ action.getEmail()+" added successfully");
			return new AddUserResult("User added Succefully");
		}else{
			logger.warning("[ "+date.toString()+" ] "+"User "+ action.getEmail()+" is not added");
			return new AddUserResult("User already exists");
		}
	}

	@Override
	public void undo(AddUser action, AddUserResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<AddUser> getActionType() {
		return AddUser.class;
	}
}
