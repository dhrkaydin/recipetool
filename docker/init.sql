CREATE DATABASE recipes_db;
USE recipes_db;

CREATE TABLE IF NOT EXISTS `recipes` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `instructions` TEXT NOT NULL,
  `is_vegetarian` BOOLEAN NOT NULL DEFAULT FALSE,
  `servings` INT NOT NULL,
  `time` INT NOT NULL,

  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `ingredients` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `recipe_ingredients` (
  `recipe_id` INT UNSIGNED NOT NULL,
  `ingredient_id` INT UNSIGNED NOT NULL,
  `quantity` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`recipe_id`, `ingredient_id`),
  CONSTRAINT `fk_recipe_ingredients_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_recipe_ingredients_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`) ON DELETE CASCADE
);


INSERT INTO recipes (name, description, instructions, is_vegetarian, servings, time) VALUES ('Ice Cube Sandwich', 'A budget-friendly classic', 'Add 4 ice cubes inbetween 2 slices of bread. Enjoy', true, 1, 2);
INSERT INTO ingredients (name) VALUES ('Ice Cube');
INSERT INTO ingredients (name) VALUES ('Bread');

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (1,1,"4 cubes");
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (1,2,"2 slices");

INSERT INTO recipes (name, description, instructions, is_vegetarian, servings, time) VALUES ('Ketchup Sandwich', 'Another budget-friendly classic', 'Squirt some ketchup inbetween 2 slices of bread. Enjoy', true, 1, 2);
INSERT INTO ingredients (name) VALUES ('Ketchup');

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (2,2,"2 slices");
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (2,3,"20 grams");
