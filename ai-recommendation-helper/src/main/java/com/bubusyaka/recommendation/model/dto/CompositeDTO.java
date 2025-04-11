package com.bubusyaka.recommendation.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompositeDTO {
    private Long id;
    private Long itemId;
    private String name;
    private Long userId;
    private String productCategory;
    private Long ageCategory;
    private Long userAge;

    public String addToBuilder(StringBuilder builder) {
//        StringBuilder builder = new StringBuilder();
        appendField(builder, "id", id);
        appendField(builder, "itId", itemId);
        appendField(builder, "n", name);
        appendField(builder, "uId", userId);
        appendField(builder, "pC", productCategory);
        appendField(builder, "aC", ageCategory);
        appendField(builder, "uA", userAge);
        return builder.toString().trim();
    }

    private void appendField(StringBuilder builder, String fieldName, Object value) {
        if (value != null) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(fieldName).append("=").append(value);
        }
    }
}

