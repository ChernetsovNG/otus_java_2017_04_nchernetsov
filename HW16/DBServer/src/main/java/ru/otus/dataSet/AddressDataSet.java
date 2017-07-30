package ru.otus.dataSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "addresses")
public class AddressDataSet extends BaseDataSet {
    @Column(name = "street")
    private String street;

    @Column(name = "index")
    private int index;

    public AddressDataSet() {
    }

    public AddressDataSet(String street, int index) {
        this(null, street, index);
    }

    AddressDataSet(Long id, String street, int index) {
        super(id);
        this.street = street;
        this.index = index;
    }

    public String getStreet() {
        return street;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
            "street='" + street + '\'' +
            ", index=" + index +
            '}';
    }
}