package com.miniyus.friday.common.hexagon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Persistence Adapter. for persistence layer
 *
 * @author miniyus
 * @date 2023/09/09
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Transactional
public @interface PersistenceAdapter {

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    String[] label() default {};

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    String timeoutString() default "";

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    boolean readOnly() default false;

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    Class<? extends Throwable>[] rollbackFor() default {};

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    String[] rollbackForClassName() default {};

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    Class<? extends Throwable>[] noRollbackFor() default {};

    /**
     * {@inheritDoc}
     */
    @AliasFor(annotation = Transactional.class)
    String[] noRollbackForClassName() default {};
}
