package org.app.server.dispatch;

import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Conges;
import org.app.shared.User;
import org.app.shared.dispatch.SaveCongesAction;
import org.app.shared.dispatch.SaveCongesResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveCongesHandler implements ActionHandler<SaveCongesAction, SaveCongesResult> {

	@Override
	public SaveCongesResult execute(SaveCongesAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		Conges conges;
		boolean added = false;
		Logger logger = Logger.getLogger("MS");
		Date date = new Date();
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
				Query query = session.createQuery(hql);
				User person = (User) query.list().get(0);
				conges = new Conges(action.getConges().getType(), action.getConges().getStartDate(),
						action.getConges().getEndDate(), action.getConges().getStartD(), action.getConges().getEndD());
				conges.setStatus("En Cours");
				person.getConges().add(conges);
				Transaction tx = session.beginTransaction();
				session.save(conges);
				session.update(person);
				tx.commit();
				added = true;

			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}

		if(added){
			logger.info("Vacation "+action.getConges().getType()+" of user "+ action.getEmail()+" was added successfully");
			return new SaveCongesResult("Conges Added Succefully");
		}else{
			logger.warning("Vacation "+action.getConges().getType()+" of user "+ action.getEmail()+" was not added");
			return new SaveCongesResult("Nok");
		}
	}

	@Override
	public Class<SaveCongesAction> getActionType() {
		return SaveCongesAction.class;
	}

	@Override
	public void undo(SaveCongesAction arg0, SaveCongesResult arg1, ExecutionContext arg2) throws ActionException {
	}

}
