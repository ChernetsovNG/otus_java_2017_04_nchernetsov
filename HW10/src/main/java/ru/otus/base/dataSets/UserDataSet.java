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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "phones",
        joinColumns = {@JoinColumn(name = "user_id")},
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

    public AddressDataSet getAddress() {
        return address;
    }

    public List<PhoneDataSet> getPhones() {
        return phones;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setAddress(AddressDataSet address) {
        this.address = address;
    }

    private void setPhones(List<PhoneDataSet> phones) {
        this.phones = phones;
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
