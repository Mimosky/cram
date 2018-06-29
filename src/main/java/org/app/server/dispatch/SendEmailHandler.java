package org.app.server.dispatch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.app.server.database.HibernateUtil;
import org.app.shared.Conges;
import org.app.shared.User;
import org.app.shared.dispatch.SendEmailAction;
import org.app.shared.dispatch.SendEmailResult;
import org.app.toolbox.SendMail;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SendEmailHandler implements ActionHandler<SendEmailAction, SendEmailResult>{

	@Override
	public SendEmailResult execute(SendEmailAction action, ExecutionContext context) throws ActionException {
		Session session = null;
		Conges conges = null;
		
		JSONParser parser = new JSONParser();
		JSONObject data;
		String emailContact = "";
		try {
			data = (JSONObject) parser.parse(new FileReader("/home/olcya/config/init.json"));
			emailContact = (String) data.get("mail");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(action.getType()==1){
			if(action.getEmail().equals("admin"))
				SendMail.execute(emailContact, action.getSubject(), action.getBody(), "");
			else
				SendMail.execute(action.getEmail(), action.getSubject(), action.getBody(), "");
		}else{
			session = HibernateUtil.getSessionFactory().openSession();
			boolean userFound = false;
			String email = "";
			String name = "";
			while( !userFound){
				String hql = "FROM User";
				Query query = session.createQuery(hql);
				@SuppressWarnings("unchecked")
				List<User> personsList =  (List<User>) query.list();
				for (User u: personsList){
					for(Conges c: u.getConges()){
						if(c.getCongesId()==action.getType()){
							userFound = true;
							email = u.getEmail();
							name = u.getName() + " "+ u.getSurname();
							conges =c;
						}
					}
				}
			}
			session.close();
			
			

			
			String message = "<html>\n" + "<body>\n" + "<br />" +
					"Bonjour,<br />Le statut de votre congé a été modifié: <br /> \t"
					+ "Type   : "+conges.getType()  + "<br />\t"
					+ "Début  : "+action.getStart()+" "+conges.getStartD()+ "<br /> \t"
					+ "Fin    : "+action.getEnd()+" "+conges.getEndD()+"<br /> \t"
					+ "Statut : "+conges.getStatus()+"<br /> \t"
					+ action.getSender()
					+"\n" + "</body>\n" + "</html>";
			SendMail.execute(email, "Demande d'abscence", message, "");
			
			message = "<html>\n" + "<body>\n" + "<br />" +
					"Bonjour,<br />Le statut de congé de "+ name +" a été modifié: <br /> \t"
					+ "Type   : "+conges.getType()  + "<br />\t"
					+ "Début  : "+action.getStart()+" "+conges.getStartD()+ "<br /> \t"
					+ "Fin    : "+action.getEnd()+" "+conges.getEndD()+"<br /> \t"
					+ "Statut : "+conges.getStatus()+"<br /> \t"
					+ action.getSender()
					+"\n" + "</body>\n" + "</html>";
			SendMail.execute(emailContact, "Demande d'abscence "+name, message, "");
		}
		return new SendEmailResult("ok");
	}

	@Override
	public Class<SendEmailAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(SendEmailAction action, SendEmailResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub
		
	}

}
