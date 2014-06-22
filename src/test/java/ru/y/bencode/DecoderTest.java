package ru.y.bencode;

import org.junit.Test;
import ru.y.bencode.pojo.Address;
import ru.y.bencode.pojo.Person;
import ru.y.bencode.pojo.PersonCplx;
import ru.y.bencode.pojo.Room;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static ru.y.bencode.BencodeUtils.DEFAULT_CHARSET;


public class DecoderTest {

    @Test
    public void testDecodeString() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("3:hey".getBytes(DEFAULT_CHARSET));

        assertEquals("hey", Decoder.decodeString(in));
    }

    @Test
    public void testDecodeInteger() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("i0e".getBytes(DEFAULT_CHARSET));

        assertEquals(new Integer(0), Decoder.decodeInteger(in));

        in = new ByteArrayInputStream("i-8e".getBytes(DEFAULT_CHARSET));

        assertEquals(new Integer(-8), Decoder.decodeInteger(in));

        in = new ByteArrayInputStream("i988e".getBytes(DEFAULT_CHARSET));

        assertEquals(new Integer(988), Decoder.decodeInteger(in));
    }

    @Test
    public void testDecodeList() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("l3:heye".getBytes(DEFAULT_CHARSET));

        assertEquals(new ArrayList() {{
            add("hey");
        }}, Decoder.decodeList(in));

        in = new ByteArrayInputStream("l3:hey3:keye".getBytes(DEFAULT_CHARSET));
        assertEquals(new ArrayList() {{
            add("hey");
            add("key");
        }}, Decoder.decodeList(in));

        in = new ByteArrayInputStream("li99e3:keyli99e3:keyee".getBytes(DEFAULT_CHARSET));
        assertEquals(new ArrayList() {{
            add(99);
            add("key");
            add(new ArrayList() {{
                add(99);
                add("key");
            }});
        }}, Decoder.decodeList(in));
    }

    @Test(expected = RuntimeException.class)
    public void testDecodeDictIncorrectKey() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("dli99e3:keyli99e3:keyee3:keye".getBytes(DEFAULT_CHARSET));
        Decoder.decodeDict(in);
    }

    @Test
    public void testDecodeDict() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("d3:key5:valuee".getBytes(DEFAULT_CHARSET));
        assertEquals(new HashMap() {{
            put("key", "value");
        }}, Decoder.decodeDict(in));

        in = new ByteArrayInputStream("d3:key5:value4:key2i4ee".getBytes(DEFAULT_CHARSET));
        assertEquals(new HashMap() {{
            put("key", "value");
            put("key2", 4);
        }}, Decoder.decodeDict(in));


        in = new ByteArrayInputStream("d4:key1li99e3:aaae4:key2d4:key1deee".getBytes(DEFAULT_CHARSET));

        Map m = new HashMap();
        Map m2 = new HashMap();
        m2.put("key1", new HashMap());

        m.put("key1", Arrays.asList(99, "aaa"));
        m.put("key2", m2);

        assertEquals(m, Decoder.decodeDict(in));
    }

    @Test
    public void testDecodePojo() throws Exception {
        Person p = new Person();
        p.setName("Ivan");
        p.setAge(11);
        p.setSomeNumbers(new ArrayList<Integer>(){{add(44); add(55);}});
        p.setSomeStrings(new HashMap<String, String>(){{put("k1", "v1"); put("k2", "v2");}});

        Address a = new Address();
        a.setCity("Kiev");
        a.setPostCode(99);
        p.setAddress(a);
        Room r = new Room();
        r.setRoom(132);
        a.setRoom(r);

        ByteArrayInputStream in = new ByteArrayInputStream("d7:addressd4:city4:Kiev8:postCodei99e4:roomd4:roomi132eee3:agei11e4:name4:Ivan11:someNumbersli44ei55ee11:someStringsd2:k12:v12:k22:v2ee".getBytes(DEFAULT_CHARSET));
        assertEquals(p, Decoder.decodePojo(in, Person.class));
    }

    @Test
    public void testDecodePojo2() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("d7:addressd4:city4:Kiev8:postCodei99e4:roomd4:roomi132eee3:agei11e4:name4:Ivan8:someListld4:city6:Harkov8:postCodei66e4:roomd4:roomi133eeed4:city7:Harkov28:postCodei66e4:roomd4:roomi133eeee7:someMapd4:kam1d4:city4:Kiev8:postCodei29e4:roomd4:roomi134eee4:kam2d4:city5:Kiev28:postCodei292e4:roomd4:roomi114eeee11:someNumbersli44ei55ee11:someStringsd2:k12:v12:k22:v2ee".getBytes(DEFAULT_CHARSET));

        PersonCplx p2 = Decoder.decodePojo(in, PersonCplx.class);
        PersonCplx p = new PersonCplx();
        p.setName("Ivan");
        p.setAge(11);
        p.setSomeNumbers(new ArrayList<Integer>(){{add(44); add(55);}});
        p.setSomeStrings(new HashMap<String, String>(){{put("k1", "v1"); put("k2", "v2");}});

        final Address al = new Address();
        al.setCity("Harkov");
        al.setPostCode(66);

        Room rl = new Room();
        rl.setRoom(133);
        al.setRoom(rl);

        final Address al2 = new Address();
        al2.setCity("Harkov2");
        al2.setPostCode(66);

        Room rl2 = new Room();
        rl2.setRoom(133);
        al2.setRoom(rl2);
        p.setSomeList(new ArrayList<Address>(){{ add(al); add(al2);}});

        final Address am = new Address();
        am.setCity("Kiev");
        am.setPostCode(29);

        Room rm = new Room();
        rm.setRoom(134);
        am.setRoom(rm);

        final Address am2 = new Address();
        am2.setCity("Kiev2");
        am2.setPostCode(292);

        Room rm2 = new Room();
        rm2.setRoom(114);
        am2.setRoom(rm2);

        p.setSomeMap(new HashMap<String, Address>(){{ put("kam1",am);put("kam2",am2);}});

        Address a = new Address();
        a.setCity("Kiev");
        a.setPostCode(99);
        p.setAddress(a);
        Room r = new Room();
        r.setRoom(132);
        a.setRoom(r);

        assertEquals(p, p2);
    }
}
