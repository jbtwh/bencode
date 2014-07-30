package ru.y.bencode;


import com.google.common.base.Charsets;
import org.junit.Test;
import ru.y.bencode.pojo.Address;
import ru.y.bencode.pojo.Person;
import ru.y.bencode.pojo.PersonCplx;
import ru.y.bencode.pojo.Room;

import java.io.ByteArrayOutputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Test(expected = RuntimeException.class)
    public void shouldThrowUnsupportedInput(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder.encode(99L, out);
    }

    @Test
    public void testEncodeString() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder.encode("hey", out);

        assertEquals("3:hey",out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
    }

    @Test
    public void testEncodeStringUTF8() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder.encode("www", out, Charsets.UTF_8);

        assertEquals("3:www",out.toString(Charsets.UTF_8.name()));
    }

    @Test
    public void testEncodeInteger() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder.encode(0, out);

        assertEquals("i0e",out.toString(BencodeUtils.DEFAULT_CHARSET.name()));

        out = new ByteArrayOutputStream();
        Encoder.encode(-11, out);

        assertEquals("i-11e",out.toString(BencodeUtils.DEFAULT_CHARSET.name()));

        out = new ByteArrayOutputStream();
        Encoder.encode(88, out);

        assertEquals("i88e", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
    }

    @Test
    public void testEncodeList() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder.encode(Arrays.asList("hey", 88), out);

        assertEquals("l3:heyi88ee", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));

        out = new ByteArrayOutputStream();
        Encoder.encode(Collections.emptyList(), out);

        assertEquals("le", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
    }

    @Test
    public void testEncodeDict() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Map<String, Object> dictionary = new LinkedHashMap<String, Object>();
        dictionary.put("key4", Collections.emptyMap());
        dictionary.put("key1", "value1");
        dictionary.put("key2", 2);
        dictionary.put("key3", Collections.emptyList());
        dictionary.put("key5", Arrays.asList("hey", 88));

        Encoder.encode(dictionary, out);

        assertEquals("d4:key16:value14:key2i2e4:key3le4:key4de4:key5l3:heyi88eee", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));

        out = new ByteArrayOutputStream();

        Encoder.encode(Collections.emptyMap(), out, BencodeUtils.DEFAULT_CHARSET);

        assertEquals("de", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
    }

    @Test
    public void testEncodePojo() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
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


        Encoder.encodePojo(p, out);

        assertEquals("d7:addressd4:city4:Kiev8:postCodei99e4:roomd4:roomi132eee3:agei11e4:name4:Ivan11:someNumbersli44ei55ee11:someStringsd2:k12:v12:k22:v2ee", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
    }

    @Test
    public void testEncodePojo2() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
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

        Encoder.encodePojo(p, out);

        assertEquals("d7:addressd4:city4:Kiev8:postCodei99e4:roomd4:roomi132eee3:agei11e4:name4:Ivan8:someListld4:city6:Harkov8:postCodei66e4:roomd4:roomi133eeed4:city7:Harkov28:postCodei66e4:roomd4:roomi133eeee7:someMapd4:kam1d4:city4:Kiev8:postCodei29e4:roomd4:roomi134eee4:kam2d4:city5:Kiev28:postCodei292e4:roomd4:roomi114eeee11:someNumbersli44ei55ee11:someStringsd2:k12:v12:k22:v2ee", out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
    }
}
