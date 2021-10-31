/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package business;

/**
 *
 * @author shivanginagar
 */
public class Person {
    private String personName;
    private int age;
    private Patient patient;
    private City City;
    private House House;
//    private String Community;
    private Community Community2;

    public Community getCommunity2() {
        return Community2;
    }

    public void setCommunity2(Community Community2) {
        this.Community2 = Community2;
    }

    public void setCity(City City) {
        this.City = City;
    }

    public void setHouse(House House) {
        this.House = House;
    }

//    public void setCommunity(String Community) {
//        this.Community = Community;
//    }

    public City getCity() {
        return City;
    }

    public House getHouse() {
        return House;
    }

//    public String getCommunity() {
//        return Community;
//    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    @Override
    public String toString()
    {
        return this.personName;
    }
}
