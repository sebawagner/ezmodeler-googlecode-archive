package org.i4change.app.servlets.services;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.i4change.app.data.basic.AuthLevelmanagement;
import org.i4change.app.data.basic.Sessionmanagement;
import org.i4change.app.data.domain.daos.OrganisationDaoImpl;
import org.i4change.app.data.domain.daos.OrganisationUserDaoImpl;
import org.i4change.app.data.user.daos.UserDaoImpl;
import org.i4change.app.hibernate.beans.adresses.Adresses_Emails;
import org.i4change.app.hibernate.beans.domain.Organisation;
import org.i4change.app.hibernate.beans.domain.Organisation_Users;
import org.i4change.app.hibernate.beans.user.Users;
import org.i4change.app.servlets.Export;
import org.i4change.app.utils.math.CalendarPatterns;

public class ExportService implements IExportService {

	private static final Log log = LogFactory.getLog(ExportService.class);

	//Spring loaded beans
	private UserDaoImpl userDaoImpl = null;
	private Sessionmanagement sessionmanagement = null;
	private OrganisationDaoImpl organisationDaoImpl = null;
	private OrganisationUserDaoImpl organisationUserDaoImpl = null;
	
	public UserDaoImpl getUserDaoImpl() {
		return userDaoImpl;
	}

	public void setUserDaoImpl(UserDaoImpl userDaoImpl) {
		this.userDaoImpl = userDaoImpl;
	}

	public Sessionmanagement getSessionmanagement() {
		return sessionmanagement;
	}

	public void setSessionmanagement(Sessionmanagement sessionmanagement) {
		this.sessionmanagement = sessionmanagement;
	}

	public OrganisationDaoImpl getOrganisationDaoImpl() {
		return organisationDaoImpl;
	}

	public void setOrganisationDaoImpl(OrganisationDaoImpl organisationDaoImpl) {
		this.organisationDaoImpl = organisationDaoImpl;
	}

	public OrganisationUserDaoImpl getOrganisationUserDaoImpl() {
		return organisationUserDaoImpl;
	}

	public void setOrganisationUserDaoImpl(
			OrganisationUserDaoImpl organisationUserDaoImpl) {
		this.organisationUserDaoImpl = organisationUserDaoImpl;
	}

