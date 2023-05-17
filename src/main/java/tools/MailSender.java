package tools;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
    private static String Subject=null;
    private static String AddrList=null;
    private static String Content=null;
    private static Properties props = null;
    private static Session session=null;
    public static void setSubject(String sub){
        Subject=sub;
    }
    public static void setAddrList(String addrs){
        AddrList=addrs;
    }
    public static void setContent(String con){
        Content=con;
    }
    public MailSender(){
        final String username = "no-reply@okkk.cc";
        final String password = "Yonet233";
        props = new Properties();
        props.put("mail.smtp.auth", true);
        // TLS 需要这两行
        // props.put("mail.smtp.starttls.enable", true);
        // props.put("mail.smtp.port", "587");
        // SSL 需要这三行
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", "smtp.qiye.aliyun.com");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }
    public static boolean sendMail(){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@okkk.cc"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(AddrList));
            message.setSubject(Subject);
            message.setText(Content);

            Transport.send(message);
            System.out.println("Done");
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // return false;
    
    }
    public static void main(String[] args) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@okkk.cc"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("181004850@qq.com"));
            message.setSubject("Testing Subject1");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);
            System.out.println("Done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}