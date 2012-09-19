---####   BRC_BUFFA #### 
select * from COMMON.attribute_type where Semantic_Group='Outcome'

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'ER_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'EVENT_DMFS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DMFS_YEARS';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'NODES_INVOLVED' ;

--- BRC CLARKE 9999 02
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'PGR_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'PARENT_CELL_LINE' ;
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'TAMOXIFEN_SENSITIVITY' ;
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'FASLODEX_SENSITIVITY' ;

update  COMMON.attribute_type set Semantic_Group='Experiment details' where short_name like 'DRUG_17BETA_ESTRADIOL' ;
update  COMMON.attribute_type set Semantic_Group='Experiment details' where short_name like 'MEDIA' ;
update  COMMON.attribute_type set Semantic_Group='Biospecimen details' where short_name like 'BIOSPECIMEN_TYPE' ;
update  COMMON.attribute_type set Semantic_Group='Metabolomics related details' where short_name like 'IONIZATION' ;

---BRC clarke - liu

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'SURGERY_TO_DR/FU';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'SURGERY_TO_LR/FU';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'SURGERY_TO_RR/FU';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DR';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'RR';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'LR';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'RELAPSE_CODE';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'BREASTCANCER_DEATH';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'DR';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'RR';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'LR';

----#### desmedt 2007
update  COMMON.attribute_type set Semantic_Group='Surgery details' where short_name =  'BREAST_CONSERVING_SURGERY';
update  COMMON.attribute_type set Semantic_Group='Surgery details' where short_name =  'MASTECTOMY';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DISEASE_FREE_SURVIVAL_DAYS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'EVENT_DFS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'OVERALL_SURVIVAL_DAYS';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'TDM_DAYS'; -- not in quick start
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'EVENT_TDM';

update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'NPI_SCORE'; -- not in quick start
update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'RISK_NPI'; -- not in quick start

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'EVENT_DFS';


----###Desmedt 2009
update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'GGI'; -- not in quick start
update  COMMON.attribute_type set Semantic_Group='Miscellaneous' where short_name =  'CLUSTER_ID'; -- not in quick start

update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'POSTMENOPAUSAL_STATUS'; -- not in quick start
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'ADJ_OR_NEOADJ_CT'; -- not in quick start

update  COMMON.attribute_type set Semantic_Group='Radiation details' where short_name =  'RADIOTHERAPY';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'LETROZOL_TREATMENT';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'TAMOXIFEN_TREATMENT';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DISEASE_FREE_SURVIVAL_MON';

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'HER2_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name like 'NODAL_STATUS' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'NODAL_STATUS' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'TAMOXIFEN_TREATMENT';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'HER2_STATUS';


---- Enerly
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name = 'SAMPLE_TYPE' ;
update  COMMON.attribute_type set Semantic_Group='Cancer details' where short_name = 'BC_MOLECULAR_SUBTYPE' ;

update  COMMON.attribute_type set Semantic_Group='Mutation details' where short_name = 'TP53_MUTATION' ;

---- Finak

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'ERBB2_IHC_STATUS' ;

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'TIME_TO_RELAPSE/FU_MONTHS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'SURGERY_TO_DEATH/FU';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'RELAPSE';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'CHEMOTHERAPY';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'HORMONE_THERAPY';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'RELAPSE';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'CHEMOTHERAPY';


-------Finetti
update  COMMON.attribute_type set Semantic_Group='Cancer details' where short_name like 'CANCER_TYPE' ;

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'PR_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'P53_IHC_STATUS' ;

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'P53_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'KI67_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'ERBB2_HT_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'ERBB2_TAB_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'ERBB2P_IHC_STATUS' ;

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'IGF1R_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'EGFR_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'TOP2A_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'FOXA1_IHC_STATUS' ;

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DMFS_MONTHS';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'PR_IHC_STATUS';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'P53_IHC_STATUS';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'EGFR_IHC_STATUS' ;


---Becca's study - cell lline

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'BPA_TREATMENT'; -- used in outcome file

--------------Lin -- cell line
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DOSAGE'; 


-----------Loi 01 ----

