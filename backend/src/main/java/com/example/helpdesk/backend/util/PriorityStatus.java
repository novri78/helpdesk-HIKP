package com.example.helpdesk.backend.util;

public enum PriorityStatus {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private final int value;

    PriorityStatus(int value) {
        this.value = value;
    }

    public static PriorityStatus fromValue(int value) {
        for (PriorityStatus status : PriorityStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Priority Status value: " + value);
    }

    public int getValue() {
        return value;
    }
}
