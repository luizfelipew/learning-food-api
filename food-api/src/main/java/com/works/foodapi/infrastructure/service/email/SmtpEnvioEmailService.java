package com.works.foodapi.infrastructure.service.email;

import com.works.foodapi.core.email.EmailProperties;
import com.works.foodapi.domain.service.EnvioEmailService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class SmtpEnvioEmailService implements EnvioEmailService {

    private final JavaMailSender emailSender;
    private final EmailProperties emailProperties;
    private final Configuration freemarkerConfig;

    @Override
    public void enviar(Mensagem mensagem) {
        try {
            var corpo = processarTemplate(mensagem);

            MimeMessage mimeMessage = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getRemetente());
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
            helper.setSubject(mensagem.getAssunto());
            helper.setText(corpo, true);

            emailSender.send(mimeMessage);
        } catch (Exception ex) {
            throw new EmailException("Não foi possivel enviar e-mail", ex);
        }
    }

    private String processarTemplate(final Mensagem mensagem) {
        try {
            var template = freemarkerConfig.getTemplate(mensagem.getCorpo());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());
        } catch (Exception ex) {
            throw new EmailException("Não foi possivel montar o template do e-mail", ex);
        }
    }
}