update  COMMON.attribute_type set Semantic_Group='Biospecimen details' where short_name like 'CHIP_TYPE' ;

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DISEASE_FREE_SURVIVAL_DAYS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DMFS_DAYS';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'TAMOXIFEN_TREATMENT'; -- used in outcome file

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'TAMOXIFEN_TREATMENT' ;


---------------Miller

update  COMMON.attribute_type set Semantic_Group='Classifiers' where short_name =  'DLDA_CLASSIFIER_RESULT_P53_STATUS';
update  COMMON.attribute_type set Semantic_Group='Classifiers' where short_name =  'DLDA_CLASSIFIER_ERROR';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DISEASE_SPECIFIC_SURVIVAL_YEARS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'EVENT_DSS';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'EVENT_DSS' ;

-----------------PAwitan
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'OVERALL_SURVIVAL_YEARS';

-------------Popovici
update  COMMON.attribute_type set Semantic_Group='Demographics' where short_name =  'RACE';

update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'BMN_GRADE';

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name =  'ER_LEVEL';
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name =  'HER2_IHC';
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name =  'HER2_FISH';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'TREATMENT';
update  COMMON.attribute_type set Semantic_Group='Clinical response' where short_name =  'POST_TRE_RESPONSE';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'ER_LEVEL' ;


----wang

update  COMMON.attribute_type set Semantic_Group='Relapse details' where short_name =  'BRAIN_RELAPSE';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'BRAIN_RELAPSE' ;


--- zhang
update  COMMON.attribute_type set Semantic_Group='Metastasis details' where short_name =  'METASTASIS_WITHIN_5YRS';

-----colon cancer studies 
-- CRC_BROSENS
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'EVENT_OS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'OVERALL_SURVIVAL_MONTHS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DISEASE_FREE_SURVIVAL_MON';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'CANCER_DEATH';
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name =  'MSI_STATUS';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'EVENT_OS' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'MSI_STATUS' ;


--CRC_GALAMB
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'DUKES_CLASS' ;
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'AJCC_TUMOR_GRADE' ;

update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name like 'DYSPLASIA' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'DYSPLASIA' ;


-- CRC_K_FORD

update  COMMON.attribute_type set Semantic_Group='Biopsy details' where short_name like 'BIOPSY_TISSUE' ;

update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name like 'BEST_RESPONSE' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'BEST_RESPONSE' ;


update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'PROGRESSION_FREE_SURVIVAL_DAYS';

update  COMMON.attribute_type set Semantic_Group='Mutation details' where short_name = 'KRAS_MUTATION' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'KRAS_MUTATION' ;

update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'AREG_AFFYQ_EXPR' ;
update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'EREG_AFFYQ_EXPR' ;
update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'AREG_QRTPCR' ;
update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'EREG_QRTPCR' ;
update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'AMPHIREGULIN_ELISA' ;
update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'EPIREGULIN_ ELISA' ;
update  COMMON.attribute_type set Semantic_Group='Transcriptional profiling' where short_name = 'EGFR_QPCR' ;

---####   CRC_PILOT_05 #### 
update  COMMON.attribute_type set Semantic_Group='Addictive Drugs' where short_name like '%CIGARETTES%' ;
update  COMMON.attribute_type set Semantic_Group='Addictive Drugs' where short_name like 'WINE' ;
update  COMMON.attribute_type set Semantic_Group='Addictive Drugs' where short_name like 'LIQUOR' ;
update  COMMON.attribute_type set Semantic_Group='Addictive Drugs' where short_name like 'BEER' ;
update  COMMON.attribute_type set Semantic_Group='Addictive Drugs' where short_name like 'PIPE' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'CIGARETTES' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'WINE' ;

update  COMMON.attribute_type set Semantic_Group='Current Diseases' where short_name = '%PRIMARY_DISEASE%' ;

update  COMMON.attribute_type set Semantic_Group='Neoadjuvant Therapy(ies)' where short_name = 'NEOADJ_THERAPY' ;
update  COMMON.attribute_type set Semantic_Group='Neoadjuvant Therapy(ies)' where short_name = 'NEOADJ_CT' ;
update  COMMON.attribute_type set Semantic_Group='Neoadjuvant Therapy(ies)' where short_name = 'NEOADJ_RADIATION' ;
update  COMMON.attribute_type set Semantic_Group='Neoadjuvant Therapy(ies)' where short_name = 'NEOADJ_HORMONE_TH' ;

