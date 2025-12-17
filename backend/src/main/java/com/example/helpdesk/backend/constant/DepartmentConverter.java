package com.example.helpdesk.backend.constant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class DepartmentConverter implements AttributeConverter<Department, String> {

    @Override
    public String convertToDatabaseColumn(Department attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Department convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        // Normalize: remove whitespace, hyphens, underscores and uppercase
        // Note: backslashes must be escaped in Java string literals
        String pattern = "[\\n\\r\\t\\s_\\-]+";
        String normalized = dbData.replaceAll(pattern, "").toUpperCase();

        for (Department d : Department.values()) {
            String candidate = d.name().replaceAll(pattern, "").toUpperCase();
            if (candidate.equals(normalized)) return d;
        }

        // Fallback: attempt direct name match (case-insensitive)
        for (Department d : Department.values()) {
            if (d.name().equalsIgnoreCase(dbData.trim())) return d;
        }

        throw new IllegalArgumentException("Unknown Department value: " + dbData);
    }
}
