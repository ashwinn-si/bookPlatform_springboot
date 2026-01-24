package com.example.BookPlatform.DTO;

public enum RoleEnum {
    USER("user"),
    ADMIN("admin");

    private final String value;


    RoleEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}

