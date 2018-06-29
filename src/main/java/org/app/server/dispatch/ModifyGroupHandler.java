package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Groupe;
import org.app.shared.User;
import org.app.shared.dispatch.ModifyGroupAction;
import org.app.shared.dispatch.ModifyGroupResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ModifyGroupHandler implements ActionHandler<ModifyGroupAction, ModifyGroupResult>{

	@Override
	public ModifyGroupResult execute(ModifyGroupAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		boolean added = false;
		Collection<User> list = new ArrayList<User>();
		User tmpUser = null;
		Groupe group = null ;
		Date date = new Date();
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM Groupe as group WHERE group.groupName ='" + action.getName() + "'";
				Query query = session.createQuery(hql);
				if (query.list().size() != 0){
					group = (Groupe) query.list().get(0);
					// Retrieve the manager
					String[] managerName = action.getManager().split(" ");
					hql = "FROM User as user WHERE user.name ='" + managerName[0] + "' and user.surname ='"+managerName[1] + "'";
					query = session.createQuery(hql);
					if (query.list().size() != 0){
						User manager = (User) query.list().get(0);
						String[] colleages = action.getUsers().split("--");
						String[] userName ;
						for(String  u : colleages ){
							userName = u.split(" ");
							hql = "FROM User as user WHERE user.name ='" + userName[0] + "' and user.surname ='"+userName[1] + "'";
							query = session.createQuery(hql);
							if(query.list().size() != 0){
								tmpUser = (User)query.list().get(0);
								list.add(tmpUser); 
								added = true;
							}
						}
						group.setManager(manager);
						group.setCollaborateurs(list);
					}
	
					if(added){
						Transaction tx = session.beginTransaction();
						session.update(group);
						tx.commit();
					}
			
				}

			} catch (Exception e) {
				logger.warning("[ "+date.toString()+" ] "+e.getMessage());
			}
		} finally {
			session.close();
		}
		
		if(added){
			logger.info("[ "+date.toString()+" ] "+ "Group " + action.getName() + " is succefully updated");
			return new ModifyGroupResult("ok");
		}
		else{
			logger.warning("[ "+date.toString()+" ] "+"An error occured while group update");
			return new ModifyGroupResult("Nok");
		}
	}

	@Override
	public Class<ModifyGroupAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(ModifyGroupAction action, ModifyGroupResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
