package com.shodh.contestplatform.dto;

import lombok.Data;
import java.util.List;

@Data
public class ContestResponse {
    private Long id;
    private String name;
    private String description;
    private List<ProblemDto> problems;
}