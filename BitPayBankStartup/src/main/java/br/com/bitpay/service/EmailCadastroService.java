package br.com.bitpay.service;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailCadastroService {

    public void enviarEmailCadastroEmAnalise(String email, String nome) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        "baroni.o@aluno.ifsp.edu.br",
                        "zxkn ltrt xbve yqdd"
                    );
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("no-reply@bitpaybank.com", "BitPay Bank"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject("Cadastro enviado para análise - BitPay Bank");

            String html = """
                <div style="font-family: Arial; background:#f5f7fc; padding:30px;">
                    <div style="max-width:520px;margin:auto;background:#fff;padding:30px;border-radius:10px;">
                        <h2 style="color:#0d6efd;">Cadastro recebido</h2>
                        <p>Olá <strong>%s</strong>,</p>
                        <p>Recebemos sua solicitação de abertura de conta.</p>
                        <p><strong>Status atual:</strong> Em análise interna.</p>
                        <p>Você receberá um novo e-mail assim que a análise for concluída.</p>
                        <p style="font-size:12px;color:#888;margin-top:30px;">
                            BitPay Bank • Conta Digital Segura
                        </p>
                    </div>
                </div>
            """.formatted(nome);

            msg.setContent(html, "text/html; charset=UTF-8");
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar e-mail de cadastro.");
        }
    }
}
