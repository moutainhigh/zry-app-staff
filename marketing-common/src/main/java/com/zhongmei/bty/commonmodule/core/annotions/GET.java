package com.zhongmei.bty.commonmodule.core.annotions;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GET {

    String key();

    boolean defBoolean() default false;

    int defInt() default 0;

    float defFloat() default 0.0f;

    long defLong() default 0l;

    String defString() default "";
}