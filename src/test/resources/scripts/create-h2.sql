CREATE SEQUENCE IF NOT EXISTS tasks_categories_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS tasks_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS tasks_categories(
    category_id NUMBER AUTO_INCREMENT primary key,
    category_name varchar2 UNIQUE NOT NULL,
    category_description varchar2
);

CREATE TABLE IF NOT EXISTS tasks (
    task_id NUMBER primary key,
    task_name varchar2 NOT NULL,
    task_description varchar2,
    deadline timestamp NOT NULL,
    category_id NUMBER NOT NULL
);