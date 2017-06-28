package ru.otus.base.dataSets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phones")
public class PhoneDataSet extends BaseDataSet {
    @Column(name = "code")
    private int code;

    @Column(name = "number")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(int code, String number) {
        this(null, code, number);
    }

    PhoneDataSet(Long id, int code, String number) {
        super(id);
        this.code = code;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
            "code=" + code +
            ", number='" + number + '\'' +
            "} " + super.toString();
    }
}
