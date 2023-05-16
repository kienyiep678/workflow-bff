import TaskInquiry from "./tasks-workflow/Task/TaskInquiry.js";
import TaskPool from "./tasks-workflow/Task/TaskPool.js";
import TaskDetails from "./tasks-workflow/Task/TaskDetails";
import TaskList from "./tasks-workflow/Task/TaskList";
import TaskTypeSetting from "./tasks-workflow/TaskSetting/TaskTypeSetting.js";
import TaskCreation from "./tasks-workflow/TaskSetting/TaskTypeCreation.js";
import WorkflowTaskDetail from "./tasks-workflow/TaskSetting/TaskTypeDetail.js";

var routes = [
  {
    path: "/task/list",
    name: "Task List",
    component: TaskList,
    layout: "/rms",
  },
  {
    path: "/task/pool",
    name: "Task Pool",
    component: TaskPool,
    layout: "/rms",
  },
  {
    path: "/task/search",
    name: "Task Inquiry",
    component: TaskInquiry,
    layout: "/rms",
  },
  {
    path: "/task/details",
    component: TaskDetails,
    name: "Task Details",
    layout: "/rms",
  },
  {
    path: "/task/setting/task-type/list",
    name: "Task Type Setting ",
    component: TaskTypeSetting,
    layout: "/rms",
  },
  {
    path: "/task/setting/task-type/creation",
    name: "Task Creation",
    component: TaskCreation,
    layout: "/rms",
  },
  {
    path: "/task/setting/task-type/detail",
    component: WorkflowTaskDetail,
    name: "Workflow Task Detail",
    layout: "/rms",
  },
];
export default routes;
