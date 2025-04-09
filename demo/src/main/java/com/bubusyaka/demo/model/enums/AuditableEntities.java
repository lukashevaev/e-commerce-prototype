package com.bubusyaka.demo.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum AuditableEntities {
    ITEM,
    ITEM_TEST;
//    ORDER,
//    PROVIDER;

    //ищет enum по строковому имени
    public static Optional<AuditableEntities> find(String name) {
        return Arrays.stream(AuditableEntities.values())
                .filter(auditableEntities -> auditableEntities.name().equals(name))
                .findFirst();
    }
}
