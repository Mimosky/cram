package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.app.server.database.HibernateUtil;
import org.app.shared.Client;
import org.app.shared.Cram;
import org.app.shared.User;
import org.app.shared.WorkLoad;
import org.app.shared.dispatch.UserInformationAction;
import org.app.shared.dispatch.UserInformationResult;
import org.app.shared.wrapper.ClientWrapper;
import org.app.shared.wrapper.CramWrapper;
import org.app.shared.wrapper.UserWrapper;
import org.app.shared.wrapper.WorkLoadWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class UserInformationHandler implements ActionHandler<UserInformationAction, UserInformationResult> {

	@Override
	public UserInformationResult execute(UserInformationAction action, ExecutionContext context)
			throws ActionException {
		Session session = null;
		UserWrapper userWrapper = null;
		Transaction tx;
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
				tx = session.beginTransaction();
				User user = (User) session.createQuery(hql).list().get(0);
				tx.commit();
				// Convert Cram to CramWrapper
				Date date;
				if(action.getYear() == -1){
					 date = new Date();
				}else{
					date = new Date();
					date.setMonth(action.getMonth());
					date.setYear(action.getYear());
				}
				CramWrapper cramWrapper = new CramWrapper();
				for (Cram itr : user.getCram()) {
					if ((itr.getDate().getMonth() == date.getMonth()) && (itr.getDate().getYear() == date.getYear())) {
						for (WorkLoad work : itr.getWorkList()) {
							cramWrapper.getWorkList()
									.add(new WorkLoadWrapper(work.getClientName(), work.getDayLoad(), work.getLoad()));
						}
						cramWrapper.setDate(date);
					}
				}
				Collection<CramWrapper> cramWrapperList = new ArrayList<CramWrapper>();
				cramWrapperList.add(cramWrapper);
				// Convert Client to ClientWrapper
				List<ClientWrapper> clientList = new ArrayList<ClientWrapper>();
				for (Client j : user.getClient()) {
					clientList.add(new ClientWrapper(j.getName(), j.getAddress(), j.getEmail(), j.getDate()));
				}
				userWrapper = new UserWrapper(user.getName(), user.getSurname(), user.getEmail(), user.getEntryDate(),
						clientList, cramWrapperList);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} finally {
			session.close();
		}
		return new UserInformationResult(userWrapper);
	}

	@Override
	public Class<UserInformationAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(UserInformationAction arg0, UserInformationResult arg1, ExecutionContext arg2)
			throws ActionException {
		// TODO Auto-generated method stub

	}

}
