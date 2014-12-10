--- DATABASE ALTERATIONS ON DEV, STAGE, PROD
---*********************************************

-- 2014 done in stage
ALTER TABLE 
   COMMON.DATA_SOURCE 
ADD 
   (
      is_precision_medicine CHAR(1) NULL,
      is_translational_research CHAR(1) NULL,
      is_population_genetics CHAR(1) NULL
   );

alter table COMMON.attribute_vocabulary modify (
TERM    VARCHAR2(2000 BYTE)
);
alter table COMMON.attribute_vocabulary modify (
TERM_MEANING   VARCHAR2(2000 BYTE)
);

alter table COMMON.HTARRAY_REPORTER modify (
GENE_SYMBOL VARCHAR2(25 BYTE)
);

-- Dec 10, 2014. Increasing size of DATA_SOURCE primary key column from 3 numbers to 6 numbers. Done on dev, stage, prod
ALTER TABLE COMMON.DATA_SOURCE MODIFY DATA_SOURCE_ID NUMBER(6,0);
ALTER TABLE COMMON.DATA_SOURCE_CONTENT MODIFY DATA_SOURCE_ID NUMBER(6,0);
ALTER TABLE COMMON.DATA_SOURCE_CONTACT MODIFY DATA_SOURCE_ID NUMBER(6,0);
ALTER TABLE COMMON.ID_MAPPING MODIFY DATA_SOURCE_ID NUMBER(6,0);
