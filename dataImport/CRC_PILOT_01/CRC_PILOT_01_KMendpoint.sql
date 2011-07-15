-------------------------------
--SURGERY_TO_RECUR/FU_MONTHS
--RECURRENCE_ANY
insert into CRC_PILOT_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'SURGERY_TO_RECUR/FU_MONTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'RECURRENCE_ANY'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'RECURRENCE_ANY' and v.term_meaning = 'YES')
);
-------------------------------------------