DROP USER ${projectName} cascade;

DROP ROLE read_${projectName};
DROP ROLE insert_${projectName};
DROP ROLE edit_${projectName};
DROP ROLE create_${projectName};