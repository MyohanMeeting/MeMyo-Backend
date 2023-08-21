-- 회원(운영자)
insert into member (`created_at`, `email`, `password`, `nickname`, `phone_number`, `certified`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'admin@admin.com', '$2a$10$EI3YbKFob4MKhoJuE6y87.vu37kjdstBvK.YWNU1/U0rWcy1s0Slu', 'admin', '000-000-0000', 'CERTIFIED');

-- 업로드
-- 유저 프로필 이미지(관리자 회원이 업로드)
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'PROFILE', 'user.jpg', 'e450b369-5a55-4efc-be51-41f7e2a0be57-1692020811373.jpg', 17770, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/profile/e450b369-5a55-4efc-be51-41f7e2a0be57-1692020811373.jpg', 1);

update member set `profile_image_id` = 1 where `member_id` = 1;

-- 회원(일반회원)
insert into member (`created_at`, `email`, `password`, `nickname`, `phone_number`, `certified`, `profile_image_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'user@user.com', '$2a$10$E.2ZQcc2g2b29oORqmK.DeqeohuEvJiA6u5QH0N5n5EMCwqv2nSj.', 'user', '000-000-0000', 'CERTIFIED', 1);
insert into member (`created_at`, `oauth_type`, `oauth_id`, `nickname`, `email`, `certified`, `profile_image_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'KAKAOTALK', '$2a$10$Dld5fafSWm2acfqohlRcBub6r9KVQ6K.wyuz7u3BY0P2RmIUFxADS', 'kakaouser', 'kakaotest@kakao.com', 'CERTIFIED', 1);

-- 권한
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'ROLE_ADMIN');
insert into authority (`created_at`, `authority_name`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'ROLE_USER');

-- 회원별 권한
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 1, 1);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 1, 2);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 2, 2);
insert into member_authority (`created_at`, `member_id`, `authority_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 3, 2);

-- 업로드
-- 고양이 이미지(10종)
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat01.jpg', 'b590421e-afa0-4ebb-a6eb-88808e30dc0c-1692021140667.jpg', 354611, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/b590421e-afa0-4ebb-a6eb-88808e30dc0c-1692021140667.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat02.jpg', 'a12373a0-b89d-4c19-9f4c-b5308a4629f6-1692021141642.jpg', 500359, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/a12373a0-b89d-4c19-9f4c-b5308a4629f6-1692021141642.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat03.jpg', '617c1f71-593e-4394-be4f-86e805af3255-1692021142647.jpg', 141803, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/617c1f71-593e-4394-be4f-86e805af3255-1692021142647.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat04.jpg', '58f0c514-7853-45f0-8375-93edff526f33-1692021143317.jpg', 153945, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/58f0c514-7853-45f0-8375-93edff526f33-1692021143317.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat05.jpg', 'c149d288-810f-422c-9116-62c72d47cd99-1692021144021.jpg', 55181, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/c149d288-810f-422c-9116-62c72d47cd99-1692021144021.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat06.jpg', 'f6dec90d-a593-4f6a-bf86-0687d14e750c-1692021144476.jpg', 443815, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/f6dec90d-a593-4f6a-bf86-0687d14e750c-1692021144476.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat07.jpg', 'f25764f4-75e0-4cc5-8a1d-63d528aacdba-1692021145421.jpg', 608203, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/f25764f4-75e0-4cc5-8a1d-63d528aacdba-1692021145421.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat08.jpg', 'a6ec0f92-a4f2-45ec-bb63-06ad73323274-1692021146479.jpg', 350531, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/a6ec0f92-a4f2-45ec-bb63-06ad73323274-1692021146479.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat09.jpg', '1caebdfc-3ca3-43d0-8bba-dd7c4990b15e-1692021147194.jpg', 449416, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/1caebdfc-3ca3-43d0-8bba-dd7c4990b15e-1692021147194.jpg', 1);
insert into upload (`created_at`, `extension`, `file_category`, `origin_name`, `saved_name`, `size`, `type`, `url`, `member_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', 'jpg', 'CAT', 'cat10.jpg', '88780097-48a7-4429-91c7-ba03cc649afa-1692021148017.jpg', 226242, 'image/jpeg', 'https://storage.googleapis.com/myohanmeeting/cat/88780097-48a7-4429-91c7-ba03cc649afa-1692021148017.jpg', 1);

-- 입양공고
insert into adopt_notice (`created_at`, `cat_age`, `cat_founded_at`, `cat_founded_place`, `cat_health_status`, `cat_name`, `cat_neutered`, `cat_personality`, `cat_regist_number`, `cat_registered`, `cat_sex`, `cat_species`, `cat_weight`, `application_count`, `comment_count`, `content`, `notice_status`, `shelter_address`, `shelter_city`, `shelter_name`, `shelter_phone_number`, `title`, `member_id`, `thumbnail_id`) values (TIMESTAMP '2023-08-15 00:23:48.207129', '2', '2022-08-15', 'Street', 'Good', '미미', 'NEUTERED', 'Friendly and playful', 'ABC12345', 'REGISTERED', 'MALE', 'Domestic Shorthair', 4.5, 0, 0, '데려가줘', 'ACCEPTING', 'HappyStreet', 0, 'HappyShelter', '1234567890', '러시안블루 고양이 분양함', 1, 1);

-- 입양공고 사진
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.285545', 1, 2);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.288016', 1, 3);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.288741', 1, 4);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.289437', 1, 5);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.290261', 1, 6);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.290955', 1, 7);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.291668', 1, 8);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.292348', 1, 9);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.29308', 1, 10);
insert into cat_picture (`created_at`, `notice_id`, `upload_id`) values (TIMESTAMP '2023-08-15 00:29:15.293736', 1, 11);
