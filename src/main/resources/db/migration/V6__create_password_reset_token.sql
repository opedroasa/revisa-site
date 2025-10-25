create table password_reset_token (
  id          bigserial primary key,
  token       varchar(100) not null,
  user_id     bigint      not null references usuario(id) on delete cascade,
  expires_at  timestamp   not null,
  used_at     timestamp   null
);

create index idx_prt_token on password_reset_token(token);
