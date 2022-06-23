package com.example.cybertestscroll;

public class User {

    public String firstName, lastName, shortName, idType, NIC, address01, address02, address03, province, district, city, postCode, emailAddress, password, UID;

    public User()
    {

    }

    public User(String firstName, String lastName, String shortName, String idType, String NIC, String address01, String address02, String address03, String province, String district, String city, String postCode, String emailAddress, String password, String UID)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.shortName=shortName;
        this.idType=idType;
        this.NIC=NIC;
        this.address01=address01;
        this.address02=address02;
        this.address03=address03;
        this.province=province;
        this.district=district;
        this.city=city;
        this.postCode=postCode;
        this.emailAddress=emailAddress;
        this.password=password;
        this.UID=UID;
    }
}
