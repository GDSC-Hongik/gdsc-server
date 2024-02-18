package com.gdschongik.gdsc.global.config;

import com.gdschongik.gdsc.global.property.email.EmailProperty;
import com.gdschongik.gdsc.global.property.email.EmailProperty.Gmail;
import com.gdschongik.gdsc.global.property.email.EmailProperty.JavaMailProperty;
import com.gdschongik.gdsc.global.property.email.EmailProperty.SocketFactory;
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
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailProperty.getHost());
        javaMailSender.setPort(emailProperty.getPort());
        javaMailSender.setDefaultEncoding(emailProperty.getEncoding());

        setGmailProperty(javaMailSender);
        setJavaMailProperties(javaMailSender);
        return javaMailSender;
    }

    private void setGmailProperty(JavaMailSenderImpl javaMailSender) {
        Gmail gmail = emailProperty.getGmail();
        javaMailSender.setUsername(gmail.loginEmail());
        javaMailSender.setPassword(gmail.password());
    }

    private void setJavaMailProperties(JavaMailSenderImpl javaMailSender) {
        JavaMailProperty javaMailProperty = emailProperty.getJavaMailProperty();
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
