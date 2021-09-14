insert into author(id_author, name, surname)
values (1, 'First', 'Author');
insert into author(id_author, name, surname)
values (2, 'Second', 'Author');
insert into author(id_author, name, surname)
values (3, 'Third', 'Author');

insert into item(id_item, title, type, year_of_publication)
VALUES (1, 'First Book', 'BOOK', 1992);
insert into item(id_item, title, type, year_of_publication)
VALUES (2, 'Second Book', 'BOOK', 1993);
insert into item(id_item, title, type, year_of_publication)
VALUES (3, 'First Magazine', 'MAGAZINE', 2020);
insert into item(id_item, title, type, year_of_publication)
VALUES (4, 'First CD', 'CD', 2002);

insert into author_item_link(id_item, id_author)
VALUES (1, 1);
insert into author_item_link(id_item, id_author)
VALUES (2, 1);
insert into author_item_link(id_item, id_author)
VALUES (3, 2);
insert into author_item_link(id_item, id_author)
VALUES (4, 1);
insert into author_item_link(id_item, id_author)
VALUES (4, 2);
insert into author_item_link(id_item, id_author)
VALUES (4, 3);

insert into client(id_client, email, name, surname)
VALUES (1, 'first@email', 'First', 'Client');
insert into client(id_client, email, name, surname)
VALUES (2, 'second@email', 'Second', 'Client');
insert into client(id_client, email, name, surname)
VALUES (3, 'third@email', 'Third', 'Client');

insert into loan (id_loan, state, start_date, id_client)
VALUES (1, 'NOT_YET_RETURNED', '2021-09-10', 1);
insert into loan (id_loan, state, start_date, id_client)
VALUES (2, 'RETURNED_ON_TIME', '2021-09-10', 2);
insert into loan (id_loan, state, start_date, id_client)
VALUES (3, 'RETURNED_LATE', '2021-09-01', 3);
insert into loan (id_loan, state, start_date, id_client)
VALUES (4, 'NOT_YET_RETURNED', '2021-09-03', 3);

insert into loan_of_item(id_loan_of_item, end_date, state, id_item, id_loan)
VALUES (1, '2021-09-17', 'NOT_YET_RETURNED', 1, 1);
insert into loan_of_item(id_loan_of_item, end_date, state, id_item, id_loan)
VALUES (2, '2021-09-17', 'RETURNED_ON_TIME', 2, 1);
insert into loan_of_item(id_loan_of_item, end_date, state, id_item, id_loan)
VALUES (3, '2021-09-09', 'RETURNED_ON_TIME', 3, 2);
insert into loan_of_item(id_loan_of_item, end_date, state, id_item, id_loan)
VALUES (4, '2021-09-09', 'RETURNED_LATE', 3, 3);
insert into loan_of_item(id_loan_of_item, end_date, state, id_item, id_loan)
VALUES (5, '2021-09-11', 'NOT_YET_RETURNED', 4, 4);