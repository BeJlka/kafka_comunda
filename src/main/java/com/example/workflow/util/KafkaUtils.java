package com.example.workflow.util;

import org.apache.kafka.common.header.Headers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KafkaUtils {

    public static <T> T apply(T object, Consumer<T> action) {
        action.accept(object);
        return object;
    }

    public static Map<String, String> headersToMap(Headers headers) {
        return apply(new HashMap<>(), it -> {
            headers.forEach(header -> it.put(header.key(), header.value() != null ? new String(header.value()) : null));
        });
    }
}
