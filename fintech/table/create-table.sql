create table usr_info(
                         id bigint not null auto_increment primary key,
                         usr_key varchar(32) not null unique,
                         usr_reg_num varchar(50) not null,
                         usr_nm varchar(20) not null,
                         usr_icm_amt bigint default 0 not null
);

create table loan_review(
                            id bigint not null auto_increment primary key,
                            usr_key varchar(32) not null unique,
                            loan_lmt_amt bigint default 0 not null,
                            loan_intrt double default 0.0 not null
);

insert into usr_info
(id, usr_key, usr_reg_num, usr_nm, usr_icm_amt)
VALUES
(
 1, '12345', '12345', 'sim', 1000
);
