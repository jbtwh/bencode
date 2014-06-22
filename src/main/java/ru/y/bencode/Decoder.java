package ru.y.bencode;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.y.bencode.BencodeUtils.*;

public class Decoder {

    public static String decodeString(ByteArrayInputStream in) {
        return decodeString(in, DEFAULT_CHARSET);
    }

    public static String decodeString(ByteArrayInputStream in, Charset charset) {
        try {
            StringBuilder len = new StringBuilder();

            Character c;
            while (Character.isDigit( c = (char) in.read() )) len.append(c);

            if (c != STRINGDELIM) throw new RuntimeException("can't find string delim "+c);

            byte[] actualString = new byte[Integer.valueOf(len.toString())];
            in.read(actualString);

            return new String(actualString, charset);

        } catch (Exception e) {
            throw new RuntimeException("exception while decoding string", e);
        }
    }

    public static Integer decodeInteger(ByteArrayInputStream in) {
        return decodeInteger(in, DEFAULT_CHARSET);
    }

    public static Integer decodeInteger(ByteArrayInputStream in, Charset charset) {
        try {
            StringBuilder actualInt = new StringBuilder();
            Character c;

            if ((c = (char) in.read()) != INT) throw new RuntimeException("can't find integer beginning");
            if ((c = (char) in.read()) == '-') {
                actualInt.append('-');
                c = (char) in.read();
            }

            while (Character.isDigit(c)) {
                actualInt.append(c);
                c = (char) in.read();
            }

            if (c != END) throw new RuntimeException("can't find integer end");

            return Integer.valueOf(actualInt.toString());

        } catch (Exception e) {
            throw new RuntimeException("exception while decoding integer", e);
        }
    }

    public static List decodeList(ByteArrayInputStream in) {
        return decodeList(in, DEFAULT_CHARSET);
    }

    public static List decodeList(ByteArrayInputStream in, Charset charset) {
        try {
            Character c;
            if ((c = (char) in.read()) != LIST) throw new RuntimeException("can't find list beginning");

            List<Object> list = new ArrayList<Object>();

            in.mark(1); //if c==-1
            while ((c = (char) in.read()) != END && c != -1) {
                in.reset();
                Object element = decode(in, charset);
                list.add(element);
                in.mark(1);
            }
            in.reset();

            if ((c = (char) in.read()) != END) throw new RuntimeException("can't find list end");

            return list;

        } catch (Exception e) {
            throw new RuntimeException("exception while decoding list", e);
        }
    }

    public static Map decodeDict(ByteArrayInputStream in) {
        return decodeDict(in, DEFAULT_CHARSET);
    }

    public static Map decodeDict(ByteArrayInputStream in, Charset charset) {
        try {
            Character c;
            if ((c = (char) in.read()) != DICT) throw new RuntimeException("can't find dict beginning");
            Map<String, Object> dict = new LinkedHashMap<String, Object>();

            in.mark(1);
            while ((c = (char) in.read()) != END && c != -1) {
                in.reset();

                String key = (String) decode(in, charset);
                Object value = decode(in, charset);
                dict.put(key, value);

                in.mark(1);
            }
            in.reset();

            if ((c = (char) in.read()) != END) throw new RuntimeException("can't find dict end");

            return dict;

        } catch (Exception e) {
            throw new RuntimeException("exception while decoding dict", e);
        }
    }

    public static<T> T decodePojo(ByteArrayInputStream in, Class<T> c) {
        return decodePojo(in, c, DEFAULT_CHARSET);
    }

    public static<T> T decodePojo(ByteArrayInputStream in, Class<T> c, Charset charset) {
        try{
            for(Class pc : ReflectionUtils.getTypesRecur(c))
                if (pc!=Integer.class && pc!=String.class && pc!=Map.class && pc!=List.class && !pc.isAnnotationPresent(BencodeSerializable.class))
                    throw new RuntimeException("unsupported pojo class "+pc);

            T o = c.newInstance();
            Map<String, Object> dict = decodeDict(in, charset);

            ReflectionUtils.setValuesForFields(dict, o);
            return o;
        } catch (Exception e) {
            throw new RuntimeException("exception while decoding pojo", e);
        }
    }

    public static Object decode(ByteArrayInputStream in) {
        return decode(in, DEFAULT_CHARSET);
    }

    public static Object decode(ByteArrayInputStream in, Charset charset) {
        //read first byte and reset
        in.mark(1);
        Integer c = in.read();
        in.reset();

        if (c == -1) throw new RuntimeException("no more elements");

        if (c == (char) INT) {
            return decodeInteger(in, charset);
        } else if (Character.isDigit(c)) {
            return decodeString(in, charset);
        } else if (c == (char) LIST) {
            return decodeList(in, charset);
        } else if (c == (char) DICT) {
            return decodeDict(in, charset);
        } else {
            throw new RuntimeException("can't parse " + c);
        }
    }
}
