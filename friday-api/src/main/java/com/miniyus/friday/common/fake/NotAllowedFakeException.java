package com.miniyus.friday.common.fake;

/**
 * Not allowed fake data to be injected from FakeInjector.
 *
 * @author seongminyoo
 * @since 2023/10/21
 */
public class NotAllowedFakeException extends RuntimeException {
    /**
     * Not allowed injects fake data.
     *
     * @param message error message.
     * @see com.precisionbio.cuttysark.common.fake.annotation.NoFake
     * @see com.precisionbio.cuttysark.common.fake.FakeInjector
     */
    public NotAllowedFakeException(String message) {
        super(message);
    }
}
