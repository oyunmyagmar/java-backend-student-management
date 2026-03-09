package com.example.studentManagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE("ACTIVE", "Үүссэн"),      // Хүснэгт дээр үүссэн боловч хараахан илгээгдээгүй
    UNACTIVE("UNACTIVE", "Идэвхигүй"),       // Илгээгдсэн
    ;
    private final String name;
    private final String value;
}
