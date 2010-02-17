GRANT SELECT ON ${projectName}.BIOSPECIMEN TO read_${projectName};
GRANT SELECT ON ${projectName}.PATIENT_ATTRIBUTE_VALUE TO read_${projectName};
GRANT SELECT ON ${projectName}.ORIGINAL_ATTRIBUTE TO read_${projectName};
GRANT SELECT ON ${projectName}.PATIENT TO read_${projectName};
GRANT SELECT ON ${projectName}.ATTRIBUTE_TIMEPOINT TO read_${projectName};
GRANT SELECT ON ${projectName}.ATTRIBUTE_STATS TO read_${projectName};
GRANT SELECT ON ${projectName}.BIOSPECIMEN_ATTRIBUTE_VALUE TO read_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE TO read_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_COLUMN TO read_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_ROW TO read_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_RUN_BIOSPECIMEN TO read_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_PRIOR TO read_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_RUN TO read_${projectName};
GRANT SELECT ON ${projectName}.KM_ATTRIBUTE TO read_${projectName};


GRANT SELECT ON ${projectName}.PATIENTS TO read_${projectName};
GRANT SELECT ON ${projectName}.USED_ATTRIBUTES TO read_${projectName};
GRANT SELECT ON ${projectName}.KM_ATTRIBUTES TO read_${projectName};
GRANT SELECT ON ${projectName}.PLIER_CONTENTS TO read_${projectName};

GRANT INSERT ON ${projectName}.BIOSPECIMEN TO insert_${projectName};
GRANT INSERT ON ${projectName}.PATIENT_ATTRIBUTE_VALUE TO insert_${projectName};
GRANT INSERT ON ${projectName}.ORIGINAL_ATTRIBUTE TO insert_${projectName};
GRANT INSERT ON ${projectName}.PATIENT TO insert_${projectName};
GRANT INSERT ON ${projectName}.ATTRIBUTE_TIMEPOINT TO insert_${projectName};
GRANT INSERT ON ${projectName}.ATTRIBUTE_STATS TO insert_${projectName};
GRANT INSERT ON ${projectName}.BIOSPECIMEN_ATTRIBUTE_VALUE TO insert_${projectName};
GRANT INSERT ON ${projectName}.MARRAY_FILE TO insert_${projectName};
GRANT INSERT ON ${projectName}.MARRAY_FILE_COLUMN TO insert_${projectName};
GRANT INSERT ON ${projectName}.MARRAY_FILE_ROW TO insert_${projectName};
GRANT INSERT ON ${projectName}.MARRAY_RUN_BIOSPECIMEN TO insert_${projectName};
GRANT INSERT ON ${projectName}.MARRAY_FILE_PRIOR TO insert_${projectName};
GRANT INSERT ON ${projectName}.MARRAY_RUN TO insert_${projectName};
GRANT INSERT ON ${projectName}.KM_ATTRIBUTE TO insert_${projectName};

GRANT INSERT ON COMMON.patient_data_source to insert_${projectName};


GRANT SELECT ON ${projectName}.PAV_GROUP_SEQUENCE TO insert_${projectName};
GRANT SELECT ON ${projectName}.PATIENT_ATTRIB_VAL_SEQUENCE TO insert_${projectName};
GRANT SELECT ON ${projectName}.BIOSPECIMEN_VALUE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_COLUMN_SEQUENCE TO insert_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_ROW_SEQUENCE TO insert_${projectName};

GRANT SELECT ON COMMON.ATTRIBUTE_VOCAB_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.ATTRIBUTE_TYPE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.ATTRIBUTE_TIMEPOINT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.PATIENT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.DATA_SOURCE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.CONTACT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.ARRAY_DESIGN_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.MARRAY_REPORTER_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.REPORTER_LIST_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.BIOSPECIMEN_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.DATA_FILE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.MARRAY_RUN_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.PROTOCOL_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.FILE_TYPE_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.FILE_FORMAT_SEQUENCE TO insert_${projectName};
GRANT SELECT ON COMMON.MARRAY_RUN_BIOSPEC_SEQUENCE TO insert_${projectName};


GRANT UPDATE, DELETE ON ${projectName}.BIOSPECIMEN TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.PATIENT_ATTRIBUTE_VALUE TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.ORIGINAL_ATTRIBUTE TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.PATIENT TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.ATTRIBUTE_TIMEPOINT TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.ATTRIBUTE_STATS TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.BIOSPECIMEN_ATTRIBUTE_VALUE TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.MARRAY_FILE TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.MARRAY_FILE_COLUMN TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.MARRAY_FILE_ROW TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.MARRAY_RUN_BIOSPECIMEN TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.MARRAY_FILE_PRIOR TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.MARRAY_RUN TO edit_${projectName};
GRANT UPDATE, DELETE ON ${projectName}.KM_ATTRIBUTE TO edit_${projectName};

GRANT SELECT ON ${projectName}.PAV_GROUP_SEQUENCE TO edit_${projectName};
GRANT SELECT ON ${projectName}.PATIENT_ATTRIB_VAL_SEQUENCE TO edit_${projectName};
GRANT SELECT ON ${projectName}.BIOSPECIMEN_VALUE_SEQUENCE TO edit_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_COLUMN_SEQUENCE TO edit_${projectName};
GRANT SELECT ON ${projectName}.MARRAY_FILE_ROW_SEQUENCE TO edit_${projectName};

GRANT SELECT ON ${projectName}.PATIENT_ATTRIB_VAL_SEQUENCE TO mcgdoc;
