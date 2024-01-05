package com.meteormin.friday.common.fake.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Not allowed fake data to be injected from FakeInjector.
 * @see com.meteormin.friday.common.fake.FakeInjector
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoFake {
}
