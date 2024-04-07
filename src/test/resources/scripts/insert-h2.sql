INSERT INTO tasks_categories(category_id,category_name, category_description)
VALUES (1, 'development', 'software development'),
       (2, 'testing', 'unit tests');

INSERT INTO tasks(task_id, task_name, task_description, deadline, category_id)
VALUES (1, 'backend development', 'be development in java', '2024-04-03T12:00:00', 1);

ALTER SEQUENCE tasks_categories_seq RESTART WITH 3;
ALTER SEQUENCE tasks_seq RESTART WITH 2;