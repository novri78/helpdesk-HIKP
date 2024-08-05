package com.example.helpdesk.backend.util;

public enum TicketStatus {
    OPEN(1),
    IN_PROGRESS(2),
    CLOSED(3);

    private final int value;

    TicketStatus(int value) {
        this.value = value;
    }

    public static TicketStatus fromValue(int value) {
        for (TicketStatus status : TicketStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TicketStatus value: " + value);
    }

    public int getValue() {
        return value;
    }
}

