--tbl_mt_task_typ
INSERT INTO maintenance.tbl_mt_task_typ(
	mt_task_typ_cd, task_name, task_desc, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('TECH_PROFILE', 'Technical Profile', 'Technical Profile', TRUE, 'ADMIN', Now(), null, null);
INSERT INTO maintenance.tbl_mt_task_typ(
	mt_task_typ_cd, task_name, task_desc, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('CALENDAR', 'Calendar', ' Calendar', TRUE, 'ADMIN', Now(), null, null);

--tbl_mt_task_typ_config
INSERT INTO maintenance.tbl_mt_task_typ_config(
	mt_task_typ_config_id, mt_task_typ_cd, num_of_approver, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('81f8f7b7-f461-447e-8421-e6de56027b1a', 'TECH_PROFILE', 1, TRUE, 'ADMIN', Now(), null, null);
INSERT INTO maintenance.tbl_mt_task_typ_config(
	mt_task_typ_config_id, mt_task_typ_cd, num_of_approver, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('0f23a713-b32b-43d9-a2f5-e6ee2da37da6', 'CALENDAR', 1, TRUE, 'ADMIN', Now(), null, null);

--tbl_mt_stage_config
INSERT INTO maintenance.tbl_mt_stage_config(
	stage_cd, user_role_cd, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('TASK_CREATION', 'MAKER', TRUE, 'ADMIN', Now(), null, null);
INSERT INTO maintenance.tbl_mt_stage_config(
	stage_cd, user_role_cd, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('PENDING_APPROVAL', 'MAKER', TRUE, 'ADMIN', Now(), null, null);
INSERT INTO maintenance.tbl_mt_stage_config(
	stage_cd, user_role_cd, is_active, created_by, dt_created, updated_by, dt_updated)
	VALUES ('HOST_EXCEPTION', 'MAKER;CHECKER', TRUE, 'ADMIN', Now(), null, null);
	
--tbl_task
INSERT INTO public.tbl_task(
	id, cmd_proc_inst_id, ref_no, name, description, add_json_obj, cur_activity_cd, mt_task_typ_cd, cur_holder_user_id, action_by, action_typ, dt_action, updated_by, dt_updated, created_by, dt_created, remark_msg, remark_msg_rej, is_end_stage, err_msg)
	VALUES ('6a35b5be-3a70-4d48-86ee-dc49f8c4234d', 'CMDSAMPLEID123', 'SAMPLEREFNO123', 'Create Technical Profile - John Yap', 'Create Technical Profile for John Yap', '{"approve_url":"test.com/test/create","view_url":"test.com/test/view","reject_url":"test.com/test/delete"}', 'PENDING_APPROVAL', 'TECH_PROFILE', 'JOHN', null, null, null,'NICK', Now(), 'NICK', Now(), null, null, FALSE, null);
INSERT INTO public.tbl_task(
	id, cmd_proc_inst_id, ref_no, name, description, add_json_obj, cur_activity_cd, mt_task_typ_cd, cur_holder_user_id, action_by, action_typ, dt_action, updated_by, dt_updated, created_by, dt_created, remark_msg, remark_msg_rej, is_end_stage, err_msg)
	VALUES ('dfb648df-2fac-4af6-9c64-6d73cc4632b8', 'CMDSAMPLEID123', 'SAMPLEREFNO456', 'Submit Holiday Request - Raya', 'Submit Holiday Request', '{"approve_url":"test.com/test/create","view_url":"test.com/test/view","reject_url":"test.com/test/delete"}', 'TASK_COMPLETED', 'TECH_PROFILE', null, 'JOHN', 'APPROVE_TASK', '2023-03-23 04:25:00.416113', 'JOHN', '2023-03-23 04:25:00.416113', 'NICK', '2023-03-22 05:34:00.416113', null, null, TRUE, null);

--tbl_task_history
INSERT INTO public.tbl_task_history(
	task_history_id, task_id, action_name, stage_from, stage_to, holder_from_user_id, holder_to_user_id, dt_start, dt_end)
	VALUES ('2b1ee025-5289-459b-a9ef-dc6bfb4d5108', '6a35b5be-3a70-4d48-86ee-dc49f8c4234d', null, null, 'Task Creation', null, 'Nick', Now(), Now());
INSERT INTO public.tbl_task_history(
	task_history_id, task_id, action_name, stage_from, stage_to, holder_from_user_id, holder_to_user_id, dt_start, dt_end)
	VALUES ('8f519455-9884-42b4-a2c7-b0608027a33a', '6a35b5be-3a70-4d48-86ee-dc49f8c4234d', 'Submit for Approval', 'Task Creation', 'Pending Approval', 'Nick', 'John', Now(), Now());
INSERT INTO public.tbl_task_history(
	task_history_id, task_id, action_name, stage_from, stage_to, holder_from_user_id, holder_to_user_id, dt_start, dt_end)
	VALUES ('7a19bcc7-2d4d-42b5-91a1-5bd97df50766', 'dfb648df-2fac-4af6-9c64-6d73cc4632b8', null, null, 'Task Creation', null, 'Nick', '2023-03-22 05:34:00.416113', '2023-03-22 05:34:00.416113');
INSERT INTO public.tbl_task_history(
	task_history_id, task_id, action_name, stage_from, stage_to, holder_from_user_id, holder_to_user_id, dt_start, dt_end)
	VALUES ('8b48a414-6cd1-4c89-9190-1523fcf74a7a', 'dfb648df-2fac-4af6-9c64-6d73cc4632b8', 'Submit for Approval', 'Task Creation', 'Pending Approval', 'Nick', 'John', '2023-03-22 05:34:00.416113', '2023-03-23 04:25:00.416113');
INSERT INTO public.tbl_task_history(
	task_history_id, task_id, action_name, stage_from, stage_to, holder_from_user_id, holder_to_user_id, dt_start, dt_end)
	VALUES ('9ef8b3ae-88dc-4bad-a042-d100d21854e5', 'dfb648df-2fac-4af6-9c64-6d73cc4632b8', 'Approve', 'Pending Approval', 'Task Completed', 'John', null, '2023-03-23 04:25:00.416113', '2023-03-23 04:25:00.416113');