package com.gdschongik.gdsc.domain.integration;

import com.gdschongik.gdsc.global.property.email.EmailProperty;
import com.gdschongik.gdsc.global.property.email.Gmail;
import com.gdschongik.gdsc.global.property.email.JavaMailProperty;
import com.gdschongik.gdsc.global.property.email.SocketFactory;
import com.gdschongik.gdsc.global.util.Pair;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class JavaMailSenderConfig {

    private final EmailProperty emailProperty;

    @Bean
    public JavaMailSender javaMailSenderBean() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailProperty.host());
        javaMailSender.setPort(emailProperty.port());
        javaMailSender.setDefaultEncoding(emailProperty.encoding());

        setGmailProperty(javaMailSender);
        setJavaMailProperties(javaMailSender);
        return javaMailSender;
    }

    private void setGmailProperty(JavaMailSenderImpl javaMailSender) {
        Gmail gmail = emailProperty.gmail();
        javaMailSender.setUsername(gmail.loginEmail());
        javaMailSender.setPassword(gmail.password());
    }

    private void setJavaMailProperties(JavaMailSenderImpl javaMailSender) {
        JavaMailProperty javaMailProperty = emailProperty.javaMailProperty();
        Properties javaMailProperties = getJavaMailProperties(javaMailProperty);
        javaMailSender.setJavaMailProperties(javaMailProperties);
    }

    private Properties getJavaMailProperties(JavaMailProperty javaMailProperty) {
        Properties properties = new Properties();
        SocketFactory socketFactory = javaMailProperty.socketFactory();
        putProperty(properties, javaMailProperty.smtpAuth());
        putProperty(properties, socketFactory.port());
        putProperty(properties, socketFactory.fallback());
        putProperty(properties, socketFactory.classInfo());
        return properties;
    }

    private void putProperty(Properties properties, Pair property) {
        properties.put(property.key(), property.value());
    }
}
