const WorkflowServiceHost = "http://localhost:8083/";

const URL_StatusList = "task/get-status";
const URL_TaskTypeList = "task/get-type";

const URL_TaskList = "task/list/personal-list";
const URL_TaskInquiry = "task/list/search";
const URL_TaskPool = "task/list/pool";
const URL_TaskClaim = "task/claim";

const URL_TaskDetails = "task/get";
const URL_TaskRoute = "task/route";
const URL_TaskSave = "task/save";

const URL_TaskSetting = "config/task-type/list";
const URL_GetTaskType = "config/task-type/get";
const URL_TaskType_Add = "config/task-type/add-task-type";
const URL_TaskType_Update = "config/task-type/update-task-type";
const URL_TaskType_Delete = "config/task-type/delete-task-type";

const headers = {
  "Content-Type": "application/json",
};

class TaskGlobalApi {
  //Status dropdown options
  async GetStatusList(params) {
    const res = await fetch(
      `${WorkflowServiceHost}${URL_StatusList}?${params}`,
      {
        method: "GET",
      }
    );
    return res;
  }

  //Task type dropdown options
  async GetTaskTypeList(params) {
    const res = await fetch(
      `${WorkflowServiceHost}${URL_TaskTypeList}?${params}`,
      {
        method: "GET",
      }
    );
    return res;
  }

  //Task list
  async GetTaskList(params) {
    const res = await fetch(`${WorkflowServiceHost}${URL_TaskList}?${params}`, {
      method: "GET",
    });
    return res;
  }

  //Task Inquiry
  async GetTaskInquiry(params) {
    const res = await fetch(
      `${WorkflowServiceHost}${URL_TaskInquiry}?${params}`,
      {
        method: "GET",
      }
    );
    return res;
  }

  //Task pool
  async GetTaskPool(params) {
    const res = await fetch(`${WorkflowServiceHost}${URL_TaskPool}?${params}`, {
      method: "GET",
    });
    return res;
  }

  //Claim Task
  async SendClaimTask(user, listToClaim) {
    const res = await fetch(`${WorkflowServiceHost}${URL_TaskClaim}`, {
      method: "POST",
      headers,
      body: JSON.stringify({
        userId: user,
        listToClaim: listToClaim,
      }),
    });
    return res.json();
  }

  //Get Task Details
  async GetTaskDetails(params) {
    const response = await fetch(
      `${WorkflowServiceHost}${URL_TaskDetails}/${params}`,
      {
        method: "GET",
      }
    );

    return response;
  }

  //START TASK SETTING
  async GetTaskSettingList(params) {
    const res = await fetch(
      `${WorkflowServiceHost}${URL_TaskSetting}/${params}`,
      {
        method: "GET",
      }
    );
    return res;
  }

  async SaveNewTask(submitNewTask) {
    const response = await fetch(`${WorkflowServiceHost}${URL_TaskType_Add}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(submitNewTask),
    });
    return response;
  }

  async GetTaskSettingDetail(params) {
    const res = await fetch(
      `${WorkflowServiceHost}${URL_GetTaskType}/${params}`,
      {
        method: "GET",
      }
    );
    return res;
  }

  async UpdateTask(UpdateDetail) {
    const response = await fetch(
      `${WorkflowServiceHost}${URL_TaskType_Update}`,
      {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(UpdateDetail),
      }
    );
    return response;
  }

  async DeleteTask(params) {
    const res = await fetch(
      `${WorkflowServiceHost}${URL_TaskType_Delete}/${params}`,
      {
        method: "DELETE",
      }
    );
    return res;
  }
  //END TASK SETTING

  //Submit Task Details
  async SubmitTaskDetails(submitBodyResponse) {
    const response = await fetch(`${WorkflowServiceHost}${URL_TaskRoute}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(submitBodyResponse),
    });

    return response;
  }

  //Save Task Details
  async SaveTaskDetails(saveBodyResponse) {
    const response = await fetch(`${WorkflowServiceHost}${URL_TaskSave}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(saveBodyResponse),
    });

    return response;
  }
}

export default new TaskGlobalApi();
