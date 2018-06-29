package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Client;
import org.app.shared.dispatch.RetrieveClientAction;
import org.app.shared.dispatch.RetrieveClientResult;
import org.app.shared.wrapper.ClientWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RetrieveClientHandler implements ActionHandler<RetrieveClientAction, RetrieveClientResult> {
	@SuppressWarnings("unchecked")
	@Override
	public RetrieveClientResult execute(RetrieveClientAction arg0, ExecutionContext arg1) throws ActionException {
		List<Client> list = null;
		List<ClientWrapper> clientList = new ArrayList<ClientWrapper>();
		Session session = null;
		Transaction tx = null;
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String hql = "FROM Client";
				list = (List<Client>) session.createQuery(hql).list();
				tx.commit();
				int i = 0;
				while (i < list.size()) {
					clientList.add(new ClientWrapper(list.get(i).getName(), list.get(i).getAddress(),
							list.get(i).getEmail(), list.get(i).getDate()));
					i++;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}
		logger.info("Clients retrieved successfully from the database and sent back to the app");
		return new RetrieveClientResult(clientList);
	}

	@Override
	public Class<RetrieveClientAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(RetrieveClientAction arg0, RetrieveClientResult arg1, ExecutionContext arg2)
			throws ActionException {
		// TODO Auto-generated method stub

	}
}
