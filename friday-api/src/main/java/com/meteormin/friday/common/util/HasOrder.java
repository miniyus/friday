package com.meteormin.friday.common.util;

public interface HasOrder {
    int order();

    default int compareTo(HasOrder o) {
        return Integer.compare(order(), o.order());
    }
}
