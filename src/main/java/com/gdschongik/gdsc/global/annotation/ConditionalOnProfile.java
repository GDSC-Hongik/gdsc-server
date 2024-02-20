package com.gdschongik.gdsc.global.annotation;

import com.gdschongik.gdsc.global.common.constant.EnvironmentConstant;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional({OnProfileCondition.class})
public @interface ConditionalOnProfile {
    EnvironmentConstant[] value() default {EnvironmentConstant.LOCAL};
}
