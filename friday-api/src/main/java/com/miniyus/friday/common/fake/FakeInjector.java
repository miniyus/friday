package com.miniyus.friday.common.fake;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.NamingBase;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.javafaker.Faker;
import com.precisionbio.cuttysark.common.fake.annotation.NoFake;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * <p>FakeInjector.</p>
 * <p>Automatically inject fake data</p>
 *
 * @author seongminyoo
 * @see Faker
 * @since 2023/10/19
 */
@RequiredArgsConstructor
public class FakeInjector {

    /**
     * Default array size, If injected type is array(or collections) then set default
     */
    @Getter
    @Setter
    private static int defaultArraySize = 3;

    /**
     * Faker data generator.
     *
     * @see Faker
     */
    @Getter
    private final Faker faker;

    /**
     * object mapper.
     *
     * @see ObjectMapper
     */
    @Getter
    private final ObjectMapper objectMapper;

    /**
     * Capabilities available for multiple data injections
     */
    private Function<Object, Object> map;

    private final Random rand = new Random();

    /**
     * Generates a list of objects of type T.
     *
     * @param dataClass the class of the objects to generate
     * @param count     the number of objects to generate
     * @return a list of generated objects
     * @throws InvocationTargetException if an exception occurs while invoking a method
     * @throws NoSuchMethodException     if a specified method does not exist
     * @throws InstantiationException    if the class cannot be instantiated
     * @throws IllegalAccessException    if the class or its nullary constructor is not accessible
     */
    public <T> List<T> generate(Class<T> dataClass, int count)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            T instance = generate(dataClass);
            list.add(instance);
        }

        return list;
    }

    /**
     * Generate a list of objects of type T.
     *
     * @param dataClass the class of the objects to generate
     * @param count     the number of objects to generate
     * @param map       a function to map the generated objects
     * @return a list of generated objects
     * @throws InvocationTargetException if the underlying method throws an exception
     * @throws NoSuchMethodException     if the specified method cannot be found
     * @throws InstantiationException    if a new instance of the class cannot be instantiated
     * @throws IllegalAccessException    if the underlying method is inaccessible
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> generate(Class<T> dataClass, int count, UnaryOperator<T> map)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        this.map = (Function<Object, Object>) map;
        List<T> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            T instance = generate(dataClass);
            list.add(instance);
        }

        return list;
    }

    /**
     * Generates a list of objects based on the given count and mapping function.
     *
     * @param count the number of objects to generate
     * @param map   the mapping function to generate each object
     * @return the list of generated objects
     * @throws InvocationTargetException if the mapping function throws an exception
     * @throws NoSuchMethodException     if the mapping function throws an exception
     * @throws InstantiationException    if the mapping function throws an exception
     * @throws IllegalAccessException    if the mapping function throws an exception
     */
    public <T> List<T> generate(int count, Function<FakeInjector, T> map)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            T instance = generate(map.apply(this));
            list.add(instance);
        }

        return list;
    }

    /**
     * Generate a new instance of the given object by resolving its fields.
     *
     * @param instance the instance of the object to be generated
     * @param <T>      the type of the object
     * @return the generated instance of the object
     * @throws NoSuchMethodException     if the method being invoked does not exist
     * @throws InvocationTargetException if an error occurs while invoking the method
     * @throws InstantiationException    if an error occurs while instantiating the object
     * @throws IllegalAccessException    if there is illegal access to the object or its fields
     */
    @SuppressWarnings("unchecked")
    public <T> T generate(T instance)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        if (instance.getClass().isAnnotationPresent(NoFake.class)) {
            throw new NotAllowedFakeException("No Fake");
        }

        if (instance.getClass().isRecord()) {
            var resolveMap = objectMapper.convertValue(instance, HashMap.class);
            resolve(instance.getClass().getDeclaredFields(), resolveMap);
            return (T) objectMapper.convertValue(resolveMap, instance.getClass());
        }

        resolve(instance.getClass().getDeclaredFields(), instance);

        return instance;
    }

    /**
     * Generates an instance of the specified data class.
     *
     * @param dataClass the class of the data to generate
     * @return an instance of the specified data class
     * @throws NoSuchMethodException     if the constructor of the data class cannot be found
     * @throws InvocationTargetException if the constructor of the data class throws an exception
     * @throws InstantiationException    if the data class cannot be instantiated
     * @throws IllegalAccessException    if the constructor of the data class is not accessible
     */
    public <T> T generate(Class<T> dataClass)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException {
        Field[] fields = dataClass.getDeclaredFields();

        if (dataClass.isAnnotationPresent(NoFake.class)) {
            // Class has NoFake annotation, throw exception
            throw new NotAllowedFakeException("No Fake");
        }

        try {
            // Try to get constructor and create instance
            Constructor<T> constructor = dataClass.getDeclaredConstructor();
            T instance = constructor.newInstance();
            if (map == null) {
                resolve(fields, instance);
            } else {
                resolve(fields, map.apply(instance));
            }

            return instance;
        } catch (NoSuchMethodException ex) {
            // Can not find constructor or create instance.
            // Inject to `Map Object` and convert to input data class using ObjectMapper
            Map<String, Object> resolveMap = new HashMap<>();
            resolve(fields, resolveMap);
            return objectMapper.convertValue(resolveMap, dataClass);
        }
    }

    /**
     * Generates an object of type T using the provided data class and map function.
     *
     * @param dataClass the class of the object to be generated
     * @param map       the function used to map the generated object
     * @param <T>       the type of the object to be generated
     * @return the generated object of type T
     * @throws InvocationTargetException if the underlying method throws an exception
     * @throws NoSuchMethodException     if the requested method cannot be found
     * @throws InstantiationException    if a new instance of the object cannot be created
     * @throws IllegalAccessException    if access to the requested method is denied
     */
    @SuppressWarnings("unchecked")
    public <T> T generate(Class<T> dataClass, UnaryOperator<T> map)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        this.map = (Function<Object, Object>) map;
        return generate(dataClass);
    }

    /**
     * Generates a value of type T by applying a function to a FakeInjector.
     *
     * @param map the function to apply to the FakeInjector
     * @return the generated value of type T
     * @throws InvocationTargetException if the function throws an exception
     * @throws NoSuchMethodException     if the function cannot be found
     * @throws InstantiationException    if an instance of T cannot be created
     * @throws IllegalAccessException    if access to the function is denied
     */
    public <T> T generate(Function<FakeInjector, T> map)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        return generate(map.apply(this));
    }

    /**
     * Generates a random enum value from the given enum class.
     *
     * @param enumClass the enum class to generate a random value from
     * @return a random enum value
     */
    public <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        return enumClass.getEnumConstants()[rand.nextInt(
            enumClass.getEnumConstants().length)];
    }

    public <T> T randomList(List<T> ls) {
        return ls.get(rand.nextInt(ls.size()));
    }

    /**
     * Resolves the given array of fields by injecting dependencies into the provided instance.
     *
     * @param fields   the array of fields to be resolved
     * @param instance the instance into which the dependencies will be injected
     * @throws IllegalAccessException    if access to a field is denied
     * @throws InvocationTargetException if the method being invoked throws an exception
     * @throws NoSuchMethodException     if a method is not found
     * @throws InstantiationException    if an instance of a class cannot be created
     */
    private void resolve(Field[] fields, Object instance)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
        InstantiationException {
        for (Field field : fields) {
            if (!field.isAnnotationPresent(NoFake.class)) {
                inject(field, instance);
            }
        }
    }

    /**
     * Sets the value of a field in an instance using reflection.
     *
     * @param field    the field to be set
     * @param instance the instance on which to set the field
     * @throws IllegalAccessException    if the field is inaccessible
     * @throws InvocationTargetException if the method or constructor being invoked throws an
     *                                   exception
     * @throws NoSuchMethodException     if the method or constructor being invoked does not exist
     * @throws InstantiationException    if the class that declares the underlying field represents
     *                                   an abstract class, an interface, an array class, a
     *                                   primitive type, or void
     */
    @SuppressWarnings({"unchecked", "java:S3011", "java:S3776"})
    private void inject(Field field, Object instance)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException,
        InstantiationException {
        field.setAccessible(true); // can be access the private, protected, and public fields
        if (getField(field, instance) != null) {
            return; // already injected.
        }
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
            setField(field, instance, (float) faker.number().randomDouble(2, 0, 100));
        } else if (field.getType() == double.class || field.getType() == Double.class) {
            var d = getField(field, instance);
            if (d instanceof Double && !d.equals(0.0)) {
                // Cause built-in double not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, faker.number().randomDouble(2, 0, 100));
        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            var b = getField(field, instance);
            if (b instanceof Boolean && !b.equals(false)) {
                // Cause built-in boolean not allowed null.
                // So, it considers the unassigned status as false.
                return;
            }
            setField(field, instance, faker.bool().bool());
        } else if (field.getType() == Date.class) {
            setField(field, instance, faker.date().birthday());
        } else if (field.getType() == LocalDateTime.class) {
            setField(field, instance, LocalDateTime.now());
        } else if (field.getType() == LocalDate.class) {
            setField(field, instance, LocalDate.now());
        } else if (field.getType() == Long.class || field.getType() == long.class) {
            var l = getField(field, instance);
            if (l instanceof Long && !l.equals(0L)) {
                // Cause built-in long not allowed null.
                // So, it considers the unassigned status as zero.
                return;
            }
            setField(field, instance, faker.number().randomNumber());
        } else if (field.getType() == LocalTime.class) {
            setField(field, instance, LocalTime.now());
        } else if (field.getType().isEnum()) {
            setField(field, instance, randomEnum((Class<? extends Enum<?>>) field.getType()));
        } else if (field.getType().isArray()) {
            // array type has component type
            // use component type to generate
            var componentType = field.getType().getComponentType();
            var componentInstance =
                this.generate(componentType, 3).toArray(new Object[defaultArraySize]);
            setField(field, instance, componentInstance);
        } else if (field.getType() == List.class) {
            // list class has generic type
            // use generic type to generate
            var genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType parameterizedType) {
                var listType = parameterizedType.getActualTypeArguments()[0];
                Class<?> componentType = (Class<?>) listType;
                var componentInstance = this.generate(componentType, 3);
                setField(field, instance, componentInstance);
            }
        } else {
            // else type considers as object
            // recursively call generate
            setField(field, instance, this.generate(field.getType()));
        }
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
        if (instance instanceof Map) {
            var fieldName = resolvePropertyName(field);
            ((Map<String, Object>) instance).put(fieldName, value);
        } else {
            field.set(instance, value);
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
    private String resolvePropertyName(Field field)
        throws InstantiationException, IllegalAccessException, NoSuchMethodException,
        InvocationTargetException {
        if (field.isAnnotationPresent(JsonProperty.class)) {
            var annotation = field.getAnnotation(JsonProperty.class);
            var propertyName = annotation.value();
            if (propertyName != null) {
                return propertyName;
            }
        } else if (field.getDeclaringClass().isAnnotationPresent(JsonNaming.class)) {
            Constructor<? extends NamingBase> propertyNamingConstructor =
                (Constructor<? extends NamingBase>) field
                    .getDeclaringClass()
                    .getAnnotation(JsonNaming.class)
                    .value()
                    .getDeclaredConstructor();
            var propertyNamingStrategy = propertyNamingConstructor.newInstance();
            return propertyNamingStrategy.translate(field.getName());
        }

        return field.getName();
    }
}
