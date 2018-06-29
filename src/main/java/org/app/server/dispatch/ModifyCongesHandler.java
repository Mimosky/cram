package org.app.server.dispatch;

import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Conges;
import org.app.shared.dispatch.ModifyCongesAction;
import org.app.shared.dispatch.ModifyCongesResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ModifyCongesHandler implements ActionHandler<ModifyCongesAction,ModifyCongesResult> {

	@Override
	public ModifyCongesResult execute(ModifyCongesAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		Conges conges = null;
		Date date = new Date();
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM Conges as conges WHERE conges.congesId ='" + action.getCongesId() + "'";
				Query query = session.createQuery(hql);
				conges  =  (Conges) query.list().get(0);
				conges.setStatus(action.getValue());
				Transaction tx = session.beginTransaction();
				session.update(conges);
				tx.commit();
				
			} catch (Exception e) {
				logger.warning("[ "+date.toString()+" ] "+e.getMessage());
			}
		} finally {
			session.close();
		}
		logger.info("[ "+date.toString()+" ] "+"Conge "+action.getCongesId()+" status changed to "+action.getValue());
		return new ModifyCongesResult("Ok");
	}

	@Override
	public Class<ModifyCongesAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(ModifyCongesAction action, ModifyCongesResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

	

}
