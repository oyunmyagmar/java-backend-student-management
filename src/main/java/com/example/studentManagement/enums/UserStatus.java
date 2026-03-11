package com.example.studentManagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE("ACTIVE", "Идэвхитэй"),      // Баталгаажуулсан хэрэглэгч
    INACTIVE("INACTIVE", "Идэвхигүй"); // Код илгээсэн, хараахан баталгаажуулаагүй

    private final String name;
    private final String value;
}
