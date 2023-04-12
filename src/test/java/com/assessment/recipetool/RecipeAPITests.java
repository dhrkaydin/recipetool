package com.assessment.recipetool;

import com.assessment.recipetool.controller.RecipeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecipeAPITests {

	// Make sure the docker containers are running before executing.

	@Autowired
	RecipeController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void insertRecipeTest() {


	}
}
