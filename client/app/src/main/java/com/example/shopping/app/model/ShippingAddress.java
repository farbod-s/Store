package com.example.shopping.app.model;


public class ShippingAddress {
    public String first_name;
    public String last_name;
    public String company;
    public String address_1;
    public String address_2;
    public String city;
    public String state;
    public String postcode;
    public String country;

    public ShippingAddress() {
        // nothing!
    }

    public ShippingAddress(String first_name, String last_name, String company, String address_1, String address_2, String city, String state, String postcode, String country) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.company = company;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.country = country;
    }
}
