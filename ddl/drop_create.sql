
    alter table OrderItem 
        drop constraint FK_6cxptya5vldowhtfdxs70ytw1

    drop table if exists Customer cascade

    drop table if exists OrderItem cascade

    drop table if exists order_list cascade

    create table Customer (
        id  bigserial not null,
        version int8 not null,
        name varchar(255) not null,
        createDateTime bytea not null,
        updateDateTime bytea not null,
        primary key (id)
    )

    comment on table Customer is
        'Customer'

    comment on column Customer.id is
        'identifier'

    comment on column Customer.name is
        'Customer Name'

    create table OrderItem (
        id  bigserial not null,
        version int8 not null,
        order_id int8,
        createDateTime bytea not null,
        updateDateTime bytea not null,
        primary key (id)
    )

    comment on column OrderItem.id is
        'identifier'

    create table order_list (
        id  bigserial not null,
        version int8 not null,
        orderDateTime bytea not null,
        createDateTime bytea not null,
        updateDateTime bytea not null,
        primary key (id)
    )

    comment on column order_list.id is
        'identifier'

    alter table OrderItem 
        add constraint FK_6cxptya5vldowhtfdxs70ytw1 
        foreign key (order_id) 
        references order_list
