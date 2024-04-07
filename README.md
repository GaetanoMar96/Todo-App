# TODO APP

This is a simple spring boot app for managing tasks and related categories.

## Endpoints

### Get all tasks

**Endpoint:** `GET /api/v1/todo/tasks`

**Description:** Retrieves all tasks.

### Get task by ID

**Endpoint:** `GET /api/v1/todo/tasks/{id}`

**Description:** Retrieves a task by its ID.

### Get all categories

**Endpoint:** `GET /api/v1/todo/categories`

**Description:** Retrieves all categories.

### Get category by ID

**Endpoint:** `GET /api/v1/todo/categories/{id}`

**Description:** Retrieves a category by its ID.

### Create a new task

**Endpoint:** `POST /api/v1/todo/tasks`

**Description:** Creates a new task.
**Request Body:**
```json
{
    "name": "task name",
    "description": "task description",
    "deadline": "YYYY-MM-DDTHH:MM:SS",
    "categoryId": 1
}
```

### Create a new category

**Endpoint:** `POST /api/v1/todo/categories`

**Description:** Creates a new category.
**Request Body:**
```json
{
    "name": "category name",
    "description": "category description"
}
```

### Delete task by ID

**Endpoint:** `DELETE /api/v1/todo/tasks/{id}`

**Description:** Deletes a task by its ID.

### Delete category by ID

**Endpoint:** `DELETE /api/v1/todo/categories/{id}`

**Description:** Deletes a category by its ID.

### Update task deadline

**Endpoint:** `PATCH /api/v1/todo/tasks/{id}`

**Description:** Updates the deadline of a task.

**Request Body:**
```json
{
    "deadline": "YYYY-MM-DDTHH:MM:SS"
}
```
### Response Status Codes
 - 200 OK: The request was successful.
 - 201 Created: The task was created successfully.
 - 400 Bad Request: The request was invalid.
 - 404 Not Found: The requested resource was not found.

DB model:

![DB model](DBModel.png)

## How to Run the Application

To run the application, execute the following command in the terminal:

```bash
./gradlew bootRun
```
This will start the application on the default port 8080

## How to Run the Application using Docker

To run the application using docker, execute the following command in the terminal where the docker file is located:

```bash
docker build -t your-image-name .
```

then to run the container 

```bash
docker run -p 8080:8080 your-image-name
```

This will start the application on the default port 8080

## How to Test the Application

To test the application, execute the following command in the terminal:

```bash
./gradlew test
```

