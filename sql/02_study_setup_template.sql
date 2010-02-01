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
GRANT DROP PUBLIC SYNONYM TO create_${projectName};
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
GRANT read_common to ${projectName};
-- cannot do the following grants to a role:
GRANT REFERENCES ON COMMON.PROTOCOL to ${projectName}; 
GRANT REFERENCES ON COMMON.ATTRIBUTE_TYPE to ${projectName};
GRANT REFERENCES ON COMMON.ATTRIBUTE_VOCABULARY to ${projectName};
GRANT REFERENCES ON COMMON.PATIENT_DATA_SOURCE to ${projectName};
GRANT REFERENCES ON COMMON.REPORTER_LIST to ${projectName};
GRANT REFERENCES ON COMMON.FILE_FORMAT to ${projectName};
GRANT REFERENCES ON COMMON.FILE_TYPE to ${projectName};
GRANT REFERENCES ON COMMON.mARRAY_REPORTER to ${projectName};
GRANT REFERENCES ON COMMON.mARRAY_DESIGN to ${projectName};
GRANT SELECT ON COMMON.patient_data_source to ${projectName} with grant option;
GRANT SELECT ON COMMON.attribute_type to ${projectName} with grant option;
GRANT SELECT ON COMMON.attribute_vocabulary to ${projectName} with grant option;
GRANT read_${projectName} to guidoc;
GRANT create_${projectName} to guidoc;
-- not sure why i need this here....would it not be covered by read_common
grant select on common.attribute_type to ${projectName};
