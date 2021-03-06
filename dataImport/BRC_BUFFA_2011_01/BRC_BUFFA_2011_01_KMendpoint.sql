--Study name BRC_BUFFA_2011_01
--TIME: DISEASE_FREE_SURVIVAL_YEARS
--end point: EVENT_DMFS
insert into BRC_BUFFA_2011_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DMFS_YEARS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_DMFS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_DMFS' and v.term_meaning = 'EVENT') 
);
