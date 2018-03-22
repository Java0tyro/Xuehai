package xuehai.util;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class MailUtil {
    public static void sendMail(String toEmail, String subject,
                                String htmlContent) {

        try {
            MailConfigure.set();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

        // 发送邮箱的邮件服务器
        senderImpl.setHost(MailConfigure.EMAIL_HOST);
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        // 为防止乱码，添加编码集设置
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
                "UTF-8");
        try {
            // 接收方邮箱
            messageHelper.setTo(toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("收件人邮箱地址出错！");
        }
        try {
            // 发送方邮箱
            messageHelper.setFrom(MailConfigure.EMAIL_FORM);
        } catch (MessagingException e) {
            throw new RuntimeException("发件人邮箱地址出错！");
        }
        try {
            messageHelper.setSubject(subject);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件主题出错！");
        }
        try {
            // true 表示启动HTML格式的邮件
            messageHelper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException("邮件内容出错！");
        }

        Properties prop = new Properties();
        // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.port", "465");

        // 添加验证
        MyAuthenticator auth = new MyAuthenticator(MailConfigure.EMAIL_USERNAME,
                MailConfigure.EMAIL_PWD);

        Session session = Session.getDefaultInstance(prop, auth);
        senderImpl.setSession(session);

        // 发送邮件
        senderImpl.send(mailMessage);
    }

    public static void test() throws Exception{
        MailConfigure.set();
    }

}


class MailConfigure {
    //发邮件
    public static String EMAIL_FORM = null;
    public static String EMAIL_HOST = null;
    public static String EMAIL_USERNAME = null;
    public static String EMAIL_PWD = null;

    public static void set() throws Exception{
        Properties properties = new Properties();

        properties.load(MailUtil.class.getClassLoader().getResourceAsStream("mailConfigure.properties"));
        EMAIL_FORM = properties.getProperty("email_from");
        EMAIL_HOST = properties.getProperty("email_host");
        EMAIL_USERNAME = properties.getProperty("email_username");
        EMAIL_PWD = properties.getProperty("email_pwd");
        System.out.println(EMAIL_FORM + EMAIL_HOST +EMAIL_USERNAME + EMAIL_PWD);
        System.out.println(properties);
    }
}


class MyAuthenticator extends Authenticator {

    private String username;
    private String password;

    public MyAuthenticator(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}