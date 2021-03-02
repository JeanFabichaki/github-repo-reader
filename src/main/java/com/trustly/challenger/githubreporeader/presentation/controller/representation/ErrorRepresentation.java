package com.trustly.challenger.githubreporeader.presentation.controller.representation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorRepresentation {

    private String title;
    private Integer status;
    private String details;

}