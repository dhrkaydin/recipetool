package com.assessment.recipetool.service;

import com.assessment.recipetool.dto.IngredientDto;
import com.assessment.recipetool.dto.RecipeDto;
import com.assessment.recipetool.dto.RecipeFilterDto;
import com.assessment.recipetool.model.Ingredient;
import com.assessment.recipetool.model.Recipe;
import com.assessment.recipetool.model.RecipeIngredient;
import com.assessment.recipetool.model.RecipeIngredientId;
import com.assessment.recipetool.repo.RecipeRepository;

import jakarta.persistence.criteria.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecipeService {

    @Autowired
    private EntityManager em;

    @Autowired
    private RecipeRepository repo;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private RecipeIngredientService recipeIngredientService;

    public Recipe findById(Long id) {
        return repo.findById(id).get();
    }
    
    public Recipe findByName(String name) {
        List<Recipe> recipeList = list();

        for (Recipe recipe : recipeList) {
            if (recipe.getName().equals(name)) return recipe;
        }

        return null;
    }

    public List<RecipeDto> retrieveAll() {
        List<Recipe> recipes = list();
        List<RecipeDto> result = new ArrayList<>();

        recipes.forEach(recipe -> result.add(recipeToDto(recipe)));

        return result;
    }

    private List<Recipe> list() {
        return repo.findAll();
    }

    public Recipe save(Recipe recipe) {
        return repo.save(recipe);
    }
    
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Recipe updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe recipe = repo.findById(id).orElse(null);
        if (recipe != null) {
            recipe.setName(recipeDto.getName());
            recipe.setDescription(recipeDto.getDescription());
            recipe.setInstructions(recipeDto.getInstructions());
            recipe.setServings(recipeDto.getServings());
            recipe.setTime(recipeDto.getTime());
            recipe.setVegetarian(recipeDto.isVegetarian());

            recipe.getRecipeIngredients().clear();

            parseIngredientDto(recipe, recipeDto.getIngredients());

            return repo.save(recipe);
        }
        
        throw new RuntimeException("this entry does not exist");
    }

    public Recipe createRecipe(RecipeDto request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.getName());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setVegetarian(request.isVegetarian());
        recipe.setTime(request.getTime());
        recipe.setServings(request.getServings());

        return recipe;
    }

    public RecipeIngredient createRecipeIngredient(Recipe recipe, Ingredient ingredient, String quantity) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(quantity);
        recipeIngredient.setRecipe(recipe);

        RecipeIngredientId id = new RecipeIngredientId();
        id.setIngredientId(ingredient.getId());
        id.setRecipeId(recipe.getId());
        recipeIngredient.setId(id);

        return recipeIngredient;
    }

    public void parseIngredientDto(Recipe recipe, List<IngredientDto> ingredients) {
        for (IngredientDto ingredientDto : ingredients) {
            Ingredient existingEntry = ingredientService.findByName(ingredientDto.getName());
            Ingredient ingredient = new Ingredient(ingredientDto.getName());

            if (existingEntry != null) {
                ingredient = existingEntry;
            }

            ingredientService.save(ingredient);

            RecipeIngredient recipeIngredient = createRecipeIngredient(recipe, ingredient, ingredientDto.getQuantity());
            recipeIngredientService.save(recipeIngredient);

        }
    }

    public RecipeDto recipeToDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setName(recipe.getName());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setInstructions(recipe.getInstructions());
        recipeDto.setVegetarian(recipe.isVegetarian());
        recipeDto.setServings(recipe.getServings());
        recipeDto.setTime(recipe.getTime());
        recipeDto.setIngredients(ingredientsToDto(recipe.getId()));

        return recipeDto;
    }

    public List<IngredientDto> ingredientsToDto(Long recipeId) {
        List<IngredientDto> ingredientDtos = new ArrayList<>();

        List <RecipeIngredient> recipeIngredients = recipeIngredientService.findByRecipeId(recipeId);

        recipeIngredients.forEach(recipeIngredient ->  {
            IngredientDto ingredientDto = new IngredientDto();

            ingredientDto.setName(recipeIngredient.getIngredient().getName());
            ingredientDto.setQuantity(recipeIngredient.getQuantity());

            ingredientDtos.add(ingredientDto);
        });

        return ingredientDtos;
    }

    public List<RecipeDto> getFilteredRecipes(RecipeFilterDto filters) {
        List<RecipeDto> result = new ArrayList<>();

        List<Recipe> recipes = findAllFiltered(filters);
        recipes.forEach(recipe -> result.add(recipeToDto(recipe)));

        return result;
    }

    public List<Recipe> findAllFiltered(RecipeFilterDto filterDto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> recipe = cq.from(Recipe.class);
        Join<Recipe, RecipeIngredient> recipeIngredientJoin = recipe.join("recipeIngredients");
        Join<RecipeIngredient, Ingredient> ingredientJoin = recipeIngredientJoin.join("ingredient");

        cq.select(recipe).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        if (filterDto.isVegetarian()) {
            predicates.add(cb.isTrue(recipe.get("isVegetarian")));
        }
        if (filterDto.getMinServings() > 0) {
            predicates.add(cb.greaterThanOrEqualTo(recipe.get("servings"), filterDto.getMinServings()));
        }
        if (filterDto.getIncludeIngredients() != null && !filterDto.getIncludeIngredients().isEmpty()) {
            List<String> includeIngredients = filterDto.getIncludeIngredients();
            for (String ingredientName : includeIngredients) {
                predicates.add(cb.equal(ingredientJoin.get("name"), ingredientName));
            }
            cq.groupBy(recipe.get("id")).having(cb.equal(cb.count(recipe.get("id")), includeIngredients.size()));
        }
        if (filterDto.getExcludeIngredients() != null && !filterDto.getExcludeIngredients().isEmpty()) {
            List<String> excludeIngredients = filterDto.getExcludeIngredients();
            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<RecipeIngredient> subqueryRecipeIngredient = subquery.from(RecipeIngredient.class);
            Join<RecipeIngredient, Ingredient> subqueryIngredientJoin = subqueryRecipeIngredient.join("ingredient");
            subquery.select(subqueryRecipeIngredient.get("recipe").get("id"))
                    .where(subqueryIngredientJoin.get("name").in(excludeIngredients));
            predicates.add(cb.not(recipe.get("id").in(subquery)));
        }
        if (filterDto.getSearchText() != null && !filterDto.getSearchText().isBlank()) {
            String searchText = filterDto.getSearchText().toLowerCase();
            predicates.add(cb.like(cb.lower(recipe.get("instructions")), "%" + searchText + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Recipe> query = em.createQuery(cq);
        return query.getResultList();
    }
}
