insert into roles(id,name) values (1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_CLIENT');
insert into users(id,email,username,password) values (1,'kelvin@gmail.com', 'kelvin', '123'),(2,'admin@brillo.com', 'brillo', '123');
insert into users_roles(user_id, role_id) values (1,1),
                                                (1,2);