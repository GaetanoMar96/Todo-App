# TODO APP

This is a simple spring boot app for managing tasks and related categories.

## Endpoints

### Get all tasks

**Endpoint:** `GET /api/v1/todo/tasks`

**Description:** Retrieves all tasks.

### Get task by ID

**Endpoint:** `GET /api/v1/todo/tasks/{id}`

**Description:** Retrieves a task by its ID.

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

### Delete task by ID

**Endpoint:** `DELETE /api/v1/todo/tasks/{id}`

**Description:** Deletes a task by its ID.

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

## How to Test the Application

To test the application, execute the following command in the terminal:

```bash
./gradlew test
```

