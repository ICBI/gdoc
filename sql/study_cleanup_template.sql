DROP TABLESPACE ${projectName} INCLUDING CONTENTS AND DATAFILES;
DROP TABLESPACE ${projectName}_TEMP_TS INCLUDING CONTENTS AND DATAFILES;

DROP USER ${projectName} cascade;

DROP ROLE read_${projectName};
DROP ROLE insert_${projectName};
DROP ROLE edit_${projectName};
DROP ROLE create_${projectName};