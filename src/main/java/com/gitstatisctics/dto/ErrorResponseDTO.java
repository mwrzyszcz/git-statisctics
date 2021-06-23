package com.gitstatisctics.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ErrorResponseDTO {
    String message;
}
