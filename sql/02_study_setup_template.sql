-- to be run as gdocadin

-------------------
------ Roles ------
-------------------
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

GRANT edit_${projectName} TO edit_gdoc;

CREATE USER ${projectName} 
  IDENTIFIED BY change_me
  DEFAULT TABLESPACE ${projectName}
  TEMPORARY TABLESPACE ${projectName}_temp_ts
  QUOTA UNLIMITED ON ${projectName};
GRANT CREATE SESSION TO ${projectName};
GRANT create_${projectName} to ${projectName};
GRANT create_${projectName} to mcgdoc;
GRANT edit_${projectName} to mcgdoc;
GRANT insert_${projectName} to mcgdoc;

GRANT read_common to ${projectName};
-- cannot do the following grants to a role:
GRANT REFERENCES ON COMMON.PROTOCOL to ${projectName}; 
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
GRANT SELECT ON COMMON.ATTRIBUTE_TIMEPOINT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.PATIENT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.DATA_SOURCE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.CONTACT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.HTARRAY_DESIGN_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.HTARRAY_REPORTER_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.BIOSPECIMEN_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.DATA_FILE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.PROTOCOL_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.FILE_TYPE_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.FILE_FORMAT_SEQUENCE to ${projectName} with grant option;
GRANT SELECT ON COMMON.ALL_DESIGNS to ${projectName} with grant option;

-- not sure why i need this here....would it not be covered by read_common
grant select on common.attribute_type to ${projectName};
