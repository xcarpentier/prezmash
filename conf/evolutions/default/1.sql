# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table candidat (
  id                        bigint auto_increment not null,
  birthdate                 datetime,
  last_name                 varchar(255),
  first_name                varchar(255),
  name_party                varchar(255),
  image_name                varchar(255),
  color_party               varchar(255),
  url_profil                varchar(255),
  active                    tinyint(1) default 0,
  constraint pk_candidat primary key (id))
;

create table stats (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  candidat_id               bigint,
  constraint pk_stats primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  created_date              datetime,
  cookie                    varchar(255),
  ip                        varchar(255),
  user_agent                varchar(255),
  email                     varchar(255),
  constraint pk_user primary key (id))
;

alter table stats add constraint fk_stats_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_stats_user_1 on stats (user_id);
alter table stats add constraint fk_stats_candidat_2 foreign key (candidat_id) references candidat (id) on delete restrict on update restrict;
create index ix_stats_candidat_2 on stats (candidat_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table candidat;

drop table stats;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

