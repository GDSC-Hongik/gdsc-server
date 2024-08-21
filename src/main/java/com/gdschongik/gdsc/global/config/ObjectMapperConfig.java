package com.gdschongik.gdsc.global.config;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.DATE;
import static com.gdschongik.gdsc.global.common.constant.RegexConstant.DATETIME;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // LocalDate
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        // LocalDateTime
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        // LocalTime
        module.addSerializer(LocalTime.class, new LocalTimeSerializer());
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());

        mapper.registerModule(module);
        return mapper;
    }

    public class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            generator.writeString(value.format(DateTimeFormatter.ofPattern(DATE)));
        }
    }

    public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            return LocalDate.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern(DATE));
        }
    }

    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            generator.writeString(value.format(DateTimeFormatter.ofPattern(DATETIME)));
        }
    }

    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern(DATETIME));
        }
    }

    public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

        @Override
        public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            generator.writeStartObject();

            generator.writeNumberField("hour", value.getHour());
            generator.writeNumberField("minute", value.getMinute());
            generator.writeNumberField("second", value.getSecond());
            generator.writeNumberField("nano", value.getNano());

            generator.writeEndObject();
        }
    }

    public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            int hour = node.get("hour").asInt();
            int minute = node.get("minute").asInt();
            int second = node.get("second").asInt();
            int nano = node.get("nano").asInt();

            return LocalTime.of(hour, minute, second, nano);
        }
    }
}
