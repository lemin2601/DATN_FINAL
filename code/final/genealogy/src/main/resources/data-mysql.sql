INSERT INTO `genealogy`.`role` (`role_id`, `role`) VALUES ('1', 'ADMIN');
INSERT INTO `genealogy`.`role` (`role_id`, `role`) VALUES ('2', 'USER');
INSERT INTO `genealogy`.`role` (`role_id`, `role`) VALUES ('3', 'USER_FB');
INSERT INTO `genealogy`.`user` (`user_id`, `active`, `email`, `last_name`, `name`, `password`) VALUES ('1', '1', 'admin@admin.com.vn', 'Admin', 'Admin', '$2a$10$b6ykZ8Nbv.CF/MfEiKHHX./trXxhtIOQnYugPnjxBvVTd/Sytigc.');
INSERT INTO `genealogy`.`user_role` (`role_id`, `user_id`) VALUES ('1', '1');


