# Recipe Tool (Abn Tech Assessment)

### How to run
- cd into the docker folder and run: ```docker-compose up -d```
- run the "RecipeAPI" class.

### Endpoints
#### GET
<b>/recipes/all</b>  

Gets all recipes 

<b>/recipes/{id}</b>

Gets a specific recipe by id

<b>/recipes/filter</b>

Gets all recipes that match your filter criteria. Filter criteria should be provided in the body of the GET request in the following JSON format, with every value that shouldn't be filtered on left blank:

```
{
"vegetarian": null,
"includeIngredients": ["example", "example2"],
"excludeIngredients": [],
"searchText": "",
"minServings": 0
} 
```

#### PUT

<b>/recipes/{id}</b>

updates a specific recipe by id. if it previously contained ingredients which were
only used by this recipe, they will be deleted from the database.

#### DELETE

<b>/recipes/{id}</b>

deletes a specific recipe by id. if it contained any ingredients which are not used in any other recipe, these
ingredients will also be deleted from the database.

#### POST
<b>/recipes</b>

Accepts a recipe in JSON format, the structure of the request body should look like this:
```
[
    {
        "name": "Ice Cube Sandwich",
        "description": "A budget-friendly classic",
        "instructions": "Add 4 ice cubes inbetween 2 slices of bread. Enjoy",
        "ingredients": [
            {
                "name": "Ice Cube",
                "quantity": "4 cubes"
            },
            {
                "name": "Bread",
                "quantity": "2 slices"
            }
        ],
        "servings": 1,
        "time": 2,
        "vegetarian": true
    }
] 
```

### Architecture mindset and approach
First step is: How I am going to structure my database?
It was clear to me that I can not store everything in 1 table. I considered
a many-to-many relationship between a recipe table and an ingredients table,
but decided to go for a join table approach as this gave me more control and scalability.
For example if I want to implement a quantities column per ingredient for a specific recipe.

After this I created the models which match the database entities, and created relationships between them
to ensure that an Ingredient needs to belong to at least 1 recipe to exist, and that deleting recipes will also
delete the recipeIngredients that belong to it etc.

I am only making a local MySQL container for local testing/debugging purposes, of course for production the database connection will be different,
I didn't implement such thing because it really depends on the environment and type of db. For integration tests
I would like to use Testcontainers.

Initial challenges: Structuring the database, deciding on the input request format. Making sure every entry can be properly updated/deleted.
Want to use mapstruct for mapping dto's to models but I'm not experienced with it so I will try to implement it in the end if I have time.
Also I know that I can use CriteriaBuilder to make a dynamic filtering system but I have not used it before. Definitely will take most of my time.

However my first priority is a working MVP with basic CRUD functionalities and a database structure that I'm satisfied with.

General approach on the structure:
- Should have minimal business logic in the controller.
- Service layer inbetween controller and repositories.
- Dto's to receive requests and send responses, and models for database entities.

Things I will leave to the end
- Intergration Testing (using Testcontainers)
- Exception handling.
  (Both important but also time consuming, I really want to get the filter right as it's easily the most complex part of the logic.)