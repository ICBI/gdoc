GRANT DELETE, UPDATE ON ATTRIBUTE_STATS TO EDIT_${projectName};
GRANT DELETE, UPDATE ON ATTRIBUTE_TIMEPOINT TO EDIT_${projectName};
GRANT DELETE, UPDATE ON BIOSPECIMEN TO EDIT_${projectName};
GRANT DELETE, UPDATE ON BIOSPECIMEN_ATTRIBUTE_VALUE TO EDIT_${projectName};
GRANT SELECT ON BIOSPECIMEN_VALUE_SEQUENCE TO EDIT_${projectName};
GRANT DELETE, UPDATE ON HT_COLUMN_LINK TO EDIT_${projectName};
GRANT DELETE, UPDATE ON HT_FILE TO EDIT_${projectName};
GRANT DELETE, UPDATE ON HT_FILE_COLUMN TO EDIT_${projectName};
GRANT DELETE, UPDATE ON HT_FILE_PRIOR TO EDIT_${projectName};
GRANT DELETE, UPDATE ON HT_RUN TO EDIT_${projectName};
GRANT DELETE, UPDATE ON HT_RUN_BIOSPECIMEN TO EDIT_${projectName};
GRANT DELETE, UPDATE ON KM_ATTRIBUTE TO EDIT_${projectName};
GRANT SELECT ON MARRAY_FILE_COLUMN_SEQUENCE TO EDIT_${projectName};
GRANT SELECT ON MARRAY_FILE_ROW_SEQUENCE TO EDIT_${projectName};
GRANT DELETE, UPDATE ON MS_PEAK TO EDIT_${projectName};
GRANT DELETE, UPDATE ON MS_PEAK_EVIDENCE_LINK TO EDIT_${projectName};
GRANT DELETE, UPDATE ON MS_PEAK_GROUP_LINK TO EDIT_${projectName};
GRANT DELETE, UPDATE ON ORIGINAL_ATTRIBUTE TO EDIT_${projectName};
GRANT DELETE, UPDATE ON PATIENT TO EDIT_${projectName};
GRANT DELETE, UPDATE ON PATIENT_ATTRIBUTE_VALUE TO EDIT_${projectName};
GRANT SELECT ON PATIENT_ATTRIB_VAL_SEQUENCE TO EDIT_${projectName};
GRANT SELECT ON PAV_GROUP_SEQUENCE TO EDIT_${projectName};
GRANT INSERT ON ATTRIBUTE_STATS TO INSERT_${projectName};
GRANT INSERT ON ATTRIBUTE_TIMEPOINT TO INSERT_${projectName};
GRANT INSERT ON BIOSPECIMEN TO INSERT_${projectName};
GRANT INSERT ON BIOSPECIMEN_ATTRIBUTE_VALUE TO INSERT_${projectName};
GRANT SELECT ON BIOSPECIMEN_VALUE_SEQUENCE TO INSERT_${projectName};
GRANT INSERT ON HT_COLUMN_LINK TO INSERT_${projectName};
GRANT INSERT ON HT_FILE TO INSERT_${projectName};
GRANT INSERT ON HT_FILE_COLUMN TO INSERT_${projectName};
GRANT INSERT ON HT_FILE_PRIOR TO INSERT_${projectName};
GRANT INSERT ON HT_RUN TO INSERT_${projectName};
GRANT INSERT ON HT_RUN_BIOSPECIMEN TO INSERT_${projectName};
GRANT INSERT ON KM_ATTRIBUTE TO INSERT_${projectName};
GRANT SELECT ON MARRAY_FILE_COLUMN_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON MARRAY_FILE_ROW_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON HT_RUN_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON HT_RUN_BIOSPECIMEN_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON BIOSPECIMEN_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON HT_FILE_SEQUENCE TO INSERT_${projectName};
GRANT INSERT ON MS_PEAK TO INSERT_${projectName};
GRANT INSERT ON MS_PEAK_EVIDENCE_LINK TO INSERT_${projectName};
GRANT INSERT ON MS_PEAK_GROUP_LINK TO INSERT_${projectName};
GRANT INSERT ON ORIGINAL_ATTRIBUTE TO INSERT_${projectName};
GRANT INSERT ON PATIENT TO INSERT_${projectName};
GRANT INSERT ON PATIENT_ATTRIBUTE_VALUE TO INSERT_${projectName};
GRANT SELECT ON PATIENT_ATTRIB_VAL_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON PAV_GROUP_SEQUENCE TO INSERT_${projectName};
GRANT SELECT ON ATTRIBUTE_STATS TO READ_${projectName};
GRANT SELECT ON ATTRIBUTE_TIMEPOINT TO READ_${projectName};
GRANT SELECT ON BIOSPECIMEN TO READ_${projectName};
GRANT SELECT ON BIOSPECIMEN_ATTRIBUTE_VALUE TO READ_${projectName};
GRANT SELECT ON HT_COLUMN_LINK TO READ_${projectName};
GRANT SELECT ON HT_FILE TO READ_${projectName};
GRANT SELECT ON HT_FILE_COLUMN TO READ_${projectName};
GRANT SELECT ON HT_FILE_CONTENTS TO READ_${projectName};
GRANT SELECT ON HT_FILE_PRIOR TO READ_${projectName};
GRANT SELECT ON HT_RUN TO READ_${projectName};
GRANT SELECT ON HT_RUN_BIOSPECIMEN TO READ_${projectName};
GRANT SELECT ON KM_ATTRIBUTE TO READ_${projectName};
GRANT SELECT ON KM_ATTRIBUTES TO READ_${projectName};
GRANT SELECT ON MS_PEAK TO READ_${projectName};
GRANT SELECT ON MS_PEAK_EVIDENCE_LINK TO READ_${projectName};
GRANT SELECT ON MS_PEAK_GROUP_LINK TO READ_${projectName};
GRANT SELECT ON ORIGINAL_ATTRIBUTE TO READ_${projectName};
GRANT SELECT ON PATIENT TO READ_${projectName};
GRANT SELECT ON PATIENTS TO READ_${projectName};
GRANT SELECT ON PATIENT_ATTRIBUTE_VALUE TO READ_${projectName};
GRANT SELECT ON USED_ATTRIBUTES TO READ_${projectName};

GRANT SELECT ON location_value TO READ_${projectName};
GRANT INSERT ON location_value TO INSERT_${projectName};
GRANT UPDATE ON location_value TO EDIT_${projectName};
GRANT DELETE ON location_value TO EDIT_${projectName};

GRANT SELECT ON reduction_analysis TO READ_${projectName};
GRANT INSERT ON reduction_analysis TO INSERT_${projectName};
GRANT UPDATE ON reduction_analysis TO EDIT_${projectName};
GRANT DELETE ON reduction_analysis TO EDIT_${projectName};

GRANT INSERT ON COMMON.patient_data_source to insert_${projectName};

GRANT SELECT ON COMMON.ATTRIBUTE_VOCAB_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.ATTRIBUTE_TYPE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.ATTRIBUTE_TIMEPOINT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.PATIENT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.DATA_SOURCE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.CONTACT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.HTARRAY_DESIGN_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.HTARRAY_REPORTER_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.BIOSPECIMEN_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.DATA_FILE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.PROTOCOL_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.FILE_TYPE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.FILE_FORMAT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.DSC_SEQUENCE TO insert_${projectName};

GRANT SELECT ON ${projectName}.PATIENT_ATTRIB_VAL_SEQUENCE TO mcgdoc;
GRANT SELECT ON ${projectName}.BIOSPECIMEN_VALUE_SEQUENCE TO mcgdoc;
