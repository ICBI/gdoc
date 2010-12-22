----------------------------------------------
--BRC_FINAK_2008_01
-- Time: time to relapse/fu in months
-- end point:: relapse
insert into BRC_FINAK_2008_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'TIME_TO_RELAPSE/FU_MONTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'RELAPSE'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'RELAPSE' and v.term_meaning = 'YES') 
); 


--not sure about this - CHECK
insert into BRC_FINAK_2008_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'SURGERY_TO_DEATH/FU'), 
(select attribute_type_id from common.attribute_type where short_name = 'VITAL_STATUS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'VITAL_STATUS' and v.term_meaning = 'DEAD') 
); 







-------------------------------------------------