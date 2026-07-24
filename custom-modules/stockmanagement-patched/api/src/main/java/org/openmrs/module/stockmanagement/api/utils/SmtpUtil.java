package org.openmrs.module.stockmanagement.api.utils;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Properties;

public class SmtpUtil {
	
	public static Session getSession() {
		Properties smtpProperties = System.getProperties();
		setProperties(smtpProperties);
		Authenticator authenticator = null;
		String password = smtpProperties.getProperty("mail.smtp.user.password");
		String username = smtpProperties.getProperty("mail.smtp.user");
		if (org.apache.commons.lang.StringUtils.isNotBlank(username)
		        && org.apache.commons.lang.StringUtils.isNotBlank(password)) {
			authenticator = new Authenticator() {
				
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};
		}
		Session session = Session.getDefaultInstance(smtpProperties, authenticator);
		return session;
	}
	
	public static boolean hasSmptHostSetup() {
		String smptHostProperty = "mail.smtp.host";
		Properties smtpProperties = System.getProperties();
		if (smtpProperties.get(smptHostProperty) == null) {
			String propertyValue = Context.getAdministrationService().getGlobalProperty(smptHostProperty);
			if (org.apache.commons.lang.StringUtils.isBlank(propertyValue)) {
				propertyValue = Context.getAdministrationService().getGlobalProperty("mail.smtp_host");
				return org.apache.commons.lang.StringUtils.isNotBlank(propertyValue);
			}
		}
		return true;
	}
	
