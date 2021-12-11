package com.example.proyectofinalapps.model;

public class Person {

    private String id;
    private String fullName;
    private String email;
    private String identification;
    private String rol;
    private boolean isActive;

    public Person() {
    }

    public Person(String id, String fullName, String email, String identification, String rol, boolean isActive) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.identification = identification;
        this.rol = rol;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