update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'DISEASE_LOCALIZATION' ;
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'TUMOR_SIZE' ;
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name in ( 'HIST_TUMOR_GRADE', 'HIST_TUMOR_TYPE', 'PTNM_T', 'PTNM_N',
'PTNM_N_POSITIVE','PTNM_N_TOTAL','PTNM_M', 'PTNM_L', 'PTNM_V', 'TUMOR_STAGE', 'RADICALITY', 'ELSTON-ELLIS_GRADE', 'TUMOR_DIGNITY');

update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name like 'TUMOR_SIZE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name like 'HIST_TUMOR_TYPE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'HIST_TUMOR_GRADE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'TUMOR_STAGE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name like 'RADICALITY' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name like 'ELSTON-ELLIS_GRADE' ;

update  COMMON.attribute_type set Semantic_Group='Metastasis details' where short_name =  'METASTASIS_LOCATION';
update  COMMON.attribute_type set Semantic_Group='Radiation details' where short_name =  'RADIATION';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'SURGERY_TO_RECUR/FU_MONTHS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'RECURRENCE_ANY';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'VITAL_STATUS';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'RECURRENCE_ANY' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'VITAL_STATUS' ;

update  COMMON.attribute_type set Semantic_Group='Family history' where short_name =  'RELATIONSHIP';
update  COMMON.attribute_type set Semantic_Group='Family history' where short_name =  'VITAL_STATUS_RELATIVE';
update  COMMON.attribute_type set Semantic_Group='Family history' where short_name =  'DISEASE_CODE_RELATIVE';

update  COMMON.attribute_type set Semantic_Group='Previous cancer' where short_name =  'DISEASE_CODE_PREV_CANCER';
update  COMMON.attribute_type set Semantic_Group='Previous cancer' where short_name =  'PREV_CANCER_REMISSION';
update  COMMON.attribute_type set Semantic_Group='Previous cancer' where short_name =  'PREV_CANCER_TREATMENT';

update  COMMON.attribute_type set Semantic_Group='Followup chemotherapy' where short_name =  'FUP_CT_AGENT';
update  COMMON.attribute_type set Semantic_Group='Followup chemotherapy' where short_name =  'MONTHS_AFTER_SURGERY';

update  COMMON.attribute_type set Semantic_Group='Demographics' where short_name =  'BMI';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'BMI' ;

update COMMON.attribute_type set Semantic_Group='Clinical Chemistry' where short_name in (
'PTT',
'QUICKS_TEST',
'SODIUM',
'POTTASIUM',
'CARBAMIDE',
'CREATININE',
'GOT',
'GPT',
'GGT',
'CHE',
'ALKALINE_PHOSPHATE',
'BILIRUBIN',
'CALCIUM',
'CRP',
'LDH',
'GLUCOSE',
'CHOLESTEROL',
'TRIGLYCERIDES',
'URIC_ACID',
'HDL_CHOLESTEROL',
'LDL_CHOLESTEROL');

update COMMON.attribute_type set Semantic_Group='Serology' where short_name in (
'CEA','CA_19_9' );

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'CREATININE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'TRIGLYCERIDES' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'CREATININE' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name like 'HDL_CHOLESTEROL' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name like 'LDL_CHOLESTEROL' ;

------CRC_SABATES

update  COMMON.attribute_type set Semantic_Group='Family history' where short_name =  'RELATIVE_ONSET_AGE';
update  COMMON.attribute_type set Semantic_Group='Family history' where short_name =  'RELATIONSHIP';

update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name =  'SAMPLE_SIZE';
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name =  'SAMPLE_LOCATION';
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name =  'SAMPLE_TYPE';

update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'NUM_HYPERPLASTIC_POLYPS';
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'PREV_EXCISED_TUMORS';
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'COLONOSCOPY_ADENOMAS';
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name = 'HIGHEST_DEG_DYSPLASIA' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'HIGHEST_DEG_DYSPLASIA' ;


