package com.product.ms.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsBean {
    private Long id;

    @NotEmpty(message = "The name cannot be empty")
    private String name;

    @Min(message = "The price cannot be less than zero", value = 0)
    @NotNull(message = "The price cannot be empty")
    private Double price;

    @NotEmpty(message = "The description cannot be empty")
    private String description;
}
