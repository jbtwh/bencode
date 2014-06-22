    @BencodeSerializable
    public class Address {
        private String city;
        private Integer postCode;
        private Room room;
        //getters/setters


    @BencodeSerializable
    public class PersonCplx {

                private String name;
                private Integer age;

                private Address address;

                private List<Integer> someNumbers;
                private Map<String, String> someStrings;

                private List<Address> someList;
                private Map<String, Address> someMap;