-- CRC_SARVER
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'MMR_STATUS' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'MMR_STATUS' ;


---CRC_SELGA _01
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'METHOTREXATE_SENSITIVITY' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'METHOTREXATE_SENSITIVITY' ;


---CRC_STAUB
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'NODES_REMOVED' ;
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'NODES_INVADED' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'NODES_INVADED' ;

-- EDINBURGH

update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'TUMOR_SIZE_PRE_TRE_ULTRA_DIM1' ;
update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'TUMOR_SIZE_PRE_TRE_ULTRA_DIM2' ;
update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'TUMOR_SIZE_PRE_TRE_CLINICAL_DIM1' ;
update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'TUMOR_SIZE_PRE_TRE_CLINICAL_DIM2' ;
update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'ER_STATUS_PRE_TRE' ;
update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'TUMOR_GRADE_PRE_TRE' ;

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'TAMOXIFEN_TREATMENT' ;
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'ARIMIDEX_TREATMENT' ;
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'COMBINATION_TREATMENT' ;
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'EXEMESTANE_TREATMENT' ;

update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name = 'POST_TRE_RESPONSE' ;

update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'NODES_INVOLVED';
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'TOTAL_NODES_TESTED';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'NODES_INVOLVED' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'TAMOXIFEN_TREATMENT' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'ARIMIDEX_TREATMENT' ;


--REYMANN

update  COMMON.attribute_type set Semantic_Group='Resection details' where short_name =  'RESECTION';
update  COMMON.attribute_type set Semantic_Group='Resection details' where short_name =  'REASON_RESECTION';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'RESECTION' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'REASON_RESECTION' ;


update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'DRUG_DOXORUBICIN' ;
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name = 'DRUG_TROVAFLOXACIN' ;

---HCC_toffanin

update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'VASCULAR_INVASION';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name = 'BCLC_STAGE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name = 'BCLC_STAGE' ;

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'LIVER_TRANSPLANT';
update  COMMON.attribute_type set Semantic_Group='Cancer details' where short_name = 'HCC_SUBCLASS' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name = 'HCC_SUBCLASS' ;


update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name =  'AFP_LEVEL';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name = 'AFP_LEVEL' ;


update  COMMON.attribute_type set Semantic_Group='Mutation details' where short_name = 'CTNNB1_MUTATION' ;

update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'RPS6_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'IGF1R_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'AKT_IHC_STATUS' ;
update  COMMON.attribute_type set Semantic_Group='Biomarker tests' where short_name like 'CTNNB1_IHC_STATUS' ;

--hcc wurmbach
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'SATELLITE_TUMOR';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name = 'SATELLITE_TUMOR' ;

----OV-TCGA

-----PC-Taylor

update  COMMON.attribute_type set Semantic_Group='Biopsy details' where short_name like 'GLEASON_SCORE_BIOPSY' ;
update  COMMON.attribute_type set Semantic_Group='Biopsy details' where short_name like 'PRIMARY_GLEASON_GRADE_BIOPSY' ;
update  COMMON.attribute_type set Semantic_Group='Biopsy details' where short_name like 'SECONDARY_GLEASON_GRADE_BIOPSY' ;

update COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name = 'GLEASON_SCORE_BIOPSY'
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'PRIMARY_GLEASON_GRADE_BIOPSY' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'SECONDARY_GLEASON_GRADE_BIOPSY' ;

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'RECURRENCE_ANY' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'EVENT_OS' ;

update  COMMON.attribute_type set Semantic_Group='Pre-treatment details' where short_name = 'PSA_LEVEL_PRE_TREATMENT' ;

update  COMMON.attribute_type set Semantic_Group='Pathological specimen details' where short_name = 'PRIMARY_GLEASON_GRADE_PATH' ;
update  COMMON.attribute_type set Semantic_Group='Pathological specimen details' where short_name = 'SECONDARY_GLEASON_GRADE_PATH' ;
update  COMMON.attribute_type set Semantic_Group='Pathological specimen details' where short_name = 'GLEASON_SCORE_PATH' ;

