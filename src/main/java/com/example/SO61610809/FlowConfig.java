package com.example.SO61610809;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.dsl.Tcp;

import static org.springframework.integration.handler.LoggingHandler.Level.INFO;

@Configuration
public class FlowConfig {

    @Bean
    public IntegrationFlow inboundFlow() {
        return IntegrationFlows.from(
                Tcp.inboundAdapter(
                        Tcp.netServer(12345)
                                .deserializer(new Int32ArrayLengthHeaderSerializer())))
                .transform(Transformers.objectToString())
                .log(INFO)
                .get();
    }
}
