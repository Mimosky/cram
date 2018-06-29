package org.app.server.dispatch;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.logging.Logger;

import org.app.server.database.HibernateUtil;
import org.app.shared.User;
import org.app.shared.dispatch.GeneratePdfAction;
import org.app.shared.dispatch.GeneratePdfResult;
import org.app.shared.wrapper.WorkLoadWrapper;
import org.app.toolbox.SendMail;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePdfHandler implements ActionHandler<GeneratePdfAction, GeneratePdfResult> {

	@Override
	public GeneratePdfResult execute(GeneratePdfAction action, ExecutionContext context) throws ActionException {
		Logger logger = Logger.getLogger("MS");
		User person = null;
		Session session = null;
		Transaction tx = null;
		Date date = new Date();
		String file = "";
		//String csvFile = "";
		try {
			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();
				String hql = "FROM User as user WHERE user.email ='" + action.getEmail() + "'";
				person = (User) session.createQuery(hql).list().get(0);
				tx.commit();

				Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
				Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
				Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

				Document document = new Document();
				String path = "/home/olcya/cram/";
				file = action.getClient() + "-" + person.getName() + "-" + person.getSurname() + "-"
						+ month(action.getDate().getMonth() + 1) +"-"+(action.getDate().getYear()+1900)+ ".pdf";
				try {
					PdfWriter.getInstance(document, new FileOutputStream(path + file));
					document.open();
					document.addTitle("CRAM");
					document.addAuthor("Olcya");
					document.addCreator("Olcya");
					Image img = Image.getInstance("/home/olcya/images/olcya.png");
					img.scalePercent(40);
					Paragraph preface = new Paragraph();
					// We add one empty line
					preface.add(img);
					// Lets write a big header
					Paragraph title = new Paragraph("Compte rendu d'activité mensuel", titleFont);
					title.setAlignment(Element.ALIGN_CENTER);
					preface.add(title);
					Paragraph mois = new Paragraph(
							month(action.getDate().getMonth() + 1) + "-" + (action.getDate().getYear() + 1900),
							smallBold);
					mois.setAlignment(Element.ALIGN_CENTER);
					preface.add(mois);
					//preface.add(new Paragraph(" "));
					preface.add(new Paragraph(" "));
					Paragraph collaborateur = new Paragraph(
							"Collaborateur :" + person.getName() + " " + person.getSurname(), smallBold);
					preface.add(collaborateur);



					float congePaye = 0;
					float maladie = 0;
					float naissance = 0;
					float rtt = 0;
					float congeSansSolde = 0;
					float mariage = 0;
					float autres = 0;
					float work = 0;
					float ferie = 0;
					String clientsName[];
					// String name = "";
					for (WorkLoadWrapper w : action.getWork()) {
						clientsName = w.getClientName().split("-");
						if (clientsName.length == 1) {
							if (w.getClientName().equals("Congé payé"))
								congePaye += w.getLoad();
							else if (w.getClientName().equals("Maladie"))
								maladie += w.getLoad();
							else if (w.getClientName().equals("Naissance"))
								naissance += w.getLoad();
							else if (w.getClientName().equals("RTT"))
								rtt += w.getLoad();
							else if (w.getClientName().equals("Congé sans solde"))
								congeSansSolde += w.getLoad();
							else if (w.getClientName().equals("Mariage"))
								mariage += w.getLoad();
							else if (w.getClientName().equals("Autres"))
								autres += w.getLoad();
							else if (w.getClientName().equals("")) {

							} else if (w.getClientName().equals("Férié")) {
								ferie += 1;
							} else {
								work += w.getLoad();
							}
						} else if (clientsName.length == 2) {
							for (String name : clientsName) {
								if (name.equals("Congé payé"))
									congePaye += 0.5;
								else if (name.equals("Maladie"))
									maladie += 0.5;
								else if (name.equals("Naissance"))
									naissance += 0.5;
								else if (name.equals("RTT"))
									rtt += 0.5;
								else if (name.equals("Congé sans solde"))
									congeSansSolde += 0.5;
								else if (name.equals("Mariage"))
									mariage += 0.5;
								else if (name.equals("Autres"))
									autres += 0.5;
								else if (name.equals("")) {

								} else if (name.equals("Férié")) {
									ferie += 0.5;
								} else {
									work += 0.5;
								}
							}
						}

					}

					preface.add(new Paragraph("Récapitulatif: " +  action.getClient(), smallBold));
					if (congePaye > 0)
						preface.add(new Paragraph("Congé payé : " + congePaye, smallFont));
					if (maladie > 0)
						preface.add(new Paragraph("Maladie : " + maladie, smallFont));
					if (naissance > 0)
						preface.add(new Paragraph("Naissance : " + naissance, smallFont));
					if (rtt > 0)
						preface.add(new Paragraph("RTT : " + rtt, smallFont));
					if (congeSansSolde > 0)
						preface.add(new Paragraph("Congé sans solde : " + congeSansSolde, smallFont));
					if (mariage > 0)
						preface.add(new Paragraph("Mariage : " + mariage, smallFont));
					if (ferie > 0)
						preface.add(new Paragraph("Férié : " + ferie, smallFont));
					if (autres > 0)
						preface.add(new Paragraph("Autres : " + autres, smallFont));

					if (work > 0)
						preface.add(new Paragraph("Travaillé : " + work, smallFont));
					preface.add(new Paragraph(" "));

					document.add(preface);

					PdfPTable table = new PdfPTable(2);
					PdfPCell c1 = new PdfPCell(new Phrase("Date", subFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(c1);


					c1 = new PdfPCell(new Phrase("Valeur", subFont));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(c1);
					table.setHeaderRows(1);

					// TODO Create a list of workLoad and conges
					// then create the table from this list
					String day = "";
					String month = "";
					int compteur = 1;
					String dayLoad = "";
					PdfPCell cell;
					Date workingDate = new Date(action.getDate().getTime());

					for (WorkLoadWrapper w : action.getWork()) {

						if (w.getDayLoad().equals("J") || w.getDayLoad().equals("Journée")) {
							dayLoad = "Journée";
						} else if (w.getDayLoad().equals("M") || w.getDayLoad().equals("Matin")) {
							dayLoad = "Matin";
						} else if (w.getDayLoad().equals("A") || w.getDayLoad().equals("Après-midi")) {
							dayLoad = "Après-midi";
						} else if (w.getClientName().equals("")) {
							dayLoad = "";
						} else {
							dayLoad = w.getDayLoad();
						}
						if (compteur < 10)
							day = "0" + compteur;
						else
							day = "" + compteur;

						if (action.getDate().getMonth() + 1 < 10)
							month = "0" + (action.getDate().getMonth() + 1);
						else
							month = "" + (action.getDate().getMonth() + 1);

						workingDate.setDate(compteur);
						cell = new PdfPCell(new Phrase(day + "/" + month + "/" + (action.getDate().getYear() + 1900)));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						if (workingDate.getDay() == 0 || workingDate.getDay() == 6)
							cell.setBackgroundColor(BaseColor.GRAY);
						table.addCell(cell);
		
						cell = new PdfPCell(new Phrase(dayLoad));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						if (workingDate.getDay() == 0 || workingDate.getDay() == 6)
							cell.setBackgroundColor(BaseColor.GRAY);
						table.addCell(cell);

						compteur++;
					}
					document.add(table);

				} catch (DocumentException e) {
					logger.warning("[ "+date.toString()+" ] "+e.getMessage());
					
				} finally {
					document.close();
				}
//				csvFile = person.getName() + "-" + person.getSurname() + "-" + month(action.getDate().getMonth() + 1)
//						+(action.getDate().getYear()+1900)+ ".csv";
//
//				try {
//					PrintWriter writer = new PrintWriter(path + csvFile, "UTF-8");
//
//					// Generate The csv file
//					String clientsName[];
//					String clientsLoad[];
//					String day = "";
//					String month = "";
//					int compteur = 1;
//
//					if (action.getDate().getMonth() + 1 < 10)
//						month = "0" + (action.getDate().getMonth() + 1);
//					else
//						month = "" + (action.getDate().getMonth() + 1);
//
//					for (WorkLoadWrapper w : action.getWork()) {
//						if (compteur < 10)
//							day = "0" + compteur;
//						else
//							day = "" + compteur;
//						clientsName = w.getClientName().split("-");
//						clientsLoad = w.getDayLoad().split("-");
//						if (clientsName.length == 2) {
//							writer.println(day + "/" + month + "/" + (action.getDate().getYear() + 1900) + ";"
//									+ clientsName[0] + ";" + clientsLoad[0] + ";" + "0.5");
//							writer.println(day + "/" + month + "/" + (action.getDate().getYear() + 1900) + ";"
//									+ clientsName[1] + ";" + clientsLoad[1] + ";" + "0.5");
//						} else {
//							writer.println(day + "/" + month + "/" + (action.getDate().getYear() + 1900) + ";"
//									+ clientsName[0] + ";" + clientsLoad[0] + ";" + w.getLoad());
//						}
//						compteur++;
//					}
//
//					writer.close();
//				} catch (IOException e) {
//					logger.warning("[ "+date.toString()+" ] "+e.getMessage());
//				}


				JSONParser parser = new JSONParser();
				JSONObject data = (JSONObject) parser.parse(new FileReader("/home/olcya/config/init.json"));
				String email = (String) data.get("mail");
				SendMail.execute(email,
						"CRAM " + month(action.getDate().getMonth() + 1) + " " + person.getName() + " " + person.getSurname(),
						"Bonjour, \nVeuilez trouver ci-joint le cram du mois : " + month(action.getDate().getMonth() + 1),
						file);

			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		} finally {
			session.close();
		}
		logger.info(" Pdf is successfully generated : "+ file);
		return new GeneratePdfResult("ok");
	}

	@Override
	public Class<GeneratePdfAction> getActionType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo(GeneratePdfAction action, GeneratePdfResult result, ExecutionContext context)
			throws ActionException {
		// TODO Auto-generated method stub

	}

	protected String month(int month) {
		switch (month) {
		case 1:
			return "Janvier";
		case 2:
			return "Février";
		case 3:
			return "Mars";
		case 4:
			return "Avril";
		case 5:
			return "Mai";
		case 6:
			return "Juin";
		case 7:
			return "Juillet";
		case 8:
			return "Aout";
		case 9:
			return "Septembre";
		case 10:
			return "Octobre";
		case 11:
			return "Novembre";
		case 12:
			return "Decembre";
		default:
			return "Invalid month";
		}
	}

}
