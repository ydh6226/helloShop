package com.ydh.helloshop.infra.amqp.sender;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Data
@Profile({"prod", "dev"})
@Component
@ConfigurationProperties("app.amqp")
public class AmqpProperty {

    private String queueName;
    private String exchangeName;
    private String routingKey;
}
