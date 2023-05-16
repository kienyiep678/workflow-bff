--Remove the constraint
ALTER TABLE tbl_task
DROP CONSTRAINT fk_mt_task_typ_cd;

--Delete Old table
DELETE FROM maintenance.tbl_mt_task_typ_config;
DELETE FROM maintenance.tbl_mt_task_typ;

--Create new table with new columns
DROP TABLE IF EXISTS maintenance.tbl_mt_task_typ_config;
DROP TABLE IF EXISTS maintenance.tbl_mt_task_typ;

CREATE TABLE maintenance.tbl_mt_task_typ (
	cd VARCHAR(50) PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(200) NULL,
	is_active BOOL NOT NULL DEFAULT FALSE,
	is_delete BOOL NOT NULL DEFAULT FALSE,
	created_by VARCHAR(50) NULL,
	dt_created TIMESTAMP NULL,
	updated_by VARCHAR(50) NULL,
	dt_updated TIMESTAMP NULL
);

CREATE TABLE maintenance.tbl_mt_task_typ_config (
	id UUID PRIMARY KEY,
	mt_task_typ_cd VARCHAR(50) NOT NULL,
	num_of_approver INT NOT NULL,
	created_by VARCHAR(50) NULL,
	dt_created TIMESTAMP NULL,
	updated_by VARCHAR(50) NULL,
	dt_updated TIMESTAMP NULL,
	CONSTRAINT fk_mt_task_typ_cd
      FOREIGN KEY(mt_task_typ_cd)
	  REFERENCES maintenance.tbl_mt_task_typ(cd)
);

--Insert Initial Values
INSERT INTO maintenance.tbl_mt_task_typ(
	cd, name, description, is_active, is_delete, created_by, dt_created, updated_by, dt_updated)
	VALUES ('TECH_PROFILE', 'Tech Profile', 'Tech Profile Type and Configuration', TRUE, FALSE, 'SYSTEM', Now(), null, null);
INSERT INTO maintenance.tbl_mt_task_typ(
	cd, name, description, is_active, is_delete, created_by, dt_created, updated_by, dt_updated)
	VALUES ('CALENDAR', 'Calendar', 'Calendar Type and Configuration', TRUE, FALSE, 'SYSTEM', Now(), null, null);

INSERT INTO maintenance.tbl_mt_task_typ_config(
	id, mt_task_typ_cd, num_of_approver, created_by, dt_created, updated_by, dt_updated)
	VALUES ('51b0c434-9786-44c5-9642-770f3dd07e75', 'TECH_PROFILE', 1, 'SYSTEM', Now(), null, null);
INSERT INTO maintenance.tbl_mt_task_typ_config(
	id, mt_task_typ_cd, num_of_approver, created_by, dt_created, updated_by, dt_updated)
	VALUES ('16094b84-741f-4d15-85f7-07c3fe2f2a61', 'CALENDAR', 1, 'SYSTEM', Now(), null, null);

--Add Back the constraint
ALTER TABLE tbl_task ADD CONSTRAINT fk_mt_task_typ_cd FOREIGN KEY (mt_task_typ_cd) REFERENCES maintenance.tbl_mt_task_typ(cd);
