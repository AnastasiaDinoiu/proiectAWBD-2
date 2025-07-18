package com.library.web.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReviewPageDTO {
    private List<ReviewDTO> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean first;
    private boolean last;
}