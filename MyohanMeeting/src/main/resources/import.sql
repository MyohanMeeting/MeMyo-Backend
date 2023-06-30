
-- 회원
insert into member (`email`, `password`, `nickname`, `phone_number`, `certified`) values ('admin@admin.com', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', '000-000-0000', 'CERTIFIED');
insert into member (`email`, `password`, `nickname`, `phone_number`, `certified`) values ('user@user.com', '$2a$10$VSxtBNYSlET5xrTI/UL52OuOwGxc9DRBC8LuX097b4V2GGwdRC3/m', 'user', '000-000-0000', 'CERTIFIED');
insert into member (`oauth_type`, `oauth_id`, `email`, `certified`) values ('KAKAOTALK', 'kakao', 'kakao@kakao.com', 'CERTIFIED');

-- 권한
insert into authority (`authority_name`) values ('ROLE_ADMIN');
insert into authority (`authority_name`) values ('ROLE_USER');

-- 회원별 권한
insert into member_authority (`member_id`, `authority_id`) values (1, 1);
insert into member_authority (`member_id`, `authority_id`) values (1, 2);
insert into member_authority (`member_id`, `authority_id`) values (2, 2);
insert into member_authority (`member_id`, `authority_id`) values (3, 2);
