package com.assessment.recipetool.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDto {
    private String name;
    private String description;
    private String instructions;
    private List<IngredientDto> ingredients;
    private int servings;
    private int time;
    private boolean isVegetarian;
}
