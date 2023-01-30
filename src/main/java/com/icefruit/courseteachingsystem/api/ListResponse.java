package com.icefruit.courseteachingsystem.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ListResponse<T> extends Response {
    private DtoList<T> data;
}

