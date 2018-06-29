package org.app.server.dispatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.app.server.database.HibernateUtil;
import org.app.shared.Conges;
import org.app.shared.User;
import org.app.shared.dispatch.RetrieveCongesAction;
import org.app.shared.dispatch.RetrieveCongesResult;
import org.app.shared.wrapper.CongesWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RetrieveCongesHandler implements ActionHandler<RetrieveCongesAction, RetrieveCongesResult> {

	@SuppressWarnings("unchecked")
	@Override
	public RetrieveCongesResult execute(RetrieveCongesAction action, ExecutionContext context) throws ActionException {
		List<User> list = null;
		User person;
		List<CongesWrapper> congesList = new ArrayList<CongesWrapper>();
		Session session = null;
		Transaction tx = null;

		try {
			try {

				if (action.getEmail().equals("getAll")) {
					session = HibernateUtil.getSessionFactory().openSession();
					String hql = "FROM User";
					tx = session.beginTransaction();
					list = (List<User>) session.createQuery(hql).list();
					tx.commit();
					for (User u : list) {
						for (Conges c : u.getConges()) {
							if (c.getStatus().equals("En Cours")) {
								congesList.add(new CongesWrapper(c.getCongesId(), c.getType(), c.getStartDate(),
										c.getEndDate(), c.getStartD(), c.getEndD(), c.getStatus(),
										u.getName() + " " + u.getSurname()));
							}
						}
					}

				} else {
					session = HibernateUtil.getSessionFactory().openSession();
					String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
					tx = session.beginTransaction();
					person = (User) session.createQuery(hql).list().get(0);
					tx.commit();
					for (Conges c : person.getConges()) {
						if (!c.getStatus().equals("deleted"))
							// The last argument is the name, I dont need it
							// here
							congesList.add(new CongesWrapper(c.getCongesId(), c.getType(), c.getStartDate(),
									c.getEndDate(), c.getStartD(), c.getEndD(), c.getStatus(), ""));
					}
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} finally {
			session.close();
		}

		double[] x = countConges(congesList);

		//System.out.println(x[0] + " " + x[1]);
		return new RetrieveCongesResult(congesList, x[0],x[1],0.0, 0.0,x[2],0.0,x[3],0.0);
	}

	@Override
	public Class<RetrieveCongesAction> getActionType() {
		return RetrieveCongesAction.class;
	}

	@Override
	public void undo(RetrieveCongesAction arg0, RetrieveCongesResult arg1, ExecutionContext arg2)
			throws ActionException {
		// TODO Auto-generated method stub

	}

	private double[] countConges(List<CongesWrapper> conges) {
		List <CongesWrapper> list = new ArrayList<CongesWrapper>();
		List <CongesWrapper> currentList = new ArrayList<CongesWrapper>();
		for(CongesWrapper c: conges){
			list.add(new CongesWrapper(c.getType(), new Date(c.getStartDate().getTime()),new Date( c.getEndDate().getTime()), c.getStartD(), c.getEndD(), c.getStatus()));
			currentList.add(new CongesWrapper(c.getType(), new Date(c.getStartDate().getTime()),new Date( c.getEndDate().getTime()), c.getStartD(), c.getEndD(), c.getStatus()));
		}
		double currentCp = 0;
		double rtt = 0;
		double allConges = 0;
		double previousCp = 0;

		double[] x;

		// Define the dates
		Date nextDate = new Date();
		nextDate.setHours(0);
		nextDate.setMinutes(0);
		nextDate.setSeconds(0);
		
		Date nextDateMinBound = new Date();
		nextDateMinBound.setHours(0);
		nextDateMinBound.setMinutes(0);
		nextDateMinBound.setSeconds(0);
		
		Date tmpDate = new Date();
		tmpDate.setMonth(5);
		tmpDate.setDate(1);
		tmpDate.setHours(0);
		tmpDate.setMinutes(0);
		tmpDate.setSeconds(0);
		
		Date lastDate = new Date();
		lastDate.setHours(0);
		lastDate.setMinutes(0);
		lastDate.setSeconds(0);
		
		Date n_1LastDate = new Date();
		n_1LastDate.setHours(0);
		n_1LastDate.setMinutes(0);
		n_1LastDate.setSeconds(0);
		
		Date n_1NextDate = new Date();
		n_1NextDate.setHours(0);
		n_1NextDate.setMinutes(0);
		n_1NextDate.setSeconds(0);


		Date currentDate = new Date();
		if (currentDate.before(tmpDate)) {
			nextDate.setMonth(5);
			nextDate.setDate(1);
			
			nextDateMinBound.setMonth(4);
			nextDateMinBound.setDate(31);

			lastDate.setYear(lastDate.getYear() - 1);
			lastDate.setMonth(4);
			lastDate.setDate(31);

			n_1NextDate.setYear(lastDate.getYear() - 1);
			n_1NextDate.setMonth(5);
			n_1NextDate.setDate(1);

			n_1LastDate.setYear(n_1LastDate.getYear() - 2);
			n_1LastDate.setMonth(4);
			n_1LastDate.setDate(31);

		} else {
			nextDate.setYear(nextDate.getYear() + 1);
			nextDate.setMonth(5);
			nextDate.setDate(1);
			
			nextDateMinBound.setYear(nextDateMinBound.getYear() + 1);
			nextDateMinBound.setMonth(4);
			nextDateMinBound.setDate(31);

			lastDate.setMonth(4);
			lastDate.setDate(31);

			n_1NextDate.setMonth(5);
			n_1NextDate.setDate(1);

			n_1LastDate.setYear(n_1LastDate.getYear() - 1);
			n_1LastDate.setMonth(4);
			n_1LastDate.setDate(31);
		}

		for (CongesWrapper c : list) {
			// The vacation starts and ends in this year
			System.out.println(c.getStartDate().toString());
			System.out.println(c.getEndDate().toString());
			c.getEndDate().setHours(1);
			if ((c.getStartDate().after(lastDate)) && (c.getEndDate().before(nextDate))) {
				System.out.println("The vacation starts and ends in this year");
				x = count(c);
				currentCp += x[0];
			}
			// The vacation started and ended in the previous year
			else if (c.getStartDate().after(n_1LastDate) && c.getEndDate().before(n_1NextDate)) {
				System.out.println(c.getEndDate().toString());
				System.out.println(n_1NextDate.toString());
				System.out.println("The vacation started and ended in the previous year");
				x = count(c);
				previousCp += x[0];
			}
			// The vacation starts this year and ends next year
			else if ((c.getStartDate().before(nextDate)) && (c.getEndDate().after(nextDateMinBound))) {
				System.out.println("The vacation starts this year and ends next year");
				nextDate.setMonth(4);
				nextDate.setDate(31);
				c.setEnd(nextDate);
				x = count(c);
				currentCp += x[0];
				nextDate.setMonth(5);
				nextDate.setDate(1);
			}
			// The vacation started previous year and ends this year
			else if ((c.getStartDate().before(n_1NextDate)) && (c.getEndDate().after(lastDate))) {
				System.out.println("The vacation started previous year and ends this year");
				Date tmp = new Date(c.getStartDate().getTime());
				c.setStart(n_1NextDate);
				x = count(c);
				currentCp += x[0];
				c.setEnd(lastDate);
				c.setStart(tmp);
				x = count(c);
				previousCp += x[0];
				

			}
			// The vacation started two years ago and ended last year
			else if (!(c.getStartDate().after(n_1LastDate)) && (c.getEndDate().after(n_1LastDate))) {
				System.out.println("The vacation started two years ago and ended last year");
				n_1LastDate.setMonth(5);
				n_1LastDate.setDate(1);
				c.setStart(n_1LastDate);
				x = count(c);
				previousCp += x[0];
				n_1LastDate.setMonth(4);
				n_1LastDate.setDate(31);
				
			}
			else{
				System.out.println("Neither of the above condition was satisfyed");
			}
		}
		// Working in current year starting from 01/01 to 31/12
		Date firstDayOfTheYear = new Date();
		firstDayOfTheYear.setMonth(0);
		firstDayOfTheYear.setDate(1);
		
		
		Date lastDayOfTheYear = new Date();
		lastDayOfTheYear.setMonth(11);
		lastDayOfTheYear.setDate(31);
		double[] y = {};
		for(CongesWrapper c : currentList){
			// Conges in the current year
			if((c.getStartDate().after(firstDayOfTheYear)) &&(c.getEndDate().before(lastDayOfTheYear))){
				y = count(c);
				rtt += y[1];
				System.out.println("Middel of the year");
				allConges +=y[2];
			}else if((c.getStartDate().before(firstDayOfTheYear))&&((c.getEndDate().before(lastDayOfTheYear)))){
				c.getStartDate().setTime(firstDayOfTheYear.getTime());
				y = count(c);
				rtt += y[1];
				System.out.println("From Previous Year");
				allConges +=y[2];
			}else if((c.getStartDate().before(lastDayOfTheYear))&&(c.getEndDate().after(lastDayOfTheYear))){
				c.getEndDate().setTime(lastDayOfTheYear.getTime());
				System.out.println("To the next Year");
				y = count(c);
				rtt += y[1];
				allConges +=y[2];
			}
		}
		
		
		return new double[] { currentCp, previousCp, rtt, allConges };
	}

	private double[] count(CongesWrapper c) {
		double cp = 0;
		double rtt = 0;
		double allConges = 0;
		int weCp = 0;
		int weRtt = 0;
		int weAllConges = 0;

		// Compute the week-ends
		Date tmpDate = new Date(c.getStartDate().getTime());
		while (tmpDate.before(c.getEndDate())) {

			if ((tmpDate.getDay() == 0) || (tmpDate.getDay() == 6)) {
				if (c.getType().equals("Congé payé")) {
					weCp ++;
					weAllConges ++;
				} else if (c.getType().equals("RTT")) {
					weRtt ++;
					weAllConges ++;
				}else{
					weAllConges ++;
				}
			}
			tmpDate.setTime(tmpDate.getTime() + 86400000);

		}

		// Check if the last day is within the week-end
//		if ((c.getEndDate().getDay() == 0) || (c.getEndDate().getDay() == 6)) {
//			if (c.getType().equals("Congé payé")) {
//				weCp ++;
//				weAllConges ++;
//			} else if (c.getType().equals("RTT")) {
//				weRtt ++;
//				weAllConges ++;
//			}else {
//				weAllConges ++;
//			}
//		}

		// Check if the vacation starts and ends in the same month
		if (c.getEndDate().getMonth() == c.getStartDate().getMonth()) {
			if (c.getEndD().equals(c.getStartD())) {
				if (c.getType().equals("Congé payé")) {
					cp += (double) c.getEndDate().getDate() - c.getStartDate().getDate();
					cp += 0.5;
					allConges += (double) c.getEndDate().getDate() - c.getStartDate().getDate();
					allConges += 0.5;
				} else if (c.getType().equals("RTT")) {
					rtt += (double) c.getEndDate().getDate() - c.getStartDate().getDate();
					rtt += 0.5;
					allConges += (double) c.getEndDate().getDate() - c.getStartDate().getDate();
					allConges += 0.5;
				}else{
					allConges += (double) c.getEndDate().getDate() - c.getStartDate().getDate();
					allConges += 0.5;
				}
			} else {
				if (c.getType().equals("Congé payé")) {
					cp += (double) c.getEndDate().getDate() - c.getStartDate().getDate() + 1;
					allConges += (double) c.getEndDate().getDate() - c.getStartDate().getDate() + 1;
				} else if (c.getType().equals("RTT")) {
					rtt += (double) c.getEndDate().getDate() - c.getStartDate().getDate() + 1;
					allConges += (double) c.getEndDate().getDate() - c.getStartDate().getDate() + 1; 
				}else{
					allConges += (double) c.getEndDate().getDate() - c.getStartDate().getDate() + 1;
				}

			}
		}
		// If the vacation is spreat in two months
		else {
			if (c.getEndD().equals(c.getStartD())) {
				if (c.getType().equals("Congé payé")) {
					cp += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate();
					cp += 0.5;
					allConges += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate();
					allConges += 0.5;
				} else if (c.getType().equals("RTT")) {
					rtt += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate();
					rtt += 0.5;
					allConges += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate();
					allConges += 0.5;
				}else {
					allConges += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate();
					allConges += 0.5;
				}
			} else {
				if (c.getType().equals("Congé payé")) {
					cp += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate() + c.getEndDate().getDate()+ 1;
					allConges += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate() + c.getEndDate().getDate()+ 1;
				} else if (c.getType().equals("RTT")) {
					rtt += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate() + 1;
					allConges += (double) monthLength(c.getStartDate()) - c.getStartDate().getDate()+ c.getEndDate().getDate() + 1;
				}
			}
		}

		cp -= weCp;
		rtt -= weRtt;
		allConges -= weAllConges;

		return new double[] { cp, rtt, allConges };
	}

	protected int monthLength(Date date) {
		int listLength;
		// Année Bissextile
		if (date.getMonth() == 1 && (date.getYear() % 4) == 0) {
			listLength = 29;
		} else if (date.getMonth() == 1 && (date.getYear() % 4) != 0) {
			listLength = 28;
		} else if (date.getMonth() == 0 || (date.getMonth() == 2) || (date.getMonth() == 4) || (date.getMonth() == 6)
				|| (date.getMonth() == 7) || (date.getMonth() == 9) || (date.getMonth() == 11)) {
			listLength = 31;
		} else {
			listLength = 30;
		}
		return listLength;
	}

}
