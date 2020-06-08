insert into role(id,role_name) values (1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_CLIENT');
insert into my_user(email,username,password) values ('kelvin@gmail.com', 'kelvin', '123'),('admin@brillo.com', 'brillo', '123');
insert into my_user_role(my_user_id, roles_id) values (select id from my_user where email = 'kelvin@gmail.com', 
													select id from role where role_name = 'ROLE_USER'),
                                                (select id from my_user where email = 'admin@brillo.com', 
                                                select id from role where role_name = 'ROLE_ADMIN');