package com.bikkadit.store.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse <T>
{
    private List<T> content;

    private int pageNumber;
                                            
    private int pageSize;

    private Long totalElement;

    private int totalPage;

    private boolean lastPage;
}