update  COMMON.attribute_type set Semantic_Group='Demographics' where short_name =  'ETHNICITY';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'NEOADJ_RADIATION_THERAPY_TYPE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'CHEMOTHERAPY_TYPE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'HORMONE_THERAPY_TYPE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADICAL_PROSTATECTOMY_TYPE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADIATION_TYPE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'DRUG';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'SURGICAL_MARGIN_STATUS';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'RADICAL_PROSTATECTOMY_TYPE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name =  'NEOADJ_RADIATION_THERAPY_TYPE';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'CHEMOTHERAPY_TYPE';
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name =  'HORMONE_THERAPY_TYPE';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'RADIATION_TYPE';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'DEATH_CAUSE';

update  COMMON.attribute_type set Semantic_Group='Metastasis details' where short_name =  'METASTASIS';

update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'PSA_LEVEL_DIAGNOSIS';
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'SEMINAL_VESICAL_INVASION_STATUS';

update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'EXTRA_CAPSULAR_EXTENSION';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'CTNM_T_PROSTATE';

update  COMMON.attribute_type set Semantic_Group='Probability' where short_name =  'PROB_PFP_POSTRP';
update  COMMON.attribute_type set Semantic_Group='Probability' where short_name =  'PROB_ECE';
update  COMMON.attribute_type set Semantic_Group='Probability' where short_name =  'PROB_SVI';
update  COMMON.attribute_type set Semantic_Group='Probability' where short_name =  'PROB_OCD';
update  COMMON.attribute_type set Semantic_Group='Probability' where short_name =  'PROB_PTNM_N';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'AGE' ;
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name like 'RACE' ;

----sc -ooi
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'LAUREN_CLASS';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'HIST_TUMOR_GRADE';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'MING_CLASS';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'WHO_CLASS';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'AJCC_TUMOR_GRADE';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'AJCC_TUMOR_GRADE';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'HIST_TUMOR_GRADE';

----OV TCGA
update  COMMON.attribute_type set Semantic_Group='Additional Treatment' where short_name =  'ADDITIONAL_CHEMO_THERAPY';
update  COMMON.attribute_type set Semantic_Group='Additional Treatment' where short_name =  'ADDITIONAL_DRUG_THERAPY';
update  COMMON.attribute_type set Semantic_Group='Additional Treatment' where short_name =  'ADDITIONAL_IMMUNO_THERAPY';
update  COMMON.attribute_type set Semantic_Group='Additional Treatment' where short_name =  'ADDITIONAL_PHARMA_THERAPY';
update  COMMON.attribute_type set Semantic_Group='Additional Treatment' where short_name =  'ADDITIONAL_RADIATION_THERAPY';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'CHEMOTHERAPY';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'HORMONE_THERAPY';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'IMMUNO_THERAPY';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'OVERALL_SURVIVAL_DAYS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'PROGRESSION_FREE_SURVIVAL_DAYS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'PROGRESSION';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'PROGRESSION';

update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'PLATINUM_FREE_INTERVAL_MONTHS';
update  COMMON.attribute_type set Semantic_Group='Outcome' where short_name =  'PLATINUM_STATUS';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'PLATINUM_STATUS';

update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'PROGRESSION_DIAGNOSIS_METHOD';
update  COMMON.attribute_type set Semantic_Group='Screening and diagnostics' where short_name =  'INITIAL_PATHOLOGIC_DIAGNOSIS_METHOD';

update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'TUMOR_STATUS';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'TUMOR_STAGE_FIGO';
update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'SITE_OF_FIRST_TUMOR_RECURRENCE';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'TUMOR_STATUS';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'TUMOR_STAGE_FIGO';

update  COMMON.attribute_type set Semantic_Group='Resection details' where short_name =  'RESIDUAL_TUMOR';
update  COMMON.attribute_type set Semantic_Group='Resection details' where short_name =  'SIZE_RESIDUAL_TUMOR';

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'RESIDUAL_TUMOR';

