package http;

import java.io.UnsupportedEncodingException;
import com.alibaba.fastjson.JSON;

public class JSONserializer implements serializer{
    @Override
    public byte[] serializer(Object object) throws UnsupportedEncodingException {
        return JSON.toJSONBytes (object);
    }

    @Override
    public <T> T deserializer(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
