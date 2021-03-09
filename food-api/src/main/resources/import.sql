insert into cozinha (id, nome) values (1, 'Tailandesa');
insert into cozinha (id, nome) values (2, 'Indiana');
insert into cozinha (id, nome) values (3, 'Brasileira');

insert into restaurante (id, nome, taxa_frete, cozinha_id) values (1, 'Thai Gourmet', 10, 1);
insert into restaurante (id, nome, taxa_frete, cozinha_id) values (2, 'Thai Delivery', 9.5, 1);
insert into restaurante (id, nome, taxa_frete, cozinha_id) values (3, 'Tuk Tuk Comida Indiana', 5.5, 2);
insert into restaurante (id, nome, taxa_frete, cozinha_id) values (4, 'Comida Mineira', 6.5, 3);
insert into restaurante (id, nome, taxa_frete, cozinha_id) values (5, 'Restaurante com frete gratis', 0.0, 3);



insert into Estado (id, nome) values (1, 'São Paulo');
insert into Estado (id, nome) values (2, 'Minas Gerais');

insert into Cidade(id, nome, estado_id) values (1, 'São Paulo', 1);
insert into Cidade(id, nome, estado_id) values (2, 'Belo Horizonte', 2);