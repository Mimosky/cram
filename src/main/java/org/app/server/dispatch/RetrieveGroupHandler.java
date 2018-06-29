package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Groupe;
import org.app.shared.User;
import org.app.shared.dispatch.RetrieveGroupAction;
import org.app.shared.dispatch.RetrieveGroupResult;
import org.app.shared.wrapper.GroupWrapper;
import org.app.shared.wrapper.UserWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RetrieveGroupHandler implements ActionHandler<RetrieveGroupAction,RetrieveGroupResult>{

	@SuppressWarnings("unchecked")
	@Override
	public RetrieveGroupResult execute(RetrieveGroupAction action, ExecutionContext context) throws ActionException {
		List<Groupe> list = null;
		ArrayList<GroupWrapper> groupList = new ArrayList<GroupWrapper>();
		Session session = null;
		Transaction tx = null;
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String hql = "FROM Groupe";
				list = (List<Groupe>) session.createQuery(hql).list();
				tx.commit();
				for(Groupe g: list){
					Collection<UserWrapper> users = new ArrayList<UserWrapper>();
					for(User u: g.getCollaborateurs()){
						users.add(new UserWrapper(u.getName(), u.getSurname(), u.getEmail()));
					}
					groupList.add(new GroupWrapper(g.getGroupName(),new UserWrapper(g.getManager().getName(),g.getManager().getSurname(),g.getManager().getEmail()),users));
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}
		logger.info("Groups retrievd successfully");
		return new RetrieveGroupResult("Ok",groupList);
	}

	@Override
	public Class<RetrieveGroupAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(RetrieveGroupAction action, RetrieveGroupResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
