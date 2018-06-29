package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Cram;
import org.app.shared.User;
import org.app.shared.WorkLoad;
import org.app.shared.dispatch.SaveCramAction;
import org.app.shared.dispatch.SaveCramResult;
import org.app.shared.wrapper.WorkLoadWrapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveCramHandler implements ActionHandler<SaveCramAction, SaveCramResult> {

	@Override
	public SaveCramResult execute(SaveCramAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		Cram cram = new Cram();
		Collection<WorkLoad> workLoad = new ArrayList<WorkLoad>();
		boolean cramFound = false;
		boolean added = false;
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
				Query query = session.createQuery(hql);
				if (query.list().size() != 0) {
					User person = (User) query.list().get(0);
					for (WorkLoadWrapper w : action.getCram().getWorkList()) {
						workLoad.add(new WorkLoad(w.getClientName(), w.getDayLoad(), w.getLoad()));
					}
					// Start a query to fetch right cram and update it if exists
					// otherwise create it
					for (Cram m : person.getCram()) {
						if ((m.getDate().getYear() == action.getCram().getDate().getYear())
								&& (m.getDate().getMonth() == action.getCram().getDate().getMonth())) {
							m.setWorkList(workLoad);
							m.setDate(action.getCram().getDate());
							Transaction tx = session.beginTransaction();
							session.update(m);
							session.update(person);
							tx.commit();
							cramFound = true;
						}
					}
					if (!cramFound) {
						cram.setWorkList(workLoad);
						cram.setDate(action.getCram().getDate());
						person.getCram().add(cram);
						Transaction tx = session.beginTransaction();
						session.save(cram);
						session.update(person);
						tx.commit();
					}
					added = true;
				}

			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}
		if (added){
			logger.info("Cram of user "+action.getEmail() +" is saved successfully");
			return new SaveCramResult("Cram Added Succefully");
		}else{
			logger.warning("Something went wrong when saving Cram of user "+ action.getEmail());
			return new SaveCramResult("User does not exists");
		}
	}

	@Override
	public void undo(SaveCramAction action, SaveCramResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<SaveCramAction> getActionType() {
		return SaveCramAction.class;
	}

}
