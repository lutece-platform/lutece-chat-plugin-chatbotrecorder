
--
-- Structure for table chatbotrecorder_scenario
--

DROP TABLE IF EXISTS chatbotrecorder_scenario;
CREATE TABLE chatbotrecorder_scenario (
id_scenario int AUTO_INCREMENT,
name long varchar,
description long varchar,
key_chatbot long varchar,
last_run timestamp NULL,
version long varchar,
status int default '0',
PRIMARY KEY (id_scenario)
);

--
-- Structure for table chatbotrecorder_conversation
--

DROP TABLE IF EXISTS chatbotrecorder_conversation;
CREATE TABLE chatbotrecorder_conversation (
id_conversation int AUTO_INCREMENT,
user_msg long varchar,
bot_response long varchar,
id_scenario int default '0',
version long varchar,
PRIMARY KEY (id_conversation)
);

--
-- Structure for table chatbotrecorder_replay
--

DROP TABLE IF EXISTS chatbotrecorder_replay;
CREATE TABLE chatbotrecorder_replay (
id_replay int AUTO_INCREMENT,
last_run timestamp NULL,
status int default '0',
nbr_err int default '0',
id_scenario int default '0',
version long varchar,
PRIMARY KEY (id_replay)
);

--
-- Structure for table chatbotrecorder_replay_conversation
--

DROP TABLE IF EXISTS chatbotrecorder_replay_conversation;
CREATE TABLE chatbotrecorder_replay_conversation (
id_replay_conversation int AUTO_INCREMENT,
user_msg long varchar,
bot_response long varchar,
status int default '0',
id_replay int default '0',
version long varchar,
PRIMARY KEY (id_replay_conversation)
);
