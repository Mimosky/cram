package org.app.server.dispatch;

import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Ferier;
import org.app.shared.dispatch.AddFerierAction;
import org.app.shared.dispatch.AddFerierResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class AddFerierHandler implements ActionHandler<AddFerierAction,AddFerierResult>{

	@Override
	public AddFerierResult execute(AddFerierAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		boolean added = false;
		Date date = new Date();
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM Ferier as ferier WHERE ferier.date ='" + action.getDate() + "'";
				Query query = session.createQuery(hql);
				if (query.list().size() == 0){
					Ferier jour = new Ferier(action.getDate(),action.getType(),"enabled");
					Transaction tx = session.beginTransaction();
					session.save(jour);
					tx.commit();
					added =true;
				}

			} catch (Exception e) {
				logger.warning("[ "+date.toString()+" ] "+e.getMessage());
			}
		} finally {
			session.close();
		}

		if(added){
			logger.info("[ "+date.toString()+" ] "+"Férié "+ action.getDate()+" added successfully");
			return new AddFerierResult("Ferier added Succefully");
		}else{
			logger.warning("[ "+date.toString()+" ] "+"Férié "+ action.getDate()+" was not added");
			return new AddFerierResult("Ferier already exists");
		}
	}

	@Override
	public Class<AddFerierAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(AddFerierAction action, AddFerierResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
