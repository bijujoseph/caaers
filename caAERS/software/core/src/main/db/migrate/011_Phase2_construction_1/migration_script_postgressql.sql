--studies-epochs-arms data migration script for users migrating from caAERS1.1.x to caAERS 1.5.x
--Instructions
--Please execute Query 1 Thru Query 6 against the postgres database configured for caAERS.
--Copy the SQL statement between "--Start" & "--End" for execution.    

--Query 1 
--Start
CREATE LANGUAGE plpgsql;
--END

--Query 2
--Start
CREATE OR REPLACE FUNCTION studies_epochs_arms() RETURNS SETOF text AS 
$$
DECLARE
    study studies%rowtype;
    epochid integer;
BEGIN
    FOR study IN SELECT * FROM studies WHERE id > 0 order by id
    LOOP
        epochid := nextval('epochs_id_seq');
		INSERT INTO epochs (id,version,name,description,study_id,order_no,grid_id) values(epochid,0,'Baseline',null,study.id,0,null);
		INSERT INTO arms (id,version,name,description,epoch_id,grid_id) values(nextval('arms_id_seq'),0,'Baseline',null,epochid,null);
		epochid := nextval('epochs_id_seq');	
        INSERT INTO epochs (id,version,name,description,study_id,order_no,grid_id) values(epochid,0,'Treatment',null,study.id,1,null);
        INSERT INTO arms (id,version,name,description,epoch_id,grid_id) values(nextval('arms_id_seq'),0,'Treatment',null,epochid,null);
        epochid := nextval('epochs_id_seq');
        INSERT INTO epochs (id,version,name,description,study_id,order_no,grid_id) values(epochid,0,'Post-treatment',null,study.id,2,null);
        INSERT INTO arms (id,version,name,description,epoch_id,grid_id) values(nextval('arms_id_seq'),0,'Post-treatment',null,epochid,null);
        RETURN NEXT study.id || '--DONE';
    END LOOP;
END;
$$ 
LANGUAGE 'plpgsql' ;
--End

--Query 3
--Start
DELETE FROM arms;
--End

--Query 4
--Start
DELETE FROM epochs;
--End


--Query 5
--Start
SELECT * from studies_epochs_arms();
--End

--Query 6
--Start
DROP FUNCTION studies_epochs_arms();
--End