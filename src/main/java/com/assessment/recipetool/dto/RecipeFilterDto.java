package com.assessment.recipetool.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeFilterDto {
    private boolean vegetarian;
    private int minServings;
    private List<String> includeIngredients;
    private List<String> excludeIngredients;
    private String searchText;
}
