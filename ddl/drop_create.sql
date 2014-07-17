
    alter table OrderItem 
        drop constraint FK_6cxptya5vldowhtfdxs70ytw1

    drop table if exists Customer cascade

    drop table if exists OrderItem cascade

    drop table if exists order_list cascade

    create table Customer (
        id  bigserial not null,
        createDateTime bytea not null,
        updateDateTime bytea not null,
        version int8 not null,
        name varchar(255) not null,
        primary key (id)
    )

    create table OrderItem (
        id  bigserial not null,
        createDateTime bytea not null,
        updateDateTime bytea not null,
        version int8 not null,
        order_id int8,
        primary key (id)
    )

    create table order_list (
        id  bigserial not null,
        createDateTime bytea not null,
        updateDateTime bytea not null,
        version int8 not null,
        orderDateTime bytea not null,
        primary key (id)
    )

    alter table OrderItem 
        add constraint FK_6cxptya5vldowhtfdxs70ytw1 
        foreign key (order_id) 
        references order_list
