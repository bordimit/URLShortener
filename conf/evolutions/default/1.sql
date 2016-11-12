# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table urlmodel (
  shortened                 varchar(255) not null,
  original                  varchar(255),
  exp_date                  datetime,
  constraint pk_urlmodel primary key (shortened))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table urlmodel;

SET FOREIGN_KEY_CHECKS=1;

