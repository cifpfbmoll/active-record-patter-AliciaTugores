package org.pingpong.restjson;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name="Fruit")
@JsonPropertyOrder({"name", "decription"})
public class Fruit extends PanacheEntity {

    @NotBlank
    @Column(unique = true)
    public String name;

    @NotEmpty
    @Column
    public String description;

    public Fruit() {
    }

    public Fruit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*
    // substituit getName por este metodo en
    // la serializacion a JSON
    @JsonGetter("name")
    public String nombre() {
        return "UMAMI";
    }*/
}
