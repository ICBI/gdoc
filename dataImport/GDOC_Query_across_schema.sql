SET SERVEROUTPUT on size 30000;
DECLARE
  qry VARCHAR2(32767);
  rc sys_refcursor;
  l_ename all_tables.owner%TYPE;
  l_empno all_tables.table_name%TYPE;
  l_type common.data_source_content.content_type%TYPE;
  l_count NUMBER(38);
BEGIN
  FOR r IN (
    select  distinct z.owner, x.content_type content_type
    from common.data_source_content x, common.data_source y, all_tables z
    where x.data_source_id = y.data_source_id and
    z.owner=y.schema_name and table_name = 'SUBJECT'
    ) 
  LOOP
    IF qry IS NOT NULL THEN
      qry := qry || ' UNION ALL ';
    END IF;
    qry := qry || 'SELECT '''
               || r.content_type
               || ''' content_type, count(c.subject_id) subject_id FROM '
               || r.owner
               || '.subject c';
  END LOOP;  
    qry:='SELECT SUM(XX.subject_id), XX.content_type FROM ('
       || qry 
       ||') XX GROUP BY XX.content_type'; 
    OPEN rc FOR qry;
    Loop
      FETCH rc
        INTO  l_count, l_type;
        EXIT WHEN rc%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(l_type || ' , ' || l_count);
     END LOOP;
     CLOSE rc;
END;
-------------------------------------------

