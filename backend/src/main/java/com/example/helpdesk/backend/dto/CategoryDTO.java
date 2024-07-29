package com.example.helpdesk.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
}
