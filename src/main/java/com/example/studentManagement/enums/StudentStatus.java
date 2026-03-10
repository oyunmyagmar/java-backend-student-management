package com.example.studentManagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudentStatus {
    ACTIVE("ACTIVE", "Идэвхитэй"),      // Баталгаажуулсан хэрэглэгч
    UNACTIVE("UNACTIVE", "Идэвхигүй"); // Код илгээсэн, хараахан баталгаажуулаагүй

    private final String name;
    private final String value;
}
