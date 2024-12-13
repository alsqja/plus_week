package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.jetbrains.annotations.TestOnly;


@Entity
@Getter
// TODO: 6. Dynamic Insert
@DynamicInsert
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(nullable = false)
    private String status;

    public Item(String name, String description, User manager, User owner) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.owner = owner;
        this.status = "PENDING";
    }

    public Item() {
        this.status = "PENDING";
    }

    @TestOnly
    public Item(Long id, String testItemName, String testDescription, User manager, User owner, String status) {
        this.id = id;
        this.name = testItemName;
        this.description = testDescription;
        this.manager = manager;
        this.owner = owner;
        this.status = status;
    }
}
