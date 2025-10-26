package com.example.diabfit;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailManager {


    private static final String EMAIL_REMETENTE = "equipediabfit@gmail.com";
    private static final String SENHA_REMETENTE = "bmah oswv grjo jqxa";


    public static void sendEmailInBackground(String emailDestinatario, String assunto, String corpoMensagem) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Executa a tarefa em segundo plano
        executor.execute(() -> {
            try {
                sendEmail(emailDestinatario, assunto, corpoMensagem);
                System.out.println("E-mail enviado com sucesso em background!");
            } catch (MessagingException e) {
                e.printStackTrace();
                System.err.println("Erro ao enviar e-mail em background: " + e.getMessage());

            }
        });
    }


    private static void sendEmail(String emailDestinatario, String assunto, String corpoMensagem) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMETENTE, SENHA_REMETENTE);
            }
        });


        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_REMETENTE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));
        message.setSubject(assunto);
        message.setText(corpoMensagem);

        // Envia a mensagem
        Transport.send(message);
    }
}

