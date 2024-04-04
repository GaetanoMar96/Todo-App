INSERT INTO tasks_categories(category_id,category_name, category_description)
VALUES (4, 'development', 'software development'),
       (5, 'testing', 'unit tests');

INSERT INTO tasks(task_id, task_name, task_description, deadline, category_id)
VALUES (2, 'backend development', 'be development in java', '2024-04-03T12:00:00', 4);