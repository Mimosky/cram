package org.app.server.dispatch;

import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.Client;
import org.app.shared.dispatch.AddClientAction;
import org.app.shared.dispatch.AddClientResult;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class AddClientHandler implements ActionHandler<AddClientAction, AddClientResult> {

	@Override
	public AddClientResult execute(AddClientAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		boolean added = false;
		Logger logger = Logger.getLogger("MS");
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM Client as client WHERE client.name ='" + action.getName() + "'";
				Query query = session.createQuery(hql);
				if (query.list().size() == 0){
					Client client = new Client(action.getName(), 
							action.getAddress(),
							action.getEmail(),
							new Date(action.getDate()));
					Transaction tx = session.beginTransaction();
					session.save(client);
					tx.commit();
					added = true;
				}

			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}

		if(added){
			logger.info("Client "+ action.getName()+" added successfully");
			return new AddClientResult("ok");
		}
			
		else{
			logger.warning("Client "+ action.getName()+" is not added");
			return new AddClientResult("Nok");
		}
			
		
	}

	@Override
	public Class<AddClientAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(AddClientAction arg0, AddClientResult arg1, ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
