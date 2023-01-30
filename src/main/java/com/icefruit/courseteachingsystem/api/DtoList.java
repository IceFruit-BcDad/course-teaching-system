package com.icefruit.courseteachingsystem.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoList<T> {
    private List<T> list;
    private int limit;
    private int offset;
    private long total;
    private int totalPages;
}
