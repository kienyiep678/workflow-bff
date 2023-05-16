CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


DROP SCHEMA IF EXISTS maintenance;
CREATE SCHEMA maintenance;

DROP TABLE IF EXISTS maintenance.tbl_mt_task_typ;
CREATE TABLE maintenance.tbl_mt_task_typ (
	mt_task_typ_cd VARCHAR(50) PRIMARY KEY,
	task_name VARCHAR(100) NOT NULL,
	task_desc VARCHAR(200) NULL,
	is_active BOOL NOT NULL DEFAULT FALSE,
	created_by VARCHAR(50) NULL,
	dt_created TIMESTAMP NULL,
	updated_by VARCHAR(50) NULL,
	dt_updated TIMESTAMP NULL
);

DROP TABLE IF EXISTS maintenance.tbl_mt_task_typ_config;
CREATE TABLE maintenance.tbl_mt_task_typ_config (
	mt_task_typ_config_id UUID PRIMARY KEY,
	mt_task_typ_cd VARCHAR(50) NOT NULL,
	num_of_approver INT NOT NULL,
	is_active BOOL NOT NULL DEFAULT FALSE,
	created_by VARCHAR(50) NULL,
	dt_created TIMESTAMP NULL,
	updated_by VARCHAR(50) NULL,
	dt_updated TIMESTAMP NULL,
	CONSTRAINT fk_mt_task_typ_cd
      FOREIGN KEY(mt_task_typ_cd) 
	  REFERENCES maintenance.tbl_mt_task_typ(mt_task_typ_cd)
);

DROP TABLE IF EXISTS public.tbl_task;
CREATE TABLE public.tbl_task (
	id UUID PRIMARY KEY,
	cmd_proc_inst_id VARCHAR(64) NOT NULL,
	ref_no VARCHAR(20) NOT NULL,
	name VARCHAR(200) NULL,
	description VARCHAR(1000) NULL,
	add_json_obj json NULL,
	cur_activity_cd VARCHAR(50) NOT NULL,
	mt_task_typ_cd VARCHAR(50) NOT NULL,
	cur_holder_user_id VARCHAR(50) NULL,
	action_by VARCHAR(50) NULL,
	action_typ VARCHAR(50) NULL,
	dt_action TIMESTAMP NULL,
	updated_by VARCHAR(50) NULL, 	
	dt_updated TIMESTAMP NULL,
	created_by VARCHAR(50) NULL,
	dt_created TIMESTAMP NULL,
	remark_msg VARCHAR(500) NULL,
	remark_msg_rej VARCHAR(500) NULL,
	is_end_stage BOOL NOT NULL DEFAULT FALSE,
	err_msg VARCHAR(1000) NULL,
	CONSTRAINT fk_mt_task_typ_cd
      FOREIGN KEY(mt_task_typ_cd) 
	  REFERENCES maintenance.tbl_mt_task_typ(mt_task_typ_cd)
);

DROP TABLE IF EXISTS public.tbl_task_history;
CREATE TABLE public.tbl_task_history (
	task_history_id UUID PRIMARY KEY,
	task_id UUID NOT NULL,
	action_name VARCHAR(100) NULL,
	stage_from VARCHAR(100) NULL,
	stage_to VARCHAR(100) NULL,
	holder_from_user_id VARCHAR(50) NULL,
	holder_to_user_id VARCHAR(50) NULL,
	dt_start TIMESTAMP NULL,
	dt_end TIMESTAMP NULL,
	CONSTRAINT fk_task_id
      FOREIGN KEY(task_id) 
	  REFERENCES public.tbl_task(id)
);


DROP TABLE IF EXISTS maintenance.tbl_mt_stage_config;
CREATE TABLE maintenance.tbl_mt_stage_config (
	stage_cd VARCHAR(100) PRIMARY KEY,
	user_role_cd VARCHAR(500) NULL,
	is_active BOOL NOT NULL DEFAULT FALSE,
	created_by VARCHAR(50) NULL,
	dt_created TIMESTAMP NULL,
	updated_by VARCHAR(50) NULL,
	dt_updated TIMESTAMP NULL
);





