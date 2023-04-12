package com.assessment.recipetool.service;

import com.assessment.recipetool.model.Recipe;
import com.assessment.recipetool.repo.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class RecipeServiceTests {

    @MockBean
    RecipeRepository recipeRepo;

    @Autowired
    RecipeService recipeService;

    @Test
    public void testDeleteById() {
        Long id = 1L;
        doNothing().when(recipeRepo).deleteById(id);
        recipeService.deleteById(id);
        verify(recipeRepo, times(1)).deleteById(id);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName("Test Recipe");
        Mockito.when(recipeRepo.findById(id)).thenReturn(Optional.of(recipe));

        // Act
        Recipe result = recipeService.findById(id);

        // Assert
        Assertions.assertEquals(recipe, result);
    }

    @Test
    public void testFindByName() {
        // Arrange
        String name = "Test Recipe";
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName(name);
        Mockito.when(recipeRepo.findAll()).thenReturn(Arrays.asList(recipe));

        // Act
        Recipe result = recipeService.findByName(name);

        // Assert
        Assertions.assertEquals(recipe, result);
    }
}


