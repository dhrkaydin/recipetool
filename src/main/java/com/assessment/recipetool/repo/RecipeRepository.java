package com.assessment.recipetool.repo;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assessment.recipetool.model.Recipe;


@Repository
@Table(name = "recipes")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}

