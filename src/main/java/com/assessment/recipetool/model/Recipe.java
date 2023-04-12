package com.assessment.recipetool.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("recipeIngredients")
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    private int servings;

    private int time;

    private boolean isVegetarian;

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", instructions='" + instructions + '\'' +
                ", recipeIngredients=" + recipeIngredients.size() +
                ", servings=" + servings +
                ", time=" + time +
                ", isVegetarian=" + isVegetarian +
                '}';
    }

}
