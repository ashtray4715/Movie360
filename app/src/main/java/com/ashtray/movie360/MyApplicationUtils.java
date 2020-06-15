package com.ashtray.movie360;

import java.util.List;

public class MyApplicationUtils {

    @SuppressWarnings("unchecked")
    public static <T extends List<?>> T objectToStringList(Object obj) {
        return (T) obj;
    }

}
