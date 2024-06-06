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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String fullName;
    private String email;
    private String password;
    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER ;

    @JsonIgnore //When fetching user don't need this list
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @ElementCollection
    private List<RestaurantDTO> favourites = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)//When deleting the user all the address should be delete
    private List<Address>addresses = new ArrayList<>();



    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
