package ru.y.bencode.pojo;


import ru.y.bencode.BencodeSerializable;

import java.util.List;
import java.util.Map;

@BencodeSerializable
public class Person {

    private String name;
    private Integer age;

    private Address address;

    private List<Integer> someNumbers;
    private Map<String, String> someStrings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Integer> getSomeNumbers() {
        return someNumbers;
    }

    public void setSomeNumbers(List<Integer> someNumbers) {
        this.someNumbers = someNumbers;
    }

    public Map<String, String> getSomeStrings() {
        return someStrings;
    }

    public void setSomeStrings(Map<String, String> someStrings) {
        this.someStrings = someStrings;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", someNumbers=" + someNumbers +
                ", someStrings=" + someStrings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (address != null ? !address.equals(person.address) : person.address != null) return false;
        if (age != null ? !age.equals(person.age) : person.age != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (someNumbers != null ? !someNumbers.equals(person.someNumbers) : person.someNumbers != null) return false;
        if (someStrings != null ? !someStrings.equals(person.someStrings) : person.someStrings != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (someNumbers != null ? someNumbers.hashCode() : 0);
        result = 31 * result + (someStrings != null ? someStrings.hashCode() : 0);
        return result;
    }
}
