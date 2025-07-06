package com.library.web.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserPageDTO {
    private List<UserDTO> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean first;
    private boolean last;
}