--TIME: OVERALL_SURVIVAL_DAYS
--end point: VITAL_STATUS
insert into OV_TCGA_2011_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'OVERALL_SURVIVAL_DAYS'), 
(select attribute_type_id from common.attribute_type where short_name = 'VITAL_STATUS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'VITAL_STATUS' and v.term_meaning = 'DEAD') 
);


--TIME: PFS
--end point: PRogression=yes
insert into OV_TCGA_2011_01.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'PROGRESSION_FREE_SURVIVAL_DAYS'), 
(select attribute_type_id from common.attribute_type where short_name = 'PROGRESSION'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'PROGRESSION' and v.term_meaning = 'YES') 
);