update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'DAYS_TO_DRUG_THERAPY_END';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'DAYS_TO_DRUG_THERAPY_START';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'TREATMENT_REGIMEN_REASON';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADIATION_NUM_FRACTIONS';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADIATION_DOSAGE_CGY';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADIATION_TYPE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADIATION_TX_INTENT';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'RADIATION_SITE';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'DAYS_TO_RADIATION_TX_END';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'DAYS_TO_RADIATION_TX_START';
update  COMMON.attribute_type set Semantic_Group='Treatment details' where short_name =  'TARGETED_MOLECULAR_THERAPY';

update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name =  'DAYS_TO_DRUG_THERAPY_END';
update  COMMON.attribute_type set GDOC_PREFERRED= 2 where short_name =  'DAYS_TO_DRUG_THERAPY_START';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'RADIATION_SITE';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'TREATMENT_REGIMEN_REASON';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'TARGETED_MOLECULAR_THERAPY';

update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DRUG'; 
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DRUG_CATEGORY'; 

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'DRUG_CATEGORY';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'DRUG';

update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DRUG_COURSE_COUNT'; 
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DRUG_THERAPY_ADMIN_ROUTE'; 
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DOSAGE_MG'; 
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DOSAGE_MG/DAY'; 
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DOSAGE_MG/M2'; 
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name =  'DOSAGE_MG/KG'; 

update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'PERFORMANCE_STATUS_SCORE_ECOG'; 
update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'PERFORMANCE_STATUS_SCORE_KARNOFSKY'; 
update  COMMON.attribute_type set Semantic_Group='Prognosis' where short_name =  'PERFORMANCE_STATUS_SCORE_TIMEPOINT'; 

update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'PERFORMANCE_STATUS_SCORE_ECOG';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'PERFORMANCE_STATUS_SCORE_KARNOFSKY';
update  COMMON.attribute_type set GDOC_PREFERRED= 1 where short_name =  'PERFORMANCE_STATUS_SCORE_TIMEPOINT';


update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'SAMPLE_SIZE_INTERMEDIATE_DIMN' ;
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'SAMPLE_SIZE_LONGEST_DIMN' ;
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'SAMPLE_SIZE_SHORTEST_DIMN' ;
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'SAMPLE_TYPE' ;

update  COMMON.attribute_type set Semantic_Group='Tumor details' where short_name =  'TUMOR_GRADE';
update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'CASE_OR_CONTROL' ;

update  COMMON.attribute_type set Semantic_Group='Sample details' where short_name like 'TIMEPOINT' ;
update  COMMON.attribute_type set Semantic_Group='Drug details' where short_name like 'DOSAGE_AUC' ;


----#####SPLIT_ATTRIBUTE

update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name like 'RELAPSE_CODE' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'DR' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name like 'RR' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name like 'LR' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=3 where short_name like 'BREASTCANCER_DEATH' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'VITAL_STATUS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'RELAPSE' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'EVENT_DFS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name like 'EVENT_OS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name like 'EVENT_DMFS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=3 where short_name like 'EVENT_TDM' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'EVENT_DSS' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE=3 where short_name like 'CANCER_DEATH' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'PLATINUM_STATUS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=3 where short_name like 'DEATH_CAUSE' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'RECURRENCE_ANY' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'PROGRESSION' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name = 'TAMOXIFEN_TREATMENT' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=3 where short_name like 'FASLODEX_SENSITIVITY' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name like 'DRUG_17BETA_ESTRADIOL' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE=1 where short_name =  'BPA_TREATMENT'; -- used in outcome file

update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 2 where short_name like 'NODAL_STATUS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 2 where short_name like 'ELSTON-ELLIS_GRADE' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 3 where short_name = 'POST_TRE_RESPONSE' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 3 where short_name = 'DUKES_CLASS' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 3 where short_name = 'AJCC_TUMOR_GRADE' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 1 where short_name like 'SAMPLE_TYPE' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 2 where short_name like 'METHOTREXATE_SENSITIVITY' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 1 where short_name like 'CASE_OR_CONTROL' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 2 where short_name like 'GENDER' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE=2 where short_name = 'BCLC_STAGE' ;
update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 1 where short_name = 'Gestational_Age_category' ;

update  COMMON.attribute_type set SPLIT_ATTRIBUTE= 1 where short_name like 'BEST_RESPONSE' ;









