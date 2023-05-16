--Drop Reject Remark message
ALTER TABLE tbl_task
DROP COLUMN IF EXISTS remark_msg_rej;

--Add stage description
ALTER TABLE maintenance.tbl_mt_stage_config
ADD COLUMN stage_desc VARCHAR(1000) NULL;
