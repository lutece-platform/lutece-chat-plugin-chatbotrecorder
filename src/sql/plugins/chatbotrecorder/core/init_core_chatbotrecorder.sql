
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'CHATBOTRECORDER_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('CHATBOTRECORDER_MANAGEMENT','chatbotrecorder.adminFeature.ChatBotRecorderManager.name',1,'jsp/admin/plugins/chatbotrecorder/SelectChatBot.jsp','chatbotrecorder.adminFeature.ChatBotRecorderManager.description',0,'chatbotrecorder',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'CHATBOTRECORDER_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('CHATBOTRECORDER_MANAGEMENT',1);

