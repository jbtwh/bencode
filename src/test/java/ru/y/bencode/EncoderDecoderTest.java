package ru.y.bencode;


import org.junit.Test;
import ru.y.bencode.pojo.Address;
import ru.y.bencode.pojo.PersonCplx;
import ru.y.bencode.pojo.Room;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EncoderDecoderTest {

    @Test
    public void testEncodeDecode() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Map toEncode = new HashMap();
        Map m2 = new HashMap();
        m2.put("key1", new HashMap());

        toEncode.put("key1", Arrays.asList(99, "aaa"));
        toEncode.put("key2", m2);

        Encoder.encode(toEncode, out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        Object decoded = Decoder.decode(in);

        assertEquals(toEncode, decoded);
    }

    @Test
    public void testEncodeDecodePojo() throws Exception {
        PersonCplx pin = new PersonCplx();
        pin.setName("Den");
        pin.setAge(99);
        pin.setSomeNumbers(new ArrayList<Integer>() {{
            add(13);
            add(99);
        }});
        pin.setSomeStrings(new HashMap<String, String>() {{
            put("k111", "aa1");
            put("k222", "string");
        }});

        final Address al = new Address();
        al.setCity("city1");
        al.setPostCode(1222);

        Room rl = new Room();
        rl.setRoom(888);
        al.setRoom(rl);

        final Address al2 = new Address();
        al2.setCity("city2");
        al2.setPostCode(6336);

        Room rl2 = new Room();
        rl2.setRoom(464);
        al2.setRoom(rl2);
        pin.setSomeList(new ArrayList<Address>() {{
            add(al);
            add(al2);
        }});

        final Address am = new Address();
        am.setCity("city3");
        am.setPostCode(23333);

        Room rm = new Room();
        rm.setRoom(1256);
        am.setRoom(rm);

        final Address am2 = new Address();
        am2.setCity("city4");
        am2.setPostCode(765);

        Room rm2 = new Room();
        rm2.setRoom(8754);
        am2.setRoom(rm2);

        pin.setSomeMap(new HashMap<String, Address>() {{
            put("somekey1", am);
            put("somekey2", am2);
        }});

        Address a = new Address();
        a.setCity("city5");
        a.setPostCode(23);
        pin.setAddress(a);
        Room r = new Room();
        r.setRoom(999);
        a.setRoom(r);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder.encodePojo(pin, out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        PersonCplx pout = Decoder.decodePojo(in, PersonCplx.class);

        System.out.println(pin);
        System.out.println(pout);

        assertEquals(pin, pout);
    }
}
