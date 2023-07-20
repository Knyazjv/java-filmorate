insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY)
values ('email@email.com', 'superUser', 'name1', '1997-09-20');
insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY)
values ('ya@ya.ru', 'user', 'name2', '2002-03-24');
insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY)
values ('mail@mail.ru', 'userTest', 'name3', '2000-07-21');
insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY)
values ('gmail@gmail.com', 'superMan', 'name4', '1981-05-14');

insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
values ('Titanic', 'description1', '1997-12-09', 194, 4);
insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
values ('StarWars', 'description2', '1977-05-25', 121, 5);
insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
values ('Avengers', 'description3', '2019-04-26', 142, 3);
insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
values ('The Dark Knight', 'description4', '2008-07-18', 152, 5);

insert into FILM_GENRE (GENRE_ID, FILM_ID)
values (3, 1);
insert into FILM_GENRE (GENRE_ID, FILM_ID)
values (2, 1);
insert into FILM_GENRE (GENRE_ID, FILM_ID)
values (5, 1);
insert into FILM_GENRE (GENRE_ID, FILM_ID)
values (3, 2);
insert into FILM_GENRE (GENRE_ID, FILM_ID)
values (6, 3);
insert into FILM_GENRE (GENRE_ID, FILM_ID)
values (1, 3);
