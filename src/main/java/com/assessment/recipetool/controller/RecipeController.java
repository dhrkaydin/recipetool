package com.assessment.recipetool.controller;

import com.assessment.recipetool.dto.RecipeDto;
import com.assessment.recipetool.dto.RecipeFilterDto;
import com.assessment.recipetool.model.Recipe;
import com.assessment.recipetool.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody RecipeDto request) {
        Recipe recipe = recipeService.createRecipe(request);
        recipeService.save(recipe);

        recipeService.parseIngredientDto(recipe, request.getIngredients());

        return ResponseEntity.ok("Recipe created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto recipe) {
        Recipe updatedRecipe = recipeService.updateRecipe(id, recipe);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.findById(id);
        RecipeDto response = recipeService.recipeToDto(recipe);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        List<RecipeDto> response = recipeService.retrieveAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RecipeDto>> getFilteredRecipes(@RequestBody RecipeFilterDto filters) {
        List<RecipeDto> results = recipeService.getFilteredRecipes(filters);

        return ResponseEntity.ok(results);
    }
}
