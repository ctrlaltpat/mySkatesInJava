package com.ctrlaltpat.MySkates.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class RollerSkatesPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private String plates;
    private String wheels;

    public RollerSkatesPair() {}

    public RollerSkatesPair(String name, String brand, String plates, String wheels) {
        this.name = name;
        this.brand = brand;
        this.plates = plates;
        this.wheels = wheels;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlates() {
        return plates;
    }

    public void setPlates(String plates) {
        this.plates = plates;
    }

    public String getWheels() {
        return wheels;
    }

    public void setWheels(String wheels) {
        this.wheels = wheels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RollerSkatesPair that = (RollerSkatesPair) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(name, that.name) &&
               Objects.equals(brand, that.brand) &&
               Objects.equals(plates, that.plates) &&
               Objects.equals(wheels, that.wheels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, plates, wheels);
    }
}
