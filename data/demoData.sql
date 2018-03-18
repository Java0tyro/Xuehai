-- 密码 1234
USE demo_xuehai;

INSERT INTO user ( username, `password`,  salt, email) VALUES ("admin1", "312433c28349f63c4f387953ff337046e794bea0f9b9ebfcb08e90046ded9c76", "asdf", "admin1@admin");
INSERT INTO user ( username, `password`,  salt, email) VALUES ("admin2", "312433c28349f63c4f387953ff337046e794bea0f9b9ebfcb08e90046ded9c76", "asdf", "admin2@admin");
INSERT INTO user ( username, `password`,  salt, email) VALUES ("admin3", "312433c28349f63c4f387953ff337046e794bea0f9b9ebfcb08e90046ded9c76", "asdf", "admin3@admin");
INSERT INTO user ( username, `password`,  salt, email) VALUES ("admin4", "312433c28349f63c4f387953ff337046e794bea0f9b9ebfcb08e90046ded9c76", "asdf", "admin4@admin");
INSERT INTO user ( username, `password`,  salt, email) VALUES ("admin5", "312433c28349f63c4f387953ff337046e794bea0f9b9ebfcb08e90046ded9c76", "asdf", "admin5@admin");
INSERT INTO user ( username, `password`,  salt, email, authority) VALUES ("admin", "312433c28349f63c4f387953ff337046e794bea0f9b9ebfcb08e90046ded9c76", "asdf", "admin@admin", 1);


INSERT INTO question_type ( `name` ) VALUES ( "type1" );
INSERT INTO question_type ( `name`, parent ) VALUES ( "type2" , 1);
INSERT INTO question_type ( `name`, parent ) VALUES ( "type3" , 2);
INSERT INTO question_type ( `name` ) VALUES ( "type4" );

INSERT INTO question ( `user`, title, content, type ) VALUES ( 1, "question1", "question1", 1);
INSERT INTO question ( `user`, title, content, type ) VALUES ( 2, "question2", "question2", 2);
INSERT INTO question ( `user`, title, content, type ) VALUES ( 3, "question3", "question3", 3);
INSERT INTO question ( `user`, title, content, type ) VALUES ( 4, "question4", "question4", 1);
INSERT INTO question ( `user`, title, content, type ) VALUES ( 5, "question5", "question5", 4);
INSERT INTO question ( `user`, title, content, type ) VALUES ( 1, "question11", "question11", 3);

INSERT INTO answer ( `user`, question, content ) VALUES ( 2, 1, "answer1" );
INSERT INTO answer ( `user`, question, content ) VALUES ( 3, 1, "answer2" );
INSERT INTO answer ( `user`, question, content ) VALUES ( 4, 1, "answer3" );
INSERT INTO answer ( `user`, question, content ) VALUES ( 5, 3, "answer4" );

INSERT INTO `comment` ( `user`, answer, content ) VALUES ( 1, 1, "comment1" );
INSERT INTO `comment` ( `user`, answer, content, parent ) VALUES ( 2, 1, "comment2", 1 );
INSERT INTO `comment` ( `user`, answer, content ) VALUES ( 4, 2, "comment3" );

INSERT INTO follow ( user_from, user_to ) VALUES ( 2, 1 );
INSERT INTO follow ( user_from, user_to ) VALUES ( 3, 1 );
INSERT INTO follow ( user_from, user_to ) VALUES ( 4, 2 );
INSERT INTO follow ( user_from, user_to ) VALUES ( 1, 5 );
INSERT INTO follow ( user_from, user_to ) VALUES ( 3, 4 );
INSERT INTO follow ( user_from, user_to ) VALUES ( 2, 4 );
INSERT INTO follow ( user_from, user_to ) VALUES ( 5, 1 );

INSERT INTO collection ( `user`, question ) VALUES ( 1, 2 );
INSERT INTO collection ( `user`, question ) VALUES ( 1, 3 );
INSERT INTO collection ( `user`, question ) VALUES ( 1, 4 );
INSERT INTO collection ( `user`, question ) VALUES ( 5, 1 );

INSERT INTO `like` ( `user`, answer ) VALUES (1, 1);
INSERT INTO `like` ( `user`, answer ) VALUES (3, 1);
INSERT INTO `like` ( `user`, answer ) VALUES (5, 2);