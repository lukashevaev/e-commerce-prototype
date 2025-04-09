package com.bubusyaka.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MostPopularOrdersDto {

    private Long itemId;

    private Long itemCount;
}
