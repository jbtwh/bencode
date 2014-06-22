package ru.y.bencode;


import com.google.common.primitives.Bytes;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.*;

import static ru.y.bencode.BencodeUtils.*;

public class Encoder {

    public static void encode(String in, ByteArrayOutputStream out) {
        encode(in, out, DEFAULT_CHARSET);
    }

    public static void encode(String in, ByteArrayOutputStream out, Charset charset) {
        try {
            byte[] secondHalf = in.getBytes(charset);
            String firstHalf = Integer.toString(secondHalf.length) + STRINGDELIM;

            out.write(Bytes.concat(firstHalf.getBytes(charset), secondHalf));
        } catch (Exception e) {
            throw new RuntimeException("exception while encoding string "+in, e);
        }
    }

    public static void encode(Integer in, ByteArrayOutputStream out) {
        encode(in, out, DEFAULT_CHARSET);
    }

    public static void encode(Integer in, ByteArrayOutputStream out, Charset charset) {
        try {
            String result = String.valueOf(INT) + in + END;

            out.write(result.getBytes(charset));
        } catch (Exception e) {
            throw new RuntimeException("exception while encoding int", e);
        }
    }

    public static void encode(List in, ByteArrayOutputStream out) {
        encode(in, out, DEFAULT_CHARSET);
    }

    public static void encode(List in, ByteArrayOutputStream out, Charset charset) {
        try {
            out.write(LIST);

            for (Object o : in) encode(o, out, charset);

            out.write(END);

        } catch (Exception e) {
            throw new RuntimeException("exception while encoding list", e);
        }
    }

    public static void encode(Map<String, Object> in, ByteArrayOutputStream out) {
        encode(in, out, DEFAULT_CHARSET);
    }

    //All keys must be byte strings and must appear in lexicographical order.
    public static void encode(Map<String, Object> in, ByteArrayOutputStream out, Charset charset) {
        try {
            out.write(DICT);
            List<String> keys = new ArrayList(in.keySet());
            Collections.sort(keys);

            for (String key : keys) {
                encode(key, out, charset);
                encode(in.get(key), out, charset);
            }
            out.write(END);

        } catch (Exception e) {
            throw new RuntimeException("exception while encoding dict", e);
        }
    }

    public static void encodePojo(Object in, ByteArrayOutputStream out) {
        encodePojo(in, out, DEFAULT_CHARSET);
    }

    //simple pojo serialization
    public static void encodePojo(Object in, ByteArrayOutputStream out, Charset charset) {
        try {
            encode(ReflectionUtils.getFieldsValues(in), out, charset);
        } catch (Exception e) {
            throw new RuntimeException("exception while encoding pojo", e);
        }
    }

    public static void encode(Object in, ByteArrayOutputStream out) {
        encode(in, out, DEFAULT_CHARSET);
    }

    public static void encode(Object in, ByteArrayOutputStream out, Charset charset) {
        try {
            if (in==null) throw new RuntimeException("null input");
            else if (in instanceof String) {
                encode((String) in, out, charset);
            } else if (in instanceof Integer) {
                encode((Integer) in, out, charset);
            } else if (in instanceof Map) {
                encode((Map) in, out, charset);
            } else if (in instanceof List) {
                encode((List) in, out, charset);
            } else if (in.getClass().isAnnotationPresent(BencodeSerializable.class)) {
                encodePojo(in, out, charset);

            } else throw new RuntimeException("unsupported input "+in);

        } catch (Exception e) {
            throw new RuntimeException("exception while encoding object", e);
        }
    }
}