	private static void setProperties(Properties properties) {
		AdministrationService administrationService = Context.getAdministrationService();
		setProperty(properties, administrationService, "mail.debug", null); // String	Default user name for SMTP.
		setProperty(properties, administrationService, "mail.smtp.user", "mail.user"); // String	Default user name for SMTP.
		setProperty(properties, administrationService, "mail.smtp.user.password", "mail.password"); // String	Default user name for SMTP.
		setProperty(properties, administrationService, "mail.smtp.host", "mail.smtp_host"); // String	The SMTP server to connect to.
		setProperty(properties, administrationService, "mail.smtp.port", "mail.smtp_port"); // int	The SMTP server port to connect to, if the connect() method doesn't explicitly specify one. Defaults to 25.
		setProperty(properties, administrationService, "100000", "mail.smtp.connectiontimeout", null); // int	Socket connection timeout value in milliseconds. This timeout is implemented by java.net.Socket. Default is infinite timeout.
		setProperty(properties, administrationService, "100000", "mail.smtp.timeout", null); // int	Socket read timeout value in milliseconds. This timeout is implemented by java.net.Socket. Default is infinite timeout.
		setProperty(properties, administrationService, "100000", "mail.smtp.writetimeout", null); // int	Socket write timeout value in milliseconds. This timeout is implemented by using a java.util.concurrent.ScheduledExecutorService per connection that schedules a thread to close the socket if the timeout expires. Thus, the overhead of using this timeout is one thread per connection. Default is infinite timeout.
		setProperty(properties, administrationService, "mail.smtp.from", "mail.from"); // String	Email address to use for SMTP MAIL command. This sets the envelope return address. Defaults to msg.getFrom() or InternetAddress.getLocalAddress(). NOTE: mail.smtp.user was previously used for this.
		setProperty(properties, administrationService, "mail.smtp.localhost", null); // String	Local host name used in the SMTP HELO or EHLO command. Defaults to InetAddress.getLocalHost().getHostName(). Should not normally need to be set if your JDK and your name service are configured properly.
		setProperty(properties, administrationService, "mail.smtp.localaddress", null); // String	Local address (host name) to bind to when creating the SMTP socket. Defaults to the address picked by the Socket class. Should not normally need to be set, but useful with multi-homed hosts where it's important to pick a particular local address to bind to.
		setProperty(properties, administrationService, "mail.smtp.localport", null); // int	Local port number to bind to when creating the SMTP socket. Defaults to the port number picked by the Socket class.
		setProperty(properties, administrationService, "mail.smtp.ehlo", null); // boolean	If false, do not attempt to sign on with the EHLO command. Defaults to true. Normally failure of the EHLO command will fallback to the HELO command; this property exists only for servers that don't fail EHLO properly or don't implement EHLO properly.
		setProperty(properties, administrationService, "mail.smtp.auth", "mail.smtp_auth"); // boolean	If true, attempt to authenticate the user using the AUTH command. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.auth.mechanisms", null); // String	If set, lists the authentication mechanisms to consider, and the order in which to consider them. Only mechanisms supported by the server and supported by the current implementation will be used. The default is "LOGIN PLAIN DIGEST-MD5 NTLM", which includes all the authentication mechanisms supported by the current implementation except XOAUTH2.
		setProperty(properties, administrationService, "mail.smtp.auth.login.disable", null); // boolean	If true, prevents use of the AUTH LOGIN command. Default is false.
		setProperty(properties, administrationService, "mail.smtp.auth.plain.disable", null); // boolean	If true, prevents use of the AUTH PLAIN command. Default is false.
		setProperty(properties, administrationService, "mail.smtp.auth.digest-md5.disable", null); // boolean	If true, prevents use of the AUTH DIGEST-MD5 command. Default is false.
		setProperty(properties, administrationService, "mail.smtp.auth.ntlm.disable", null); // boolean	If true, prevents use of the AUTH NTLM command. Default is false.
		setProperty(properties, administrationService, "mail.smtp.auth.ntlm.domain", null); // String	The NTLM authentication domain.
		setProperty(properties, administrationService, "mail.smtp.auth.ntlm.flags", null); // int	NTLM protocol-specific flags. See http://curl.haxx.se/rfc/ntlm.html#theNtlmFlags for details.
		setProperty(properties, administrationService, "mail.smtp.auth.xoauth2.disable", null); // boolean	If true, prevents use of the AUTHENTICATE XOAUTH2 command. Because the OAuth 2.0 protocol requires a special access token instead of a password, this mechanism is disabled by default. Enable it by explicitly setting this property to "false" or by setting the "mail.smtp.auth.mechanisms" property to "XOAUTH2".
		setProperty(properties, administrationService, "mail.smtp.submitter", null); // String	The submitter to use in the AUTH tag in the MAIL FROM command. Typically used by a mail relay to pass along information about the original submitter of the message. See also the setSubmitter method of SMTPMessage. Mail clients typically do not use this.
		setProperty(properties, administrationService, "mail.smtp.dsn.notify", null); // String	The NOTIFY option to the RCPT command. Either NEVER, or some combination of SUCCESS, FAILURE, and DELAY (separated by commas).
		setProperty(properties, administrationService, "mail.smtp.dsn.ret", null); // String	The RET option to the MAIL command. Either FULL or HDRS.
		setProperty(properties, administrationService, "mail.smtp.allow8bitmime", null); // boolean	If set to true, and the server supports the 8BITMIME extension, text parts of messages that use the "quoted-printable" or "base64" encodings are converted to use "8bit" encoding if they follow the RFC2045 rules for 8bit text.
		setProperty(properties, administrationService, "mail.smtp.sendpartial", null); // boolean	If set to true, and a message has some valid and some invalid addresses, send the message anyway, reporting the partial failure with a SendFailedException. If set to false (the default), the message is not sent to any of the recipients if there is an invalid recipient address.
		setProperty(properties, administrationService, "mail.smtp.sasl.enable", null); // boolean	If set to true, attempt to use the javax.security.sasl package to choose an authentication mechanism for login. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.sasl.mechanisms", null); // String	A space or comma separated list of SASL mechanism names to try to use.
		setProperty(properties, administrationService, "mail.smtp.sasl.authorizationid", null); // String	The authorization ID to use in the SASL authentication. If not set, the authentication ID (user name) is used.
		setProperty(properties, administrationService, "mail.smtp.sasl.realm", null); // String	The realm to use with DIGEST-MD5 authentication.
		setProperty(properties, administrationService, "mail.smtp.sasl.usecanonicalhostname", null); // boolean	If set to true, the canonical host name returned by InetAddress.getCanonicalHostName is passed to the SASL mechanism, instead of the host name used to connect. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.quitwait", null); // boolean	If set to false, the QUIT command is sent and the connection is immediately closed. If set to true (the default), causes the transport to wait for the response to the QUIT command.
		setProperty(properties, administrationService, "mail.smtp.reportsuccess", null); // boolean	If set to true, causes the transport to include an SMTPAddressSucceededException for each address that is successful. Note also that this will cause a SendFailedException to be thrown from the sendMessage method of SMTPTransport even if all addresses were correct and the message was sent successfully.
		setProperty(properties, administrationService, "mail.smtp.socketFactory", null); // SocketFactory	If set to a class that implements the javax.net.SocketFactory interface, this class will be used to create SMTP sockets. Note that this is an instance of a class, not a name, and must be set using the put method, not the setProperty method.
		setProperty(properties, administrationService, "mail.smtp.socketFactory.class", null); // String	If set, specifies the name of a class that implements the javax.net.SocketFactory interface. This class will be used to create SMTP sockets.
		setProperty(properties, administrationService, "mail.smtp.socketFactory.fallback", null); // boolean	If set to true, failure to create a socket using the specified socket factory class will cause the socket to be created using the java.net.Socket class. Defaults to true.
		setProperty(properties, administrationService, "mail.smtp.socketFactory.port", null); // int	Specifies the port to connect to when using the specified socket factory. If not set, the default port will be used.
		setProperty(properties, administrationService, "mail.smtp.ssl.enable", null); // boolean	If set to true, use SSL to connect and use the SSL port by default. Defaults to false for the "smtp" protocol and true for the "smtps" protocol.
		setProperty(properties, administrationService, "mail.smtp.ssl.checkserveridentity", null); // boolean	If set to true, check the server identity as specified by RFC 2595. These additional checks based on the content of the server's certificate are intended to prevent man-in-the-middle attacks. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.ssl.trust", null); // String	If set, and a socket factory hasn't been specified, enables use of a MailSSLSocketFactory. If set to "*", all hosts are trusted. If set to a whitespace separated list of hosts, those hosts are trusted. Otherwise, trust depends on the certificate the server presents.
		setProperty(properties, administrationService, "mail.smtp.ssl.socketFactory", null); // SSLSocketFactory	If set to a class that extends the javax.net.ssl.SSLSocketFactory class, this class will be used to create SMTP SSL sockets. Note that this is an instance of a class, not a name, and must be set using the put method, not the setProperty method.
		setProperty(properties, administrationService, "mail.smtp.ssl.socketFactory.class", null); // String	If set, specifies the name of a class that extends the javax.net.ssl.SSLSocketFactory class. This class will be used to create SMTP SSL sockets.
		setProperty(properties, administrationService, "mail.smtp.ssl.socketFactory.port", null); // int	Specifies the port to connect to when using the specified socket factory. If not set, the default port will be used.
		setProperty(properties, administrationService, "mail.smtp.ssl.protocols", null); // string	Specifies the SSL protocols that will be enabled for SSL connections. The property value is a whitespace separated list of tokens acceptable to the javax.net.ssl.SSLSocket.setEnabledProtocols method.
		setProperty(properties, administrationService, "mail.smtp.ssl.ciphersuites", null); // string	Specifies the SSL cipher suites that will be enabled for SSL connections. The property value is a whitespace separated list of tokens acceptable to the javax.net.ssl.SSLSocket.setEnabledCipherSuites method.
		setProperty(properties, administrationService, "mail.smtp.starttls.enable", null); // boolean	If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection before issuing any login commands. If the server does not support STARTTLS, the connection continues without the use of TLS; see the mail.smtp.starttls.required property to fail if STARTTLS isn't supported. Note that an appropriate trust store must configured so that the client will trust the server's certificate. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.starttls.required", null); // boolean	If true, requires the use of the STARTTLS command. If the server doesn't support the STARTTLS command, or the command fails, the connect method will fail. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.proxy.host", null); // string	Specifies the host name of an HTTP web proxy server that will be used for connections to the mail server.
		setProperty(properties, administrationService, "mail.smtp.proxy.port", null); // string	Specifies the port number for the HTTP web proxy server. Defaults to port 80.
		setProperty(properties, administrationService, "mail.smtp.proxy.user", null); // string	Specifies the user name to use to authenticate with the HTTP web proxy server. By default, no authentication is done.
		setProperty(properties, administrationService, "mail.smtp.proxy.password", null); // string	Specifies the password to use to authenticate with the HTTP web proxy server. By default, no authentication is done.
		setProperty(properties, administrationService, "mail.smtp.socks.host", null); // string	Specifies the host name of a SOCKS5 proxy server that will be used for connections to the mail server.
		setProperty(properties, administrationService, "mail.smtp.socks.port", null); // string	Specifies the port number for the SOCKS5 proxy server. This should only need to be used if the proxy server is not using the standard port number of 1080.
		setProperty(properties, administrationService, "mail.smtp.mailextension", null); // String	Extension string to append to the MAIL command. The extension string can be used to specify standard SMTP service extensions as well as vendor-specific extensions. Typically the application should use the SMTPTransport method supportsExtension to verify that the server supports the desired service extension. See RFC 1869 and other RFCs that define specific extensions.
		setProperty(properties, administrationService, "mail.smtp.userset", null); // boolean	If set to true, use the RSET command instead of the NOOP command in the isConnected method. In some cases sendmail will respond slowly after many NOOP commands; use of RSET avoids this sendmail issue. Defaults to false.
		setProperty(properties, administrationService, "mail.smtp.noop.strict", null); // boolean	If set to true (the default), insist on a 250 response code from the NOOP command to indicate success. The NOOP command is used by the isConnected method to determine if the connection is still alive. Some older servers return the wrong response code on success, some servers don't implement the NOOP command at all and so always return a failure code. Set this property to false to handle servers that are broken in this way. Normally, when a server times out a connection, it will send a 421 response code, which the client will see as the response to the next command it issues. Some servers send the wrong failure response code when timing out a connection. Do not set this property to false when dealing with servers that are broken in this way.
		
	}
	
