package hella;



import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Mail {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true); // Enable STARTTLS
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sumeetjha932@gmail.com", "jypvemfngnlvghxj");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sumeetjha932@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sumeetzhaa09@gmail.com"));
        message.setSubject("Email FROM Java");

        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("<h1>Email</h1>", "text/html");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
