
CREATE DATABASE trial1;
CREATE TABLE todo2 ( todo_id int, todo  varchar, is_completed  boolean,PRIMARY KEY (todo_id),FOREIGN KEY (task_id) REFERENCES tasks (task_id));


CREATE TABLE todo1 ( todo_id int, todo  varchar, is_completed  boolean,PRIMARY KEY (todo_id),FOREIGN KEY (task_id) REFERENCES tasks (task_id),FOREIGN KEY ( task_id) REFERENCES  tasks (task_id));


CREATE TABLE todo ( todo_id int, todo  varchar, is_completed  boolean,PRIMARY KEY (todo_id),FOREIGN KEY (task_id) REFERENCES tasks (task_id));
INSERT INTO todo VALUES (6,some good sleep,true);
INSERT INTO todo VALUES (2,make dinner,false);
INSERT INTO todo VALUES (3,have some sleep,false);
INSERT INTO todo VALUES (4,just lunch,true);
INSERT INTO todo VALUES (11,not good day,true);


