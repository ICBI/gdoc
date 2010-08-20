-----------------------------------------------------------

--ZHOU
--TIME: DISEASE_FREE_SURVIVAL_YEARS
--end point: RELAPSE
insert into zhou.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DISEASE_FREE_SURVIVAL_YEARS'), 
(select attribute_type_id from common.attribute_type where short_name = 'RELAPSE'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'RELAPSE' and v.term_meaning = 'YES') 
);

------------------------------------------------------------