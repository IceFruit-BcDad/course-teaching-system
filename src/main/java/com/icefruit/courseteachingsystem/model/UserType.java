package com.icefruit.courseteachingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity
public class UserType {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private String name;
}