	private static void setProperty(Properties property, AdministrationService administrationService, String propertyName,
	        String altSourcePropertyName) {
		setProperty(property, administrationService, null, propertyName, altSourcePropertyName);
	}
	
	private static void setProperty(Properties property, AdministrationService administrationService, String defaultValue,
	        String propertyName, String altSourcePropertyName) {
		if (property.get(propertyName) == null) {
			String propertyValue = administrationService.getGlobalProperty(propertyName);
			if (!org.apache.commons.lang.StringUtils.isBlank(propertyValue)) {
				property.setProperty(propertyName, propertyValue);
			} else {
				if (altSourcePropertyName != null) {
					propertyValue = administrationService.getGlobalProperty(altSourcePropertyName);
				}
				if (!org.apache.commons.lang.StringUtils.isBlank(propertyValue)) {
					property.setProperty(propertyName, propertyValue);
				} else if (defaultValue != null) {
					property.setProperty(propertyName, defaultValue);
				}
			}
		}
	}
	
	public static void sendEmail(String subject, String body, String emailAddress) throws MessagingException, IOException {
		Session session = SmtpUtil.getSession();
		sendEmail(session, subject, body, emailAddress);
	}
	
	public static void sendEmail(Session session, String subject, String body, String emailAddress)
	        throws MessagingException, IOException {
		sendEmail(subject, body, session, emailAddress);
	}
	
	public static void sendEmail(String subject, String body, Session session, String... emailAddresses)
	        throws MessagingException, IOException {
		MimeMessage mimeMessage = new MimeMessage(session);
		Address[] addresses = new Address[emailAddresses.length];
		for (int i = 0; i < emailAddresses.length; i++) {
			addresses[i] = InternetAddress.parse(emailAddresses[i], false)[0];
		}
		mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
		mimeMessage.setSubject(subject);
		mimeMessage.setHeader("Content-Transfer-Encoding", "base64");
		mimeMessage.setDataHandler(new DataHandler(new ByteArrayDataSource(body, "text/html")));
		Transport.send(mimeMessage);
	}
	
}
