package br.com.bitpay.service;


import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
public class EmailService {

    private static final String USER = "seuemail@gmail.com";
    private static final String PASSWORD = "SENHA_DE_APP";

    public static void enviarToken(String destino, String token) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USER, PASSWORD);
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USER));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            msg.setSubject("Recuperação de Senha");
            msg.setText("Seu token de recuperação é: " + token);

            Transport.send(msg);
            System.out.println("Email enviado para " + destino);

        } catch (Exception e) {
            System.out.println("Erro ao enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}