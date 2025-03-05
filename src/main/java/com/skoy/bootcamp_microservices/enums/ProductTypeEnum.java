package com.skoy.bootcamp_microservices.enums;

public enum ProductTypeEnum {
    BANK_ACCOUNT("CUENTAS BANCARIAS"), //PASIVOS
    CREDIT("CREDITOS"); //ACTIVOS

    private final String name;

    ProductTypeEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
