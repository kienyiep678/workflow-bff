--Clear data from this two table to prevent error while running the following script
DELETE FROM tbl_task_history;
DELETE FROM tbl_task;

--Create new sequence script and trigger for reference no [START]
CREATE SEQUENCE tf_seq START 1;

CREATE OR REPLACE FUNCTION generate_ref_no()
RETURNS TRIGGER AS $$
BEGIN
  NEW.ref_no := 'TF' || LPAD(CAST(NEXTVAL('tf_seq') AS VARCHAR), 8, '0');
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tbl_task_ref_no_trigger
BEFORE INSERT ON tbl_task
FOR EACH ROW
EXECUTE FUNCTION generate_ref_no();

ALTER TABLE tbl_task ADD CONSTRAINT ref_no_unique UNIQUE (ref_no);
--Create new sequence script and trigger for reference no [END]

--Create new table to foreign key with action type [START]
DROP TABLE IF EXISTS maintenance.tbl_mt_action_typ;
CREATE TABLE maintenance.tbl_mt_action_typ (
	action_typ INT PRIMARY KEY,
	action_typ_desc VARCHAR(500) NULL
);

INSERT INTO maintenance.tbl_mt_action_typ (action_typ, action_typ_desc) VALUES (1,'APPROVE TASK');
INSERT INTO maintenance.tbl_mt_action_typ (action_typ, action_typ_desc) VALUES (2,'REJECT TASK');
INSERT INTO maintenance.tbl_mt_action_typ (action_typ, action_typ_desc) VALUES (3,'CANCEL TASK');

ALTER TABLE tbl_task ALTER COLUMN action_typ TYPE INT USING action_typ::integer;

ALTER TABLE tbl_task ADD CONSTRAINT fk_action_typ FOREIGN KEY (action_typ) REFERENCES maintenance.tbl_mt_action_typ (action_typ);
--Create new table to foreign key with action type [END]

--Rename activity cd into stage cd
ALTER TABLE tbl_task 
RENAME COLUMN cur_activity_cd TO cur_stage_cd;

