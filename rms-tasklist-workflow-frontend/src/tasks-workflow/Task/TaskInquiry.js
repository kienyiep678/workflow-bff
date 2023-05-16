import React, { Component } from "react";

// reactstrap components
import { Container } from "reactstrap";
// core components
import { useEffect, useState } from "react";

import { USERNAME } from "../../data/DataConst.js";
import TableFilter from "../../table-layouts/TableFilter.js";
import TableDisplay from "../../table-layouts/TableDisplay.js";
import PageTitle from "../../table-layouts/PageTitle.js";
import TaskGlobalApi from "../../data/TaskGlobalApi.js";

const TaskInquiry = () => {
  /* ==============================================================================================================================
                                                          USE STATE
       ==============================================================================================================================
  */
  const [isLoading, setIsLoading] = useState(false);

  const [tableData, setTableData] = useState([]);
  const [dropdownStatus, setDropdownStatus] = useState([]);
  const [dropdownTaskType, setDropdownTaskType] = useState([]);

  const [selectedTask, setSelectedTask] = useState("");
  const [selectedRefNo, setSelectedRefNo] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");
  const [selectedTaskType, setSelectedTaskType] = useState("");

  const [timestamps, setTimestamps] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");

  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPage] = useState(0);

  /* ==============================================================================================================================
                                          HANDLER  ->  Handle state change
       ==============================================================================================================================
    */

  //#region
  // Task Name
  const handleTaskChange = (event) => {
    // setSelectedTask(event.map((option) => option.value));
    setSelectedTask(event.target.value);
  };

  // Task Reference No
  const handleRefNoChange = (event) => {
    setSelectedRefNo(event.target.value);
  };

  // Task Status
  const handleStatusChange = (event) => {
    setSelectedStatus(event.value);
  };

  // Task Page Change -> Page number
  const handlePageChange = (pageNum) => {
    setIsLoading(true);
    setCurrentPage(pageNum);
  };

  const handleTaskTypeChange = (event) => {
    setSelectedTaskType(event.value);
  };

  // Sort date order -> dateUpdated
  const handleSortOrder = () => {
    if (sortOrder === "asc") {
      setTimestamps((prevTimestamps) =>
        prevTimestamps.slice().sort((a, b) => new Date(b) - new Date(a))
      );
      setSortOrder("desc");
    } else {
      setTimestamps((prevTimestamps) =>
        prevTimestamps.slice().sort((a, b) => new Date(a) - new Date(b))
      );
      setSortOrder("asc");
    }
  };

  //#endregion
  /* ==============================================================================================================================
                                          FUNCTION -> Handle filter table
     ==============================================================================================================================
 */
  //#region

  //search task name -> check is include inside table
  const resultName = tableData.filter((item) => {
    return (
      item.name && item.name.toLowerCase().includes(selectedTask.toLowerCase)
    );
  });

  //search task ref no -> check is include inside table
  const resultRefNo = tableData.filter((item) => {
    return (
      item.refNo && item.refNo.toLowerCase().includes(selectedRefNo.toLowerCase)
    );
  });

  //#endregion

  /* ==============================================================================================================================
                                                API  -> Handle call api 
       ==============================================================================================================================
    */
  //#region

  const GetApiTableData = () => {
    setIsLoading(true);

    const params = new URLSearchParams({
      name: selectedTask,
      status: selectedStatus,
      order: sortOrder,
      "task-type": selectedTaskType,

      "reference-no": selectedRefNo,
      "page-number": currentPage - 1,
      "page-size": pageSize,
      "user-id": USERNAME,
    });

    setTimeout(() => {
      TaskGlobalApi.GetTaskInquiry(params)
        .then((response) => response.json())
        .then((data) => {
          const list = data.data.taskList;

          if (list.length > 0) {
            setTableData(list);

            const date = list.map((obj) => obj.dateUpdated);
            setTimestamps(date);

            const count = data.data.countOfPages;
            setTotalPage(count);
          } else {
            console.log("No task inquiry available");
            setTableData([]);
          }
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }, 500);
  };

  const GetDropdownStatus = () => {
    setIsLoading(true);
    const path = "search";

    const params = new URLSearchParams({
      "user-id": USERNAME,
      path: path,
    });

    setTimeout(() => {
      TaskGlobalApi.GetStatusList(params)
        .then((response) => response.json())
        .then((data) => {
          // console.log(data.data); //whole list
          const list = data.data;

          setDropdownStatus(list);
          // console.log(statusList);
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }, 500);
  };

  const GetDropdownTaskType = () => {
    setIsLoading(true);
    const path = "search";

    const params = new URLSearchParams({
      "user-id": USERNAME,
      path: path,
    });

    setTimeout(() => {
      TaskGlobalApi.GetTaskTypeList(params)
        .then((response) => response.json())
        .then((data) => {
          // console.log(data.data); //whole list
          const list = data.data;

          setDropdownTaskType(list);
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }, 500);
  };

  //#endregion

  /* ==============================================================================================================================
                                            USE EFFECT  -> Handle on change reload page
       ==============================================================================================================================
  */

  //#region
  //get data when first reload
  useEffect(() => {
    GetApiTableData();
    GetDropdownStatus();
    GetDropdownTaskType();
  }, [currentPage]);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      // Send request to API
      GetApiTableData();
      GetDropdownStatus();
      GetDropdownTaskType();

      setCurrentPage(1);
      setPageSize(10);

      if (resultName.length > 0 && resultName !== "") {
        setSelectedTask(resultName);
      } else {
        setTableData([]);
      }
      if (resultRefNo.length > 0 && resultRefNo !== "") {
        setSelectedRefNo(resultRefNo);
      } else {
        setTableData([]);
      }
    }, 500);

    return () => {
      clearTimeout(timeoutId);
    };
  }, [
    selectedStatus,
    selectedTask,
    selectedRefNo,
    sortOrder,
    selectedTaskType,
  ]);

  //#endregion

  /* ==============================================================================================================================
                                                                   RETURN
       ==============================================================================================================================
    */

  //param for filter
  const taskFilter = {
    selectedTask: selectedTask,
    handleTaskChange: handleTaskChange,
    selectedRefNo: selectedRefNo,
    handleRefNoChange: handleRefNoChange,
    selectedStatus: selectedStatus,
    handleStatusChange: handleStatusChange,
    dropdownStatus: dropdownStatus,
    dropdownTaskType: dropdownTaskType,
    selectedTaskType: selectedTaskType,
    handleTaskTypeChange: handleTaskTypeChange,
  };
  const taskDisplay = {
    tableData: tableData,
    currentPage: currentPage,
    totalPages: totalPages,
    handlePageChange: handlePageChange,
    handleSortOrder: handleSortOrder,
  };

  return (
    <Container fluid>
      <Container
        style={{
          marginTop: "50px",
          color: "white",
        }}
        fluid
      >
        <PageTitle
          pageTitle="Task Inquiry"
          pageDescription="A dashboard to view all the processed task in the history."
        />

        <TableFilter taskFilter={taskFilter} hasCheckbox={false} />
      </Container>

      <Container
        style={{
          marginTop: "30px",
          background: "white",
          borderRadius: "20px",
        }}
        fluid
      >
        <TableDisplay
          taskDisplay={taskDisplay}
          link="inquiry"
          hasCheckbox={false}
          isLoading={isLoading}
        />
      </Container>
    </Container>
  );
};

export default TaskInquiry;

//commented

//#region

// let [selectedDateFrom, setSelectedDateFrom] = useState(null);
// let [selectedDateTo, setSelectedDateTo] = useState(null);

// function --- task name
//   const optionsTasksName = tableData.map((task) => {
//     return { value: task.name, label: task.name };
//   });

//   const uniqueOptionsTasksName = optionsTasksName.reduce((task, current) => {
//     //make sure is unique and show only one option
//     const x = task.find((item) => item.value === current.value);
//     if (!x) {
//       return task.concat([current]);
//     } else {
//       return task;
//     }
//   }, []);

// function --- task status
//   const optionsTasksStatus = tableData.map((task) => {
//     return { value: task.curStageCd, label: task.curStageCd };
//   });

//   const uniqueOptionsTasksStatus = optionsTasksStatus.reduce(
//     (task, current) => {
//       const x = task.find((item) => item.value === current.value);
//       if (!x) {
//         return task.concat([current]);
//       } else {
//         return task;
//       }
//     },
//     []
//   );
//#endregion
