package br.com.fiap.globalSolution.Rabbit.Consumers;

import br.com.fiap.globalSolution.Rabbit.Dto.EmailDto;
import br.com.fiap.globalSolution.Rabbit.Entity.EmailEntity;
import br.com.fiap.globalSolution.Rabbit.Service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//Bean gerenciado pelo Spring (Component)
@Component
public class EmailConsumer {

    //Ponto de injeção
    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void listen(@Payload EmailDto emailDto) {
        EmailEntity emailModel = new EmailEntity();

        BeanUtils.copyProperties(emailDto, emailModel);

        emailService.sendEmail(emailModel);
        System.out.println("Email Status: " + emailModel.getStatusEmail().toString());
    }
}
