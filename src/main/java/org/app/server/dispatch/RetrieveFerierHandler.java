package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Ferier;
import org.app.shared.dispatch.RetrieveFerierAction;
import org.app.shared.dispatch.RetrieveFerierResult;
import org.app.shared.wrapper.FerierWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RetrieveFerierHandler implements ActionHandler<RetrieveFerierAction, RetrieveFerierResult>{

	@SuppressWarnings("unchecked")
	@Override
	public RetrieveFerierResult execute(RetrieveFerierAction action, ExecutionContext context) throws ActionException {
		List<Ferier> list = null;
		List<FerierWrapper> ferierList = new ArrayList<FerierWrapper>();
		Session session = null;
		Transaction tx = null;
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String hql = "FROM Ferier";
				list = (List<Ferier>) session.createQuery(hql).list();
				tx.commit();
				int i = 0;
				while (i < list.size()) {
					ferierList.add(new FerierWrapper(list.get(i).getDate(),list.get(i).getType(),list.get(i).getStatus()));
					i++;
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}
		logger.info("Vacation days retrived successfully from db and sent to the app");
		return new RetrieveFerierResult(ferierList);
	}

	@Override
	public Class<RetrieveFerierAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(RetrieveFerierAction action, RetrieveFerierResult result, ExecutionContext context)
			throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
