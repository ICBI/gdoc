--TIME: OVERALL_SURVIVAL_MNTHS
--end point: EVENT_OS
insert into REMBRANDT_02.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'OVERALL_SURVIVAL_DAYS'), 
(select attribute_type_id from common.attribute_type where short_name = 'CENSORING_STATUS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'CENSORING_STATUS' and v.term_meaning = 'Censoring Status 1') 
);