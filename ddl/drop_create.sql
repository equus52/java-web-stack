
    alter table order_item 
        drop constraint FK_5gjhq2fmknk50h8859nf0bcmx

    drop table if exists customer cascade

    drop table if exists order_item cascade

    drop table if exists order_list cascade

    create table customer (
        id  bigserial not null,
        version int8 not null,
        name varchar(255) not null,
        rounding_mode int4,
        create_date_time timestamp not null,
        update_date_time timestamp not null,
        primary key (id)
    )

    comment on table customer is
        'Customer'

    comment on column customer.id is
        'identifier'

    comment on column customer.name is
        'Customer Name'

    create table order_item (
        id  bigserial not null,
        version int8 not null,
        order_id int8,
        create_date_time timestamp not null,
        update_date_time timestamp not null,
        primary key (id)
    )

    comment on column order_item.id is
        'identifier'

    create table order_list (
        id  bigserial not null,
        version int8 not null,
        order_date_time timestamp not null,
        create_date_time timestamp not null,
        update_date_time timestamp not null,
        primary key (id)
    )

    comment on column order_list.id is
        'identifier'

    alter table order_item 
        add constraint FK_5gjhq2fmknk50h8859nf0bcmx 
        foreign key (order_id) 
        references order_list
