package ru.y.bencode.pojo;


import ru.y.bencode.BencodeSerializable;

import java.util.List;
import java.util.Map;

//more complex data
@BencodeSerializable
public class PersonCplx {

    private String name;
    private Integer age;

    private Address address;

    private List<Integer> someNumbers;
    private Map<String, String> someStrings;

    private List<Address> someList;
    private Map<String, Address> someMap;

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

    public List<Address> getSomeList() {
        return someList;
    }

    public void setSomeList(List<Address> someList) {
        this.someList = someList;
    }

    public Map<String, Address> getSomeMap() {
        return someMap;
    }

    public void setSomeMap(Map<String, Address> someMap) {
        this.someMap = someMap;
    }

    @Override
    public String toString() {
        return "PersonCplx{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", someNumbers=" + someNumbers +
                ", someStrings=" + someStrings +
                ", someList=" + someList +
                ", someMap=" + someMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonCplx that = (PersonCplx) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (age != null ? !age.equals(that.age) : that.age != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (someList != null ? !someList.equals(that.someList) : that.someList != null) return false;
        if (someMap != null ? !someMap.equals(that.someMap) : that.someMap != null) return false;
        if (someNumbers != null ? !someNumbers.equals(that.someNumbers) : that.someNumbers != null) return false;
        if (someStrings != null ? !someStrings.equals(that.someStrings) : that.someStrings != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (someNumbers != null ? someNumbers.hashCode() : 0);
        result = 31 * result + (someStrings != null ? someStrings.hashCode() : 0);
        result = 31 * result + (someList != null ? someList.hashCode() : 0);
        result = 31 * result + (someMap != null ? someMap.hashCode() : 0);
        return result;
    }
}
