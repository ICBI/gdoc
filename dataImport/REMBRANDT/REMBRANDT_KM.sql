--TIME: OVERALL_SURVIVAL_MNTHS
--end point: EVENT_OS
insert into REMBRANDT.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'OVERALL_SURVIVAL_MONTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_OS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_OS' and v.term_meaning = 'EVENT') 
);