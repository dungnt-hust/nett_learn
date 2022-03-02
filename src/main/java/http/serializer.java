package http;

import java.io.UnsupportedEncodingException;

public interface serializer {
    byte[] serializer(Object object) throws UnsupportedEncodingException;

    <T> T deserializer(Class<T> clazz, byte[] bytes);
}
