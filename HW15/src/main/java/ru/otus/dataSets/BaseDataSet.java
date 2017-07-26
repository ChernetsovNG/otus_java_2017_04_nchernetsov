package ru.otus.dataSets;

import javax.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD)
public class BaseDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    BaseDataSet() {
    }

    BaseDataSet(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}