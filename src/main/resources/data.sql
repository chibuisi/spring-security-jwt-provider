insert into roles(id,role_name) values (1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_CLIENT'),(4, 'ROLE_DEVELOPER');
insert into users(id,email,username,password) values (1,'ckamiaka2019@gmail.com', 'ckamiaka',
                                                      '$2a$12$ESK4BePk5B4cSek39pI3y.tGLeab1E0yqwFh30cY.r4l9BvcATH2e');
insert into users_roles(user_id, role_id) values (1,1);