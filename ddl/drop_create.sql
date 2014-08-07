
    alter table Student_Course 
        drop constraint FK_hqu4nc0js0t5m5vmh4xx7kafi

    alter table Student_Course 
        drop constraint FK_7lwxxnu7wbg63k8so3tb6sn0g

    alter table order_item 
        drop constraint FK_5gjhq2fmknk50h8859nf0bcmx

    drop table if exists Student_Course cascade

    drop table if exists course cascade

    drop table if exists customer cascade

    drop table if exists order_item cascade

    drop table if exists order_list cascade

    drop table if exists student cascade

    create table Student_Course (
        Student_id int8 not null,
        courseList_id int8 not null
    )

    create table course (
        id  bigserial not null,
        version int8 not null,
        name varchar(255) not null,
        create_date_time timestamp not null,
        update_date_time timestamp not null,
        primary key (id)
    )

    comment on column course.id is
        'identifier'

    create table customer (
        id  bigserial not null,
        version int8 not null,
        name varchar(255) not null,
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

    create table student (
        id  bigserial not null,
        version int8 not null,
        name varchar(255) not null,
        create_date_time timestamp not null,
        update_date_time timestamp not null,
        primary key (id)
    )

    comment on column student.id is
        'identifier'

    alter table Student_Course 
        add constraint FK_hqu4nc0js0t5m5vmh4xx7kafi 
        foreign key (courseList_id) 
        references course

    alter table Student_Course 
        add constraint FK_7lwxxnu7wbg63k8so3tb6sn0g 
        foreign key (Student_id) 
        references student

    alter table order_item 
        add constraint FK_5gjhq2fmknk50h8859nf0bcmx 
        foreign key (order_id) 
        references order_list
