--TIME: DISEASE_SPECIFIC_SURVIVAL_MON
--end point: EVENT_DSS
insert into BRC_MILLER_2005_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DISEASE_SPECIFIC_SURVIVAL_YEARS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_DSS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_DSS' and v.term_meaning = 'EVENT') 
);