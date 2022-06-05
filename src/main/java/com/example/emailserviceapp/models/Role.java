package com.example.emailserviceapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Role {


    private String id;
    private RoleType roleType;

    public Role(RoleType roletype) {
        this.roleType = roletype;
    }
}
