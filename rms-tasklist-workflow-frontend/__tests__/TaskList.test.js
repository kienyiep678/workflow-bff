import React from "react";
import { render, screen, cleanup } from "@testing-library/react";
import "@testing-library/jest-dom";
import request from "supertest";
import fetchMock from "jest-fetch-mock";

import TaskList from "../src/tasks-workflow/Task/TaskList.js";
import TaskGlobalApi from "../src/data/TaskGlobalApi.js";
import { USERNAME } from "../src/data/DataConst.js";

// unmount and cleanup DOM after the test is finished.
afterEach(cleanup);

test("TaskList - TableFilter.js", () => {
  render(<TaskList />);

  const taskName = screen.getByTestId("filter-task-name");
  const taskRefNo = screen.getByTestId("filter-task-refNo");
  const taskStage = screen.getByTestId("filter-task-stage");
  const taskType = screen.getByTestId("filter-task-type");

  expect(taskName).toBeInTheDocument();
  expect(taskRefNo).toBeInTheDocument();
  expect(taskStage).toBeInTheDocument();
  expect(taskType).toBeInTheDocument();
});

test("TaskList - skeleton is loading", () => {
  render(<TaskList />);

  const skeletonLoading = screen.getByTestId("skeleton-loading");
  expect(skeletonLoading).toBeInTheDocument();
});

// test("TaskList - backdrop is loading ", () => {
//   render(<TaskList />);

//   const openBackdrop = screen.getByTestId("show-backdrop");
//   expect(openBackdrop).toBeNull(); // by default is null
// });

// (this is some of the data from backend db)
// to check is the backend return the correct list

// const mockedResponse = [
//   {
//     taskReferenceNo: "TF00000401",
//     taskName: "Check api",
//     stageName: "TASK_CANCELLED",
//   },
//   {
//     taskReferenceNo: "TF00000454",
//     taskName: "Check dropdown",
//     stageName: "PENDING_APPROVAL",
//   },
// ];

// global.fetch = jest.fn(() =>
//   Promise.resolve({
//     json: () => Promise.resolve(mockedResponse),
//   })
// );

// test("Backend return tasklist data", async () => {
//   const params = {
//     "user-id": USERNAME,
//   };

//   const response = await TaskGlobalApi.GetTaskList(params);
//   expect(response.json).toBeDefined(); //check response is not undefined
//   // expect(response).resolves.toEqual(mockedResponse);

//   //Check that the response contains the expected data
//   const responseData = await response.json();
//   console.log(await response.json());

//   if (responseData.data.taskList) {
//     mockedResponse.forEach((item) => {
//       expect(
//         responseData.some(
//           (backendItem) =>
//             backendItem.taskReferenceNo === item.taskReferenceNo &&
//             backendItem.taskName === item.taskName &&
//             backendItem.stageName === item.stageName
//         )
//       ).toBe(true);
//     });
//   }
// });

//   const mockTableData = [
//     {
//       taskReferenceNo: "TF00000401",
//       taskName: "Check api",
//       stageName: "TASK_CANCELLED",
//     },
//     {
//       taskReferenceNo: "TF00000454",
//       taskName: "Check dropdown",
//       stageName: "PENDING_APPROVAL",
//     },
//   ];
