package com.skoy.bootcamp_microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String id;
    private String customerType;
    private String name;
    private String surname;
    private String documentType;
    private String documentNumber;
    private String email;
    private String phone;
}
