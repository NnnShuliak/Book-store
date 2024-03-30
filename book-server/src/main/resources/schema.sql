create table if not exists books(
    id uuid primary key ,
    isbn varchar(255) unique not null,
    title varchar(255) not null,
    author varchar(255),
    quantity integer
);

select * from books;