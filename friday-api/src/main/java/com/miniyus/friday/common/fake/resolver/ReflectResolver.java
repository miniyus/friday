package com.miniyus.friday.common.fake.resolver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.miniyus.friday.common.fake.FakeInjector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.datafaker.Faker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReflectResolver {
    private final FakeInjector injector;
    private final Faker faker;
    private final Options options;

    public ReflectResolver(FakeInjector injector) {
        this(injector, Options.defaultOptions());
    }

    public ReflectResolver(FakeInjector injector, Options options) {
        this.injector = injector;
        this.faker = injector.getFaker();
        this.options = Options.fill(options);
    }

    /**
     * Resolves the property name for a given field.
     *
     * @param field the field for which to resolve the property name
     * @return the resolved property name
     * @throws InstantiationException    if an error occurs while instantiating a class
     * @throws IllegalAccessException    if an error occurs while accessing a class
     * @throws NoSuchMethodException     if the requested method does not exist
     * @throws InvocationTargetException if an error occurs while invoking a method
     */
    @SuppressWarnings("unchecked")
    public String resolvePropertyName(Field field)
        throws InstantiationException, IllegalAccessException, NoSuchMethodException,
        InvocationTargetException {
        if (field.isAnnotationPresent(JsonProperty.class)) {
            var annotation = field.getAnnotation(JsonProperty.class);
            var propertyName = annotation.value();
            if (propertyName != null) {
                return propertyName;
            }
        } else if (field.getDeclaringClass().isAnnotationPresent(JsonNaming.class)) {
            Constructor<? extends PropertyNamingStrategies.NamingBase> propertyNamingConstructor =
                (Constructor<? extends PropertyNamingStrategies.NamingBase>) field
                    .getDeclaringClass()
                    .getAnnotation(JsonNaming.class)
                    .value()
                    .getDeclaredConstructor();
            var propertyNamingStrategy = propertyNamingConstructor.newInstance();
            return propertyNamingStrategy.translate(field.getName());
        }

        return field.getName();
    }

    /**
     * Resolves the property type of given field for the provided instance.
     *
     * @param field    the field to resolve the property type for
     * @param instance the instance for which to resolve the property type
     * @throws InvocationTargetException if an exception occurs while invoking a method
     * @throws IllegalAccessException    if access to the field is denied
     * @throws InstantiationException    if an instance of the class cannot be created
     * @throws NoSuchMethodException     if a requested method does not exist
     */
    @SuppressWarnings({"unchecked", "java:S3011", "java:S3776"})
    public void resolvePropertyType(Field field, Object instance)
        throws InvocationTargetException, IllegalAccessException, InstantiationException,
        NoSuchMethodException {

        int minAge = options.dateTime.max.toLocalDateTime().getYear() - LocalDateTime.now().getYear();
        int maxAge = options.dateTime.min.toLocalDateTime().getYear() - LocalDateTime.now().getYear();

        field.setAccessible(true);
        if (field.getType() == String.class) {
            setField(field, instance, faker.lorem().word());
        } else if (field.getType() == char.class || field.getType() == Character.class) {
            setField(field, instance, (char) faker.number().randomDigit());
        } else if (field.getType() == byte.class || field.getType() == Byte.class) {
            setField(field, instance, (byte) faker.number().randomDigit());
        } else if (field.getType() == byte[].class || field.getType() == Byte[].class) {
            setField(field, instance, faker.name().name().getBytes());
        } else if (field.getType() == short.class || field.getType() == Short.class) {
            var s = getField(field, instance);
            if (s instanceof Short && !s.equals(0)) {
                // Cause built-in type short not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, (short) faker.number().randomDigit());
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            var i = getField(field, instance);
            if (i instanceof Integer && !i.equals(0)) {
                // Cause built-in type int not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, (int) faker.number().randomNumber());
        } else if (field.getType() == float.class || field.getType() == Float.class) {
            var f = getField(field, instance);
            if (f instanceof Float && !f.equals(0.0)) {
                // Cause built-in type float not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, (float) faker.number().randomDouble(
                options.number.maxNumberOfDigits,
                options.number.min,
                options.number.max));
        } else if (field.getType() == double.class || field.getType() == Double.class) {
            var d = getField(field, instance);
            if (d instanceof Double && !d.equals(0.0)) {
                // Cause built-in double not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, faker.number().randomDouble(
                options.number.maxNumberOfDigits,
                options.number.min,
                options.number.max));
        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            var b = getField(field, instance);
            if (b instanceof Boolean && !b.equals(false)) {
                // Cause built-in boolean not allowed null.
                // So, it considers the unassigned status as false.
                return;
            }
            setField(field, instance, faker.bool().bool());
        } else if (field.getType() == Date.class) {
            var dateInstant = faker.date().birthday(minAge, maxAge).toInstant();
            setField(field, instance, Date.from(dateInstant));
        } else if (field.getType() == LocalDateTime.class) {
            var localDateTime = faker.date().birthday(minAge, maxAge).toLocalDateTime();
            setField(field, instance, localDateTime);
        } else if (field.getType() == LocalDate.class) {
            var localDateTime = faker.date().birthday(minAge, maxAge).toLocalDateTime();
            setField(field, instance, localDateTime.toLocalDate());
        } else if (field.getType() == LocalTime.class) {
            var localDateTime = faker.date().birthday(minAge, maxAge).toLocalDateTime();
            setField(field, instance, localDateTime.toLocalTime());
        } else if (field.getType() == Timestamp.class) {
            setField(field, instance, faker.date().birthday(minAge, maxAge));
        } else if (field.getType() == UUID.class) {
            setField(field, instance, UUID.randomUUID());
        } else if (field.getType() == Long.class || field.getType() == long.class) {
            var l = getField(field, instance);
            if (l instanceof Long && !l.equals(0L)) {
                // Cause built-in long not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, faker.number().randomNumber());
        } else if (field.getType().isEnum()) {
            setField(field, instance,
                injector.randomEnum((Class<? extends Enum<?>>) field.getType()));
        } else if (field.getType().isArray()) {
            // array type has component type
            // use component type to generate
            var componentType = field.getType().getComponentType();
            var componentInstance =
                injector.generate(componentType, options.array.defaultSize)
                    .toArray(new Object[options.array.defaultSize]);
            setField(field, instance, componentInstance);
        } else if (field.getType() == List.class) {
            // list class has generic type
            // use generic type to generate
            var genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType parameterizedType) {
                var listType = parameterizedType.getActualTypeArguments()[0];
                Class<?> componentType = (Class<?>) listType;
                var componentInstance = injector.generate(componentType, options.array.defaultSize);
                setField(field, instance, componentInstance);
            }
        } else {
            // else type considers as object
            // recursively call generate
            setField(field, instance, injector.generate(field.getType()));
        }
    }

    /**
     * Retrieves the value of a field from an object.
     *
     * @param field    the field to retrieve the value from
     * @param instance the object to retrieve the value from
     * @return the value of the field
     * @throws IllegalAccessException if the field is inaccessible
     */
    @SuppressWarnings("unchecked")
    private Object getField(Field field, Object instance)
        throws IllegalAccessException, InvocationTargetException, InstantiationException,
        NoSuchMethodException {
        if (instance instanceof Map) {
            var fieldName = resolvePropertyName(field);
            return ((Map<String, Object>) instance).get(fieldName);
        }
        return field.get(instance);
    }

    /**
     * Sets the value of a field in an instance using reflection.
     *
     * @param field    reflect field
     * @param instance the instance on which to set the field
     * @param value    the value to set
     */
    @SuppressWarnings({"unchecked", "java:S3011"})
    private void setField(Field field, Object instance, Object value)
        throws IllegalAccessException, InvocationTargetException, InstantiationException,
        NoSuchMethodException {
        if (getField(field, instance) != null) {
            return;
        }

        if (instance instanceof Map) {
            var fieldName = resolvePropertyName(field);
            ((Map<String, Object>) instance).put(fieldName, value);
        } else {
            field.set(instance, value);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Options {
        private Array array;
        private DateTime dateTime;
        private Number number;


        public static class Array {
            int defaultSize;
        }


        public static class DateTime {
            Timestamp min;
            Timestamp max;
        }


        public static class Number {
            int maxNumberOfDigits;
            int min;
            int max;
        }


        public static class OptionsBuilder {
            private final Array array;
            private final DateTime datetime;
            private final Number number;

            public OptionsBuilder() {
                this.array = new Array();
                this.datetime = new DateTime();
                this.number = new Number();
            }

            public OptionsBuilder array(int defaultSize) {
                this.array.defaultSize = defaultSize;
                return this;
            }

            /**
             * @param min pattern of min yyyy-MM-dd HH:mm:ss
             * @param max pattern of max yyyy-MM-dd HH:mm:ss
             * @return this
             */
            public OptionsBuilder datetime(String min, String max) {
                this.datetime.min = Timestamp.valueOf(min);
                this.datetime.max = Timestamp.valueOf(max);
                return this;
            }

            public OptionsBuilder number(int maxNumberOfDigits, int min, int max) {
                this.number.maxNumberOfDigits = maxNumberOfDigits;
                this.number.min = min;
                this.number.max = max;
                return this;
            }

            public Options build() {
                return new Options(
                    array,
                    datetime,
                    number);
            }
        }

        public static OptionsBuilder builder() {
            return new OptionsBuilder();
        }

        public static Options defaultOptions() {
            var datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return new OptionsBuilder()
                .array(3)
                .datetime(LocalDateTime.MIN.format(datePattern),
                    LocalDateTime.MAX.format(datePattern))
                .number(2,
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE)
                .build();
        }

        public static Options fill(Options opts) {

            if (opts == null) {
                return defaultOptions();
            }

            var builder = new OptionsBuilder();
            if (opts.dateTime.min == null) {
                builder.datetime.min = Timestamp.valueOf(LocalDateTime.MIN);
            }

            if (opts.dateTime.max == null) {
                builder.datetime.min = Timestamp.valueOf(LocalDateTime.now());
            }

            return builder.build();
        }
    }
}
