--TIME: OVERALL_SURVIVAL_MONTHS
--end point: EVENT_OS
insert into PC_TAYLOR_2011_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'OVERALL_SURVIVAL_MONTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_OS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_OS' and v.term_meaning = 'EVENT') 
);

-- Time: time to relapse/fu in months
-- end point: RECURRENCE_ANY
insert into PC_TAYLOR_2011_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'TIME_TO_RELAPSE/FU_MONTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'RECURRENCE_ANY'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'RECURRENCE_ANY' and v.term_meaning = 'YES') 
);  
