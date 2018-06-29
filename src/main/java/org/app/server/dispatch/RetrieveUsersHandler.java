package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Client;
import org.app.shared.User;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.RetrieveUsersResult;
import org.app.shared.wrapper.ClientWrapper;
import org.app.shared.wrapper.UserWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RetrieveUsersHandler implements ActionHandler<RetrieveUsersAction, RetrieveUsersResult> {
	@SuppressWarnings("unchecked")
	@Override
	public RetrieveUsersResult execute(RetrieveUsersAction arg0, ExecutionContext arg1) throws ActionException {
		List<User> list = null;
		List<UserWrapper> usersList = new ArrayList<UserWrapper>();
		Session session = null;
		Transaction tx = null;
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String hql = "FROM User";
				list = (List<User>) session.createQuery(hql).list();
				tx.commit();
				List<ClientWrapper> clientList = null;
				for (User i : list) {
					clientList = new ArrayList<ClientWrapper>();
					for (Client j : i.getClient()) {
						clientList.add(new ClientWrapper(j.getName(), j.getAddress(), j.getEmail(), j.getDate()));
					}
					usersList.add(new UserWrapper(i.getName(), i.getSurname(), i.getEmail(), i.getEntryDate(),
							clientList, i.getStatus(), i.getProfile(), i.getTypeDeContrat()));
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}
		logger.info("User retrived successfylly from database");
		return new RetrieveUsersResult(usersList);
	}

	@Override
	public Class<RetrieveUsersAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(RetrieveUsersAction arg0, RetrieveUsersResult arg1, ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub

	}

}
