package com.miniyus.friday.common.util;

import com.miniyus.friday.common.fake.FakeInjector;

import java.util.ArrayList;
import java.util.List;

public class FakeUtil {
    private static FakeInjector fakeInjector;

    private FakeUtil() {
    }

    public static FakeInjector getInjector() {
        return fakeInjector;
    }

    public static void setInjector(FakeInjector fakeInjector) {
        FakeUtil.fakeInjector = fakeInjector;
    }

    public static <T> T generate(Class<T> dataClass) {
        try {
            return fakeInjector.generate(dataClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> generate(Class<T> dataClass, int count) {
        try {
            return fakeInjector.generate(dataClass, count);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static <T> T generate(T data) {
        try {
            return fakeInjector.generate(data);
        } catch (Exception e) {
            return null;
        }
    }
}
