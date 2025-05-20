package com.works.foodapi.infrastructure.service.email;

import com.works.foodapi.core.email.EmailProperties;
import com.works.foodapi.domain.service.EnvioEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class SmtpEnvioEmailService implements EnvioEmailService {

    private final JavaMailSender emailSender;
    private final EmailProperties emailProperties;

    @Override
    public void enviar(Mensagem mensagem) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getRemetente());
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
            helper.setSubject(mensagem.getAssunto());
            helper.setText(mensagem.getCorpo(), true);

        } catch (Exception ex) {
            throw new EmailException("Não foi possivel enviar e-mail", ex);
        }

//        emailSender.send();
    }
}
