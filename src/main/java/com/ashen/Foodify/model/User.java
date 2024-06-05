package com.ashen.Foodify.model;

import com.ashen.Foodify.DTO.RestaurantDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data //Getter Setter Methods
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private USER_ROLE role;

    @JsonIgnore //When fetching user don't need this list
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    private List<RestaurantDTO> favourites = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)//When deleting the user all the address should be delete
    private List<Address>addresses = new ArrayList<>();



}
