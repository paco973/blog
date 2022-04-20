package com.quest.etna.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
public class Address {


    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    int id;

    @Column(length = 100, nullable = false)
    String street;

    @Column(name = "postal_code", length = 30, nullable = false)
    String postal_code;

    @Column(length = 50, nullable = false)
    String city;

    @Column(length = 50, nullable = false)
    String country;

    @Column(name = "creation_date")
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Date creationDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Date updateDate;

    @ManyToOne
    User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return street.equals(address.street) &&
                postal_code.equals(address.postal_code) &&
                city.equals(address.city) &&
                country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postal_code, city, country);
    }
}

