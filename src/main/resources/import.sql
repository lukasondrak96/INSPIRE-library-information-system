insert into author(id_author, name, surname) values (1, 'First', 'Author');
insert into author(id_author, name, surname) values (2, 'Second', 'Author');
insert into author(id_author, name, surname) values (3, 'Third', 'Author');

insert into item(id_item, title, type, year_of_publication) VALUES (1, 'First Book', 'BOOK', 1992);
insert into item(id_item, title, type, year_of_publication) VALUES (2, 'Second Book', 'BOOK', 1993);
insert into item(id_item, title, type, year_of_publication) VALUES (3, 'First Magazine', 'MAGAZINE', 2020);
insert into item(id_item, title, type, year_of_publication) VALUES (4, 'First CD', 'CD', 2002);

insert into author_item_link(id_item, id_author) VALUES (1,1);
insert into author_item_link(id_item, id_author) VALUES (2,1);
insert into author_item_link(id_item, id_author) VALUES (3,2);
insert into author_item_link(id_item, id_author) VALUES (4,1);
insert into author_item_link(id_item, id_author) VALUES (4,2);
insert into author_item_link(id_item, id_author) VALUES (4,3);

insert into client(id_client, email, name, surname) VALUES (1, 'first@email', 'First', 'Client');
insert into client(id_client, email, name, surname) VALUES (2, 'second@email', 'Second', 'Client');
insert into client(id_client, email, name, surname) VALUES (3, 'third@email', 'Third', 'Client');

insert into loan (id_loan, state, id_client) VALUES (1, 'ONGOING', 1);
insert into loan (id_loan, state, id_client) VALUES (2, 'RETURNED_IN_DUE_TIME', 2);
insert into loan (id_loan, state, id_client) VALUES (3, 'NOT_RETURNED_IN_DUE_TIME', 3);
insert into loan (id_loan, state, id_client) VALUES (4, 'ONGOING', 3);

insert into loan_of_item(id_loan_of_item, start_date, end_date, state, id_item, id_loan) VALUES (1, '2021-09-10', '2021-09-17', 'ONGOING', 1, 1);
insert into loan_of_item(id_loan_of_item, start_date, end_date, state, id_item, id_loan) VALUES (2, '2021-09-10', '2021-09-17', 'ONGOING', 2, 1);
insert into loan_of_item(id_loan_of_item, start_date, end_date, state, id_item, id_loan) VALUES (3, '2021-09-01', '2021-09-09', 'RETURNED_IN_DUE_TIME', 3, 2);
insert into loan_of_item(id_loan_of_item, start_date, end_date, state, id_item, id_loan) VALUES (4, '2021-09-01', '2021-09-09', 'NOT_RETURNED_IN_DUE_TIME', 3, 3);