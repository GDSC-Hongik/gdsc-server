package com.gdschongik.gdsc.global.annotation;

import static java.util.Objects.*;

import com.gdschongik.gdsc.global.common.constant.EnvironmentConstant;
import java.util.Arrays;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnProfileCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        EnvironmentConstant[] targetProfiles = getTargetProfiles(metadata);

        return Arrays.stream(targetProfiles)
                .anyMatch(targetProfile -> Arrays.asList(activeProfiles).contains(targetProfile.getValue()));
    }

    private EnvironmentConstant[] getTargetProfiles(AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes =
                requireNonNull(metadata.getAnnotationAttributes(ConditionalOnProfile.class.getName()));
        return (EnvironmentConstant[]) attributes.get("value");
    }
}
