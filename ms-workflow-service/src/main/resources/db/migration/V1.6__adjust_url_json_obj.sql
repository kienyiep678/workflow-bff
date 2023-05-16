--Change json type
ALTER TABLE tbl_task ALTER COLUMN add_json_obj TYPE JSONB;

--Rename add_json_obj into url_json_obj
ALTER TABLE tbl_task
RENAME COLUMN add_json_obj TO url_json_obj;