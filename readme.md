С помощью Java реализуйте, пожалуйста, сериализацию и десериализацию данных для формата b-encode. Оформите ответ в виде Maven Artifact.

Bencode сериализация/десириализация:

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Map toEncode = new HashMap();
        Map m2 = new HashMap();
        m2.put("key1", new HashMap());

        toEncode.put("key1", Arrays.asList(99, "aaa"));
        toEncode.put("key2", m2);

        Encoder.encode(toEncode, out);

        //System.out.println(out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
        //d4:key1li99e3:aaae4:key2d4:key1deee

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        Object decoded = Decoder.decode(in);
        //System.out.println(decoded);
        //{key1=[99, aaa], key2={key1={}}}

сериализация/десириализация простых pojo. Только для классов с аннотацией @BencodeSerializable. Поля не имеющие геттера и сеттера игнорируются.

```
@BencodeSerializable
public class PersonCplx {

    private String name;
    private Integer age;
    private Address address;
    private List<Integer> someNumbers;
    private Map<String, String> someStrings;
    private List<Address> someList;
    private Map<String, Address> someMap;
    //getters/setters

@BencodeSerializable
public class Address {
    private String city;
    private Integer postCode;
    private Room room;
    //getters/setters

@BencodeSerializable
public class Room {
    private Integer room;
    //getters/setters


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

        //System.out.println(out.toString(BencodeUtils.DEFAULT_CHARSET.name()));
        //d7:addressd4:city5:city58:postCodei23e4:roomd4:roomi999eee3:agei99e4:name3:Den8:someListld4:city5:city18:postCodei1222e4:roomd4:roomi888eeed4:city5:city28:postCodei6336e4:roomd4:roomi464eeee7:someMapd8:somekey1d4:city5:city38:postCodei23333e4:roomd4:roomi1256eee8:somekey2d4:city5:city48:postCodei765e4:roomd4:roomi8754eeee11:someNumbersli13ei99ee11:someStringsd4:k1113:aa14:k2226:stringee


        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        PersonCplx pout = Decoder.decodePojo(in, PersonCplx.class);

        //System.out.println(pin);
        //PersonCplx{name='Den', age=99, address=Address{city='city5', postCode=23, room=Room{room=999}}, someNumbers=[13, 99], someStrings={k222=string, k111=aa1}, someList=[Address{city='city1', postCode=1222, room=Room{room=888}}, Address{city='city2', postCode=6336, room=Room{room=464}}], someMap={somekey2=Address{city='city4', postCode=765, room=Room{room=8754}}, somekey1=Address{city='city3', postCode=23333, room=Room{room=1256}}}}

        //System.out.println(pout);
        //PersonCplx{name='Den', age=99, address=Address{city='city5', postCode=23, room=Room{room=999}}, someNumbers=[13, 99], someStrings={k111=aa1, k222=string}, someList=[Address{city='city1', postCode=1222, room=Room{room=888}}, Address{city='city2', postCode=6336, room=Room{room=464}}], someMap={somekey1=Address{city='city3', postCode=23333, room=Room{room=1256}}, somekey2=Address{city='city4', postCode=765, room=Room{room=8754}}}}
```