	/* (non-Javadoc)
	 * @see org.i4change.app.servlets.services.IExportService#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void service(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {

		try {
			String sid = httpServletRequest.getParameter("sid");
			if (sid == null) {
				sid = "default";
			}
			log.debug("sid: " + sid);
			
			Long users_id = sessionmanagement.checkSession(sid);
			Long user_level = userDaoImpl.getUserLevelByID(users_id);

			System.out.println("users_id: " + users_id);
			System.out.println("user_level: " + user_level);

			if (AuthLevelmanagement.checkAdminLevel(user_level)) {
				 //if (true) {
				
				String moduleName = httpServletRequest.getParameter("moduleName");
				if (moduleName == null) {
					moduleName = "moduleName";
				}
				System.out.println("moduleName: " + moduleName);
				
				if (moduleName.equals("users") || moduleName.equals("userorganisations")) {
					String organisation = httpServletRequest.getParameter("organisation");
					if (organisation == null) {
						organisation = "0";
					}
					Long organisation_id = Long.valueOf(organisation).longValue();
					System.out.println("organisation_id: " + organisation_id);

					List<Users> uList = null;
					String downloadName = "users";
					if (moduleName.equals("userorganisations")) {
						Organisation  orga = organisationDaoImpl.getOrganisationById(organisation_id);
						downloadName += "_"+orga.getName();
						uList = organisationUserDaoImpl.getUsersByOrganisationId(organisation_id);
					} else {
						uList = userDaoImpl.getAllUsers();
					}
					
					if (uList != null) {
						Document doc = this.createDocument(uList);

						String requestedFile = "users.xml";

						httpServletResponse.reset();
						httpServletResponse.resetBuffer();
						OutputStream out = httpServletResponse.getOutputStream();
						httpServletResponse.setContentType("APPLICATION/OCTET-STREAM");
						httpServletResponse.setHeader("Content-Disposition",
								"attachment; filename=\"" + downloadName
										+ ".xml\"");
						// httpServletResponse.setHeader("Content-Length", ""+
						// rf.length());

						this.serializetoXML(out, "UTF-8", doc);

						out.flush();
						out.close();
					}

				}
			} else {
				System.out.println("ERROR LangExport: not authorized FileDownload "+ (new Date()));
			}	
		} catch (Exception er) {
			log.error("ERROR ", er);
			System.out.println("Error exporting: " + er);
			er.printStackTrace();
		}
	}

	private Document createDocument(List<Users> uList) throws Exception {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		document.addComment(
				"###############################################\n" +
				"This File is auto-generated by the Backup Tool \n" +
				"you should use the BackupPanel to modify or change this file \n" +
				"see http://code.google.com/p/openmeetings/wiki/BackupPanel for Details \n" +
				"###############################################");
		
		Element root = document.addElement("root");
		
		Element users = root.addElement("users");

		for (Iterator<Users> it = uList.iterator();it.hasNext();) {
			Users u = it.next();

			Element user = users.addElement("user");

			user.addElement("age").setText(CalendarPatterns.getDateByMiliSeconds(u.getAge()));
			user.addElement("availible").setText(u.getAvailible().toString());
			user.addElement("deleted").setText(u.getDeleted());
			user.addElement("firstname").setText(u.getFirstname());
			user.addElement("lastname").setText(u.getLastname());
			user.addElement("login").setText(u.getLogin());
			user.addElement("pass").setText(u.getPassword());
			
			String pictureuri = u.getPictureuri();
			if (pictureuri != null) user.addElement("pictureuri").setText(pictureuri);
			else user.addElement("pictureuri").setText("");
			
			if ( u.getLanguage_id() != null ) user.addElement("language_id").setText(u.getLanguage_id().toString());
			else user.addElement("language_id").setText("");
				
			user.addElement("status").setText(u.getStatus().toString());
			user.addElement("regdate").setText(CalendarPatterns.getDateWithTimeByMiliSeconds(u.getRegdate()));
			user.addElement("title_id").setText(u.getTitle_id().toString());
			user.addElement("level_id").setText(u.getLevel_id().toString());
			
			user.addElement("additionalname").setText(u.getAdresses().getAdditionalname());
			user.addElement("comment").setText(u.getAdresses().getComment());
			//A User can not have a deleted Adress, you cannot delete the Adress of an User
			//String deleted = u.getAdresses().getDeleted()
			//Phone Number not done yet
			user.addElement("fax").setText(u.getAdresses().getFax());
			user.addElement("state_id").setText(u.getAdresses().getStates().getCountry_id().toString());
			user.addElement("street").setText(u.getAdresses().getStreet());
			user.addElement("town").setText(u.getAdresses().getTown());
			user.addElement("zip").setText(u.getAdresses().getZip());
			
			Element user_emails = user.addElement("emails");
			for (Iterator<Adresses_Emails> iterObj=u.getAdresses().getEmails().iterator();iterObj.hasNext();){
				Adresses_Emails addrE = iterObj.next();
				user_emails.addElement("mail").setText(addrE.getMail().getEmail());
			}
			
			Element user_organisations = user.addElement("organisations");
			//List<String> organisations = new LinkedList();
			for (Iterator<Organisation_Users> iterObj = u.getOrganisation_users().iterator();iterObj.hasNext(); ) {
				Organisation_Users orgUsers = iterObj.next();
				user_organisations.addElement("organisation_id").addText(orgUsers.getOrganisation().getOrganisation_id().toString());
			}
			
			//Not need at the moment
			//Element user_groups = user.addElement("groups");

		}

		return document;
	}	
	
	private void serializetoXML(OutputStream out, String aEncodingScheme, Document doc)
			throws Exception {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding(aEncodingScheme);
		XMLWriter writer = new XMLWriter(out, outformat);
		writer.write(doc);
		writer.flush();
	}
}
