--CRC_BROSENS_2010_01
--TIME: DISEASE_FREE_SURVIVAL_MON
--end point: EVENT_DFS
insert into CRC_BROSENS_2010_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DISEASE_FREE_SURVIVAL_MON'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_DFS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_DFS' and v.term_meaning = 'EVENT') 
);

--TIME: OVERALL_SURVIVAL_MONTHS
--end point: EVENT_OS
insert into CRC_BROSENS_2010_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'OVERALL_SURVIVAL_MONTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_OS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_OS' and v.term_meaning = 'EVENT') 
);
