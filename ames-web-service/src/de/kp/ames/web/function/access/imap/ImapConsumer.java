package de.kp.ames.web.function.access.imap;
/**
 * This Java module is part of the
 *  Application Developer Framework
 *
 *  Project: AMES-Web-Service
 *  Package: de.kp.ames.web.function.access.imap
 *  Module: ImapConsumer
 *  @author krusche@dr-kruscheundpartner.de
 *
 * Add your semantic annotations within the SemanticAssist tags and
 * mark them with a leading hashtag #:
 *
 * <SemanticAssist>
 *     #access #consumer #function #imap #web
 * </SemanticAssist>
 *
 */

/**
 *	Copyright 2012 Dr. Krusche & Partner PartG
 *
 *	AMES-Web-Service is free software: you can redistribute it and/or 
 *	modify it under the terms of the GNU General Public License 
 *	as published by the Free Software Foundation, either version 3 of 
 *	the License, or (at your option) any later version.
 *
 *	AMES- Web-Service is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 *  See the GNU General Public License for more details. 
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import de.kp.ames.web.GlobalConstants;
import de.kp.ames.web.core.util.FileUtil;
import de.kp.ames.web.shared.constants.JaxrConstants;

public class ImapConsumer {
	
	private static final int BUFSIZE     = 8192; // 8K
	private static final int STRBLD_SIZE = 32768; // 32K

	/*
	 * Reference to Mail Store
	 */
	private Store store;
	
	/**
	 * Constructor
	 * 
	 * @param jAccessor
	 */
	public ImapConsumer(JSONObject jAccessor) {
		
		try {
			/*
			 * unpack Slots
			 */
			JSONObject jSlots = new JSONObject(jAccessor.getString(JaxrConstants.RIM_SLOT));

			/*
			 * Access parameters
			 */
			String host = jSlots.getString(JaxrConstants.SLOT_ENDPOINT);
			String port = jSlots.getString(JaxrConstants.SLOT_PORT);
			
			/*
			 * Credentials
			 */
			String alias   = jSlots.getString(JaxrConstants.SLOT_ALIAS);
			String keypass = jSlots.getString(JaxrConstants.SLOT_KEYPASS);

			
			/*
			 * Authenticator & Session
			 */
			Session session = createSession(host, port, alias, keypass);
			store = session.getStore(ImapConstants.DEFAULT_PROTOCOL_VALUE); 

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param host
	 * @param port
	 * @param alias
	 * @param keypass
	 */
	public ImapConsumer(String host, String port, final String alias, final String keypass) {
		
		/*
		 * Authenticator & Session
		 */
		Session session = createSession(host, port, alias, keypass);
	
		try {
			store = session.getStore(ImapConstants.DEFAULT_PROTOCOL_VALUE); 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}
	
	/**
	 * @param host
	 * @param port
	 * @param alias
	 * @param keypass
	 * @return
	 */
	private Session createSession(String host, String port, final String alias, final String keypass) {

		Properties props = setProperties(host, port);
		
		/*
		 * Authenticator & Session
		 */
		Session session = Session.getInstance(props, new Authenticator() { 
	          @Override protected PasswordAuthentication getPasswordAuthentication() { 
	            return new PasswordAuthentication(alias, keypass); 
	          } 
		});
		
		return session;
		
	}
	
	/**
	 * @param host
	 * @param port
	 * @return
	 */
	private Properties setProperties(String host, String port) {

		Properties props = new Properties();
		
		/*
		 * Host
		 */
		props.put(ImapConstants.IMAP_HOST, host);

		/* 
		 * Port
		 */
		if (port == null) port = ImapConstants.DEFAULT_PORT_VALUE;
		props.put(ImapConstants.IMAP_PORT, port);
		
		/* 
		 * Protocol
		 */
		props.put(ImapConstants.IMAP_PROTOCOL, ImapConstants.DEFAULT_PROTOCOL_VALUE);

		return props;
		
	}
	
	/**
	 * Retrieve mail messages in a JSON representation
	 * 
	 * @return
	 */
	public JSONArray getJMessages() {
		
		JSONArray jMessages = new JSONArray();
		if (store == null) return jMessages;
		
		try {
			/*
			 * Connect to IMAP store
			 */
		    store.connect(); 
		    
		    /*
		     * Retrieve & open INBOX folder
		     */
		    Folder folder = store.getFolder(ImapConstants.INBOX); 
		    folder.open(Folder.READ_ONLY); 
		    
		    Message[] messages = folder.getMessages(); 
		    for ( int i = 0; i < messages.length; i++ ) { 

		    	Message m = messages[i]; 

		    	JSONObject jMessage = new JSONObject();

		    	jMessage.put(ImapConstants.J_KEY, ""); // introduced to be compatible with posted emails		    	
		    	jMessage.put(ImapConstants.J_ID, i);   // message identifier to retrieve from external server
		    	
		    	jMessage.put(ImapConstants.J_SUBJECT, m.getSubject());		      
		    	jMessage.put(ImapConstants.J_DATE,    m.getSentDate());
		    	
		    	String from = "";

		    	Address[]  addresses = m.getFrom();
		    	for (Address address:addresses) {
		    		
		    		InternetAddress internetAddress = (InternetAddress)address;
		    		from = internetAddress.getPersonal();
		    		
		    	}
		    	
		    	jMessage.put(ImapConstants.J_FROM, from);
		    	
		    	FileUtil attachment = getAttachment(m);
		    	if (attachment == null) {
		    		jMessage.put(ImapConstants.J_ATTACHMENT, false);
		    	
		    	} else {
		    		jMessage.put(ImapConstants.J_ATTACHMENT, true);
		    		
		    	}
		    	
		    	jMessages.put(jMessages.length(), jMessage);
		      

		    } 

		    folder.close( false ); 
		    store.close(); 
		    
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {}

		return jMessages;
	}
	
	/**
	 * Retrieve attachment from mail
	 * 
	 * @param m
	 * @return
	 */
	private FileUtil getAttachment(Message m) {

		FileUtil file = null;

		try {
		    
			Object content = m.getContent();
		    if (content instanceof Multipart) {
		    	file = handleMultipart((Multipart)content, false);
	  
		    } else {
		    	file = handlePart(m, false);
		    
		    }
		
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return file;
		
	}
	
	/**
	 * Retrieve attachment from message id
	 * 
	 * @param mid
	 * @return
	 */
	public FileUtil getAttachment(int mid) {
		
		FileUtil file = null;		
		if (store == null) return file;
		
		try {
			/*
			 * Connect to IMAP store
			 */
		    store.connect(); 

		    /*
		     * Retrieve & open INBOX folder
		     */
		    Folder folder = store.getFolder(ImapConstants.INBOX); 
		    folder.open(Folder.READ_ONLY); 
		    
		    Message[] messages = folder.getMessages(); 		    
		    Message m = null;

		    /* 
		     * Message message = folder.getMessage(mid); 
		     * 
		     * the method 'getMessage' always returns an
		     * index out of bound error, so we have to
		     * introduce the hack below
		     */
		    
		    for (int i=0; i < messages.length; i++) {		    	
		    	if (i == mid) m = messages[i];		    	
		    }
		    
		    if (m == null) return file;

		    Object content = m.getContent();
		    if (content instanceof Multipart) {
		    	file = handleMultipart((Multipart)content, true);
	  
		    } else {
		    	file = handlePart(m, true);
		    
		    }

		    folder.close( false ); 
		    store.close(); 
		    
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return file;
		
	}

	/**
	 * Retrieve HTML representation of mail message
	 * 
	 * @param mid
	 * @return
	 */
	public String getHTMLMessage(int mid) {
		
		String html = null;		
		if (store == null) return html;
		
		try {
			/*
			 * Connect to IMAP store
			 */
		    store.connect(); 

		    /*
		     * Retrieve & open INBOX folder
		     */
		    Folder folder = store.getFolder(ImapConstants.INBOX); 
		    folder.open(Folder.READ_ONLY); 
		    
		    Message[] messages = folder.getMessages(); 		    
		    Message m = null;
		    
		    for (int i=0; i < messages.length; i++) {		    	
		    	if (i == mid) m = messages[i];		    	
		    }
		    
		    if (m == null) return html;
		    html = mail2HTML(m);

		    folder.close( false ); 
		    store.close(); 
		    
		} catch (Exception e) {
			e.printStackTrace();
			
		}

		
		return html;
		
	}

	/**
	 * @param m
	 * @return
	 * @throws Exception
	 */
	private String getBody(Message m) throws Exception {		
		return messageFromPart((MimeMessage)m);	    
	}
	
	/**
	 * @param mp
	 * @return
	 * @throws Exception
	 */
	private String messageFromMultipart(Multipart mp) throws Exception {
		
		String message = "";
		
		int count = mp.getCount();
		for (int i = 0; i < count; i++) {

			Part part = mp.getBodyPart(i);
			String text = messageFromPart(part);	
			
			if (!text.equals("")) message = text;
		}

		return message;
	
	}

	/**
	 * @param part
	 * @return
	 * @throws Exception
	 */
	private String messageFromPart(Part part) throws Exception {
	
		if (part.isMimeType(GlobalConstants.MT_TEXT)) {
			
			String text = readPart(part);
			return text;
			
		} else if (part.isMimeType(GlobalConstants.MT_HTML)) {

			String text = readPart(part);
			return text;
			
		} else if (part.isMimeType(GlobalConstants.MT_MULTIPART) ) {
			
			Multipart mp = (Multipart) part.getContent();
			return messageFromMultipart(mp);
		
		}
		
		return "";
		
	}
	
	/**
	 * @param part
	 * @return
	 * @throws MessagingException
	 */
	private String readPart(Part part) throws MessagingException {

		try {
			return readStream(part.getInputStream());
			
		} catch (IOException e) {

			/* 
			 * Try to get message from raw input stream
			 */
			final InputStream stream;
			
			if (part instanceof MimeBodyPart) {
				
				final MimeBodyPart mimeBodyPart = (MimeBodyPart) part;
				stream = mimeBodyPart.getRawInputStream();

			} else if (part instanceof MimeMessage) {

				final MimeMessage mm = (MimeMessage) part;
				stream = mm.getRawInputStream();

			} else {
				stream = null;
			}

			if ( stream == null ) {
				/* 
				 * Neither a MimeBodyPart nor a MimeMessage
				 */
				return "";
			}

			try {
				return readStream(stream);

			} catch (IOException e1) {
				return "";

			} finally {
				try {
					stream.close();
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}
	}

	/**
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	private String readStream(final InputStream stream) throws IOException {

		InputStreamReader reader = null;

		try {

			int count = 0;
			final char[] c = new char[BUFSIZE];
			reader= new InputStreamReader(stream, GlobalConstants.UTF_8);

			if ( (count = reader.read( c )) > 0 ) {
				final StringBuilder sb = new StringBuilder( STRBLD_SIZE );
				do {
					sb.append( c, 0, count );
				}
				while ((count = reader.read( c )) > 0);
				return sb.toString();
			}
			return "";

		} finally {

			if ( null != reader ) {
				try {
					reader.close();
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			}
		
		}
	}

	/**
	 * @param m
	 * @return
	 * @throws Exception
	 */
	private String mail2HTML(Message m) throws Exception {

		/*
		 * Date & subject
		 */
		String date    = m.getSentDate().toString();	
		String subject = m.getSubject();

		/*
		 * Body
		 */
    	String body = getBody(m);

		/*
		 * From
		 */
	   	String from = "";

    	Address[]  addresses = m.getFrom();
    	for (Address address:addresses) {
    		
    		InternetAddress internetAddress = (InternetAddress)address;
    		from = internetAddress.getPersonal();
    		
    	}
    	 
    	return HtmlRenderer.mail2HTML(from, date, subject, body);
    	
	}
	
	/**
	 * @param mp
	 * @param attach
	 * @return
	 * @throws Exception
	 */
	private FileUtil handleMultipart(Multipart mp, boolean attach) throws Exception {

		FileUtil file = null;
		for (int i=0; i < mp.getCount(); i++) {
			file = handlePart(mp.getBodyPart(i), attach);           
		} 
		
		return file;
		
	}
	
	/**
	 * @param part
	 * @param attach
	 * @return
	 * @throws Exception
	 */
	private FileUtil handlePart(Part part, boolean attach) throws Exception {
		
		FileUtil file = null;
		
		String disposition = part.getDisposition();		
		if (disposition == null) {
			// no nothing
		
		} else if (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE)) {
			
			file = new FileUtil();
			
			String fileName = part.getFileName();
			String contentType = part.getContentType();
			
			/* 
			 * The contentType parameter contains mimetype and filename in one
			 */
			String mimeType = contentType.split(";")[0].trim();
			
			file.setFilename(fileName);
			file.setMimetype(mimeType);
			
			InputStream inputStream = (attach == true) ? part.getInputStream() : null;
			if (inputStream != null) file.setInputStream(inputStream, mimeType);

		}
		
		return file;
		
	}
	
}
