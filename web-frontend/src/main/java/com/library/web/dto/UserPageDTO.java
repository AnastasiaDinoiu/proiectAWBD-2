package com.library.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDTO {
    private List<UserDTO> content = new ArrayList<>();
    private int totalPages = 0;
    private long totalElements = 0;
    private int size = 0;
    private int number = 0;
    private boolean first = true;
    private boolean last = true;
    private boolean empty = true;
    private int numberOfElements = 0;
    
    // Constructor pentru cazul de eroare
    public UserPageDTO(boolean isEmpty) {
        this();
        this.empty = isEmpty;
    }
}