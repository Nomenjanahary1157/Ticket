package com.techlab.ticketrepository.dtos;

import com.techlab.ticketrepository.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private User user;
    private String content;
}
