
CREATE ROLE read_${projectName};
CREATE ROLE insert_${projectName};
CREATE ROLE edit_${projectName};
CREATE ROLE create_${projectName};

GRANT CREATE MATERIALIZED VIEW TO create_${projectName};
GRANT CREATE PROCEDURE TO create_${projectName};
GRANT CREATE PUBLIC SYNONYM TO create_${projectName};
GRANT CREATE SEQUENCE TO create_${projectName};
GRANT CREATE SESSION TO create_${projectName};
GRANT CREATE SYNONYM TO create_${projectName};
GRANT CREATE TABLE TO create_${projectName};
GRANT CREATE VIEW TO create_${projectName};
GRANT CREATE TRIGGER to create_${projectName};

GRANT read_${projectName} TO create_${projectName}; 
GRANT read_${projectName} TO insert_${projectName};
GRANT read_${projectName} TO edit_${projectName};

GRANT insert_${projectName} TO create_${projectName}; 
GRANT insert_${projectName} TO edit_${projectName};


CREATE USER ${projectName} 
  IDENTIFIED BY cur34c4nc3r
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP
  QUOTA UNLIMITED ON USERS;
GRANT CREATE SESSION TO ${projectName};
GRANT create_${projectName} to ${projectName};
GRANT create_${projectName} to mcgdoc;
GRANT edit_${projectName} to mcgdoc;
GRANT insert_${projectName} to mcgdoc;

GRANT read_common to ${projectName};
GRANT READ_${projectName} to guidoc;

GRANT REFERENCES ON COMMON.ATTRIBUTE_TYPE to ${projectName};
GRANT REFERENCES ON COMMON.ATTRIBUTE_VOCABULARY to ${projectName};
GRANT REFERENCES ON COMMON.PATIENT_DATA_SOURCE to ${projectName};
GRANT REFERENCES ON COMMON.HTARRAY_REPORTER_LIST to ${projectName};
GRANT REFERENCES ON COMMON.FILE_FORMAT to ${projectName};
GRANT REFERENCES ON COMMON.FILE_TYPE to ${projectName};
GRANT REFERENCES ON COMMON.HTARRAY_REPORTER to ${projectName};
GRANT REFERENCES ON COMMON.HTARRAY_DESIGN to ${projectName};
GRANT REFERENCES ON COMMON.MS_PEAK_EVIDENCE to ${projectName};
GRANT REFERENCES ON COMMON.MS_PEAK_GROUP to ${projectName};

GRANT ALL ON COMMON.patient_data_source to ${projectName} with grant option;
GRANT ALL ON COMMON.attribute_type to ${projectName} with grant option;
GRANT ALL ON COMMON.attribute_vocabulary to ${projectName} with grant option;
GRANT SELECT ON COMMON.ATTRIBUTE_VOCAB_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.ATTRIBUTE_TYPE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.PATIENT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.DATA_SOURCE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.CONTACT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.HTARRAY_DESIGN_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.HTARRAY_REPORTER_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.BIOSPECIMEN_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.DATA_FILE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.FILE_TYPE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.FILE_FORMAT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.ALL_DESIGNS to ${projectName} with grant option;
GRANT SELECT ON COMMON.DSC_SEQUENCE to ${projectName} with grant option;

grant select on common.attribute_type to ${projectName};
