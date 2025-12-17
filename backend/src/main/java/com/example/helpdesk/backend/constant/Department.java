package com.example.helpdesk.backend.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Department {
    OPERATION,
    FINANCE,
    FUNDING,
    ITSUPPORT,
    DIRECTOR,
    ITDEVELOPER,
    MANRISK,
    GAOPERATION,
    SUPERADMIN;

    @JsonCreator
    public static Department fromString(String value) {
        if (value == null) return null;
        String normalized = value.replaceAll("[\\s_\\-]+", "").toUpperCase();
        for (Department d : Department.values()) {
            String cand = d.name().replaceAll("[\\s_\\-]+", "").toUpperCase();
            if (cand.equals(normalized)) return d;
        }
        throw new IllegalArgumentException("Unknown Department value: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

//    OPERATION(1),
//    FINANCE(2),
//    FUNDING(3),
//    ITSUPPORT(4),
//    DIRECTOR(5),
//    ITDEVELOPER(6),
//    GAOPERATION(7);
//
//    private final int value;
//
//    Department(int value) {
//        this.value = value;
//    }
//
//    public static Department fromValue(int value) {
//        for (Department name : Department.values()) {
//            if (name.value == value) {
//                return name;
//            }
//        }
//        throw new IllegalArgumentException("Invalid Priority Status value: " + value);
//    }
//
//    public long getValue() {
//        return value;
//    }
}