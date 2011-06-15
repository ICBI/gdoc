-------------------------------------------------
--ZHANG
-- tIME: Disease free survival in months
-- END PIONT: Event indicator for Disease Free SurvivaL
insert into BRC_ZHANG_2009_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DISEASE_FREE_SURVIVAL_MON'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_DFS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_DFS' and v.term_meaning = 'EVENT') 
); 

------------------------------------------------------