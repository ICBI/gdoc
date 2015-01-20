--CA_ARRAY_LUNG
--TIME: MONTHS_TO_FIRST_PROGRESSION
--end point: FIRST_PROGRESSION_OR_RELAPSE
insert into CA_ARRAY_LUNG.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'MONTHS_TO_FIRST_PROGRESSION'), 
(select attribute_type_id from common.attribute_type where short_name = 'FIRST_PROGRESSION_OR_RELAPSE'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'FIRST_PROGRESSION_OR_RELAPSE' and v.term_meaning = 'YES') 
);

--TIME: OVERALL_SURVIVAL_MNTHS
--end point: VITAL_STATUS
insert into CA_ARRAY_LUNG.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'OVERALL_SURVIVAL_MNTHS'), 
(select attribute_type_id from common.attribute_type where short_name = 'VITAL_STATUS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'VITAL_STATUS' and v.term_meaning = 'DEAD') 
);