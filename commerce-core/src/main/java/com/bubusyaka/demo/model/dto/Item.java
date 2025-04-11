package com.bubusyaka.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private Long price;
    private Long providerId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String productCategory;
    private Long ageCategory;

    public void addToBuilder(StringBuilder builder) {
        appendField(builder, "id", id);
        appendField(builder, "n", name);
        appendField(builder, "p", price);
        appendField(builder, "pid", providerId);
        appendField(builder, "cD", createdDate);
        appendField(builder, "l", lastModifiedDate);
        appendField(builder, "pC", productCategory);
        appendField(builder, "aC", ageCategory);
    }

    private void appendField(StringBuilder builder, String fieldName, Object value) {
        if (value != null) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(fieldName).append("=").append(value);
        }
    }

    public void addToNameBuilder(StringBuilder builder) {
        appendName(builder,name);
    }
    private void appendName(StringBuilder builder, String value) {
        if (value != null) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(value);
        }
    }
}
