package com.assessment.recipetool.service;

import com.assessment.recipetool.model.Ingredient;
import com.assessment.recipetool.repo.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IngredientService {

    @Autowired
    IngredientRepository repo;

    public Ingredient findByName(String name) {
        List<Ingredient> ingredientList = list();
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.getName().equals(name)) return ingredient;
        }

        return null;
    }

    private List<Ingredient> list() {
        return repo.findAll();
    }

    public Ingredient save(Ingredient ingredient) {
        return repo.save(ingredient);
    }

}
