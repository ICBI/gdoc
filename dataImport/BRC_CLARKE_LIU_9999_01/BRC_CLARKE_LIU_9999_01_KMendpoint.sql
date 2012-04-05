
insert into BRC_CLARKE_LIU_9999_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'SURGERY_TO_DEATH/FU'), 
(select attribute_type_id from common.attribute_type where short_name = 'VITAL_STATUS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'VITAL_STATUS' and v.term_meaning = 'DEAD') 
); 

insert into BRC_CLARKE_LIU_9999_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'SURGERY_TO_RR/FU'), 
(select attribute_type_id from common.attribute_type where short_name = 'RR'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'RR' and v.term_meaning = 'YES') 
);

insert into BRC_CLARKE_LIU_9999_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'SURGERY_TO_DR/FU'), 
(select attribute_type_id from common.attribute_type where short_name = 'DR'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'DR' and v.term_meaning = 'YES') 
);

insert into BRC_CLARKE_LIU_9999_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'SURGERY_TO_LR/FU'), 
(select attribute_type_id from common.attribute_type where short_name = 'LR'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'LR' and v.term_meaning = 'YES') 
);