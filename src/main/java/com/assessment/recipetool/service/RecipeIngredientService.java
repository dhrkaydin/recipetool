package com.assessment.recipetool.service;

import com.assessment.recipetool.model.RecipeIngredient;
import com.assessment.recipetool.repo.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecipeIngredientService {

    @Autowired
    RecipeIngredientRepository repo;

    public List<RecipeIngredient> findByRecipeId(Long recipeId) {

        return repo.findByRecipeId(recipeId);
    }

    public RecipeIngredient save(RecipeIngredient recipeIngredient) {
        return repo.save(recipeIngredient);
    }
}
