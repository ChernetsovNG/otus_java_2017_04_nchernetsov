package ru.otus.base.dataSets;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserDataSet extends BaseDataSet {

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "phones",
        joinColumns = {@JoinColumn(name = "user_id", nullable = false)},
        inverseJoinColumns = {@JoinColumn(name = "id")})
    private List<PhoneDataSet> phones = new ArrayList<>();

    public UserDataSet() {
    }

    public UserDataSet(String name, AddressDataSet address, List<PhoneDataSet> phones) {
        this.setName(name);
        this.setAddress(address);
        this.setPhones(phones);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
            "name='" + name + '\'' +
            ", address=" + address +
            ", phones=" + phones +
            "} " + super.toString();
    }
}
