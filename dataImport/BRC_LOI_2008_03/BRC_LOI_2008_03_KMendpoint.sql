--loi9195
--BRC_LOI_2008_02 

--TIME: DISEASE_FREE_SURVIVAL_DAYS
--end point: EVENT_DFS
insert into BRC_LOI_2008_03.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DISEASE_FREE_SURVIVAL_DAYS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_DFS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_DFS' and v.term_meaning = 'EVENT') 
);

--TIME: DMFS_DAYS
--end point: EVENT_DMFS
insert into BRC_LOI_2008_03.km_attribute (KM_ATTRIBUTE_ID, CENSOR_ATTRIBUTE_ID, CENSOR_VALUE_ID) 
values ( 
(select attribute_type_id from common.attribute_type where short_name = 'DMFS_DAYS'), 
(select attribute_type_id from common.attribute_type where short_name = 'EVENT_DMFS'), 
(select attribute_vocabulary_id from common.attribute_vocabulary v, common.attribute_type t where t.attribute_type_Id = v.attribute_type_id and t.short_name = 'EVENT_DMFS' and v.term_meaning = 'EVENT') 
);