package com.icefruit.courseteachingsystem.api;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DataResponse<T> extends Response {
    private T data;
}
