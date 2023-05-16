import React, { Component } from "react";

// core components
import { Container } from "reactstrap";
import { useEffect, useState } from "react";

import {
  USERNAME,
  UI_POPUP_CLAIM,
  UI_MESSAGE_SUCCESS,
  UI_MESSAGE_FAIL,
} from "../../data/DataConst.js";
import TaskGlobalApi from "../../data/TaskGlobalApi.js";

import TableFilter from "../../table-layouts/TableFilter.js";
import TableDisplay from "../../table-layouts/TableDisplay.js";
import PageTitle from "../../table-layouts/PageTitle.js";

const TaskPool = () => {
  /* ==============================================================================================================================
                                                          USE STATE
  ============================================================================================================================== */
  const [isLoading, setIsLoading] = useState(false);
  const [openBackdropLoading, setOpenBackdropLoading] = useState(false);

  const [tableData, setTableData] = useState([]);

  // Checkboxes
  const [selectedCheckbox, setSelectedCheckbox] = useState(
    tableData.map((task) => task.id)
  );
  const [selectedCheckboxRefNo, setSelectedCheckboxRefNo] = useState(
    tableData.map((task) => task.refNo)
  );
  const isCheckboxListEmpty = selectedCheckbox.length === 0;

  // Filter task, status, reference no
  const [dropdownStatus, setDropdownStatus] = useState([]);
  const [dropdownTaskType, setDropdownTaskType] = useState([]);

  const [selectedTask, setSelectedTask] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");
  const [selectedRefNo, setSelectedRefNo] = useState("");
  const [selectedTaskType, setSelectedTaskType] = useState("");

  // date time sorting
  const [timestamps, setTimestamps] = useState([]);
  const [sortOrder, setSortOrder] = useState("asc");

  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPage] = useState(0);

  //Task claim status
  const [claimFailed, setClaimFailed] = useState(null);
  const [claimSuccess, setClaimSuccess] = useState(null);

  // Show modal after click claim
  const [showClaimModal, setShowClaimModal] = useState(false);

  // Modal content
  const [modalTitle, setModalTitle] = useState("");
  const [popUpType, setPopUpType] = useState(null); // Changes here
  const [showError, setShowError] = useState(false);
  const [showPartialError, setShowPartialError] = useState(false);

  // For error details
  const [errorMessage, setErrorMessage] = useState([]);
  const [successMessage, setSuccessMessage] = useState([]);

  /* ==============================================================================================================================
                                                          HANDLER
       ==============================================================================================================================
    */

  //#region
  // Task name
  const handleTaskChange = (event) => {
    setSelectedTask(event.target.value);
  };

  // Task reference number
  const handleRefNoChange = (event) => {
    setSelectedRefNo(event.target.value);
  };

  // Task status
  //add selected status inside array - status
  const handleStatusChange = (event) => {
    setSelectedStatus(event.value);
  };

  const handleTaskTypeChange = (event) => {
    setSelectedTaskType(event.value);
  };

  // Page number
  const handlePageChange = (pageNum) => {
    setIsLoading(true);

    setCurrentPage(pageNum);
    setSelectedCheckbox([]); //reset checkbox
  };

  // Checkbox is selected/!selected
  const handleIDCheckboxChange = (event) => {
    const { checked } = event.target;
    const startIndex = (currentPage - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const currentData = tableData.slice(startIndex, endIndex);
    setSelectedCheckbox(checked ? currentData.map((task) => task.id) : []);

    setSelectedCheckboxRefNo(
      checked ? currentData.map((task) => task.refNo) : []
    );
  };

  // when a row is selected
  const handleRowCheckboxChange = (event, rowID) => {
    const { checked } = event.target;
    if (checked) {
      // Add the row name to the selected rows array
      setSelectedCheckbox((prevSelectedRows) => [...prevSelectedRows, rowID]);
    } else {
      // Remove the row name from the selected rows array
      setSelectedCheckbox((prevSelectedRows) =>
        prevSelectedRows.filter((selectedRow) => selectedRow !== rowID)
      );
    }
  };

  // Claim button
  const handleClaimClick = async () => {
    // setTimeout(() => {
    ClaimTasksApi();
    // }, 500);
  };

  // Close modal
  const handleModalClosed = () => {
    setIsLoading(true);
    setShowClaimModal(false);

    setSelectedCheckbox([]);

    setClaimSuccess(null);
    setClaimFailed(null);

    GetApiTableData();
    console.log("reload", tableData.length);
  };

  //#endregion
  /* ==============================================================================================================================
                                                            FUNCTION
     ==============================================================================================================================
 */

  //#region

  //search task name
  const resultName = tableData.filter((item) => {
    return (
      item.name && item.name.toLowerCase().includes(selectedTask.toLowerCase)
    );
  });

  //search task ref no
  const resultRefNo = tableData.filter((item) => {
    return (
      item.refNo && item.refNo.toLowerCase().includes(selectedRefNo.toLowerCase)
    );
  });

  // sort date order
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

  // Success Message : to combine success message
  const getSuccessMessageList = () => {
    if (successMessage !== null && showPartialError) {
      return (
        successMessage.map((item) => item.referenceNo).join(", ") +
        UI_MESSAGE_SUCCESS
      );
    }

    return null;
  };

  // for error details : to combine error message and display as one
  function generateErrorMessageString(errorList) {
    const numString = errorList.map((item) => item.referenceNo).join(", ");

    const uniqueErrorMessages = new Set();
    const resMsg = errorList
      .map((item) => {
        if (!uniqueErrorMessages.has(item.responseMessage)) {
          uniqueErrorMessages.add(item.responseMessage);
          return item.responseMessage;
        }
      })
      .filter(Boolean);
    return numString + ": " + resMsg;
  }

  // Error Message
  const getErrorMessageList = () => {
    return errorMessage !== null && showError
      ? generateErrorMessageString(errorMessage)
      : null;
  };

  // Modal Text
  const getModalText = () => {
    return claimSuccess !== null
      ? claimSuccess.map((item) => item.referenceNo).join(", ") +
          UI_MESSAGE_SUCCESS
      : claimFailed !== null
      ? UI_MESSAGE_FAIL
      : null;
  };

  //#endregion

  /* ==============================================================================================================================
                                                               API
       ==============================================================================================================================
    */

  //#region

  // get whole table
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
      TaskGlobalApi.GetTaskPool(params)
        .then((response) => response.json())
        .then((data) => {
          const list = data.data.taskList;
          // console.log(list);

          if (list.length > 0) {
            setTableData(list);

            const date = list.map((obj) => obj.dateUpdated);
            setTimestamps(date);

            const count = data.data.countOfPages;
            setTotalPage(count);
          } else {
            console.log("No task pool available");
          }
        })

        .catch((error) => {
          console.log(error);
          setTableData([]);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }, 500);
  };

  // for task claim
  const ClaimTasksApi = () => {
    // setIsLoading(true);
    console.log("claim");
    setOpenBackdropLoading(true);

    // setTimeout(() => {
    TaskGlobalApi.SendClaimTask(USERNAME, selectedCheckbox)
      .then((response) => response)
      .then((data) => {
        // console.log("Data: ", data);
        const successList = data.data.success;
        const errorList = data.data.fail;

        if (data.code === "200") {
          // console.log("Status: ", data.code);
          setPopUpType(UI_POPUP_CLAIM);
          setModalTitle("Task Claimed");
          setClaimSuccess(successList);
          setClaimFailed(null);
          setShowError(null);
          setErrorMessage([]);
          setSuccessMessage(null);
          //
        } else if (data.code === "400") {
          setPopUpType(null);
          setModalTitle("Execution Error");
          setClaimFailed(errorList);
          setErrorMessage(errorList);
          setShowError(true);
          //
        } else if (data.code === "206") {
          // console.log("success: ", successList, "error: ", errorList);

          setPopUpType(null);
          setModalTitle("Execution Error");
          setErrorMessage(errorList);
          setClaimFailed(errorList);
          setShowPartialError(errorList !== null);
          setShowError(errorList !== null);
          setSuccessMessage(successList);
          //
        } else {
          setModalTitle("Execution Error");
          setPopUpType(null);
          setShowError(true);
        }
      })
      .then(() => {
        setShowClaimModal(true);
        setOpenBackdropLoading(false);
      })
      .catch((error) => {
        console.error("Error Catch:", error);

        setPopUpType(null);
        setModalTitle("Execution Error");
        setShowError(true);
        setOpenBackdropLoading(false);
      })
      .finally(() => {
        // setIsLoading(false);
      });
    // }, 1500);
  };

  const GetDropdownStatus = () => {
    // setIsLoading(true);

    const path = "pool";
    const params = new URLSearchParams({
      "user-id": USERNAME,
      path: path,
    });

    // setTimeout(() => {
    TaskGlobalApi.GetStatusList(params)
      .then((response) => response.json())
      .then((data) => {
        const list = data.data;
        setDropdownStatus(list);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        // setIsLoading(false);
      });
    // }, 500);
  };

  const GetDropdownTaskType = () => {
    // setIsLoading(true);
    const path = "pool";

    const params = new URLSearchParams({
      "user-id": USERNAME,
      path: path,
    });

    // setTimeout(() => {
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
        // setIsLoading(false);
      });
    // }, 500);
  };
  //#endregion

  /* ==============================================================================================================================
                                                                 USE EFFECT
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

  //param for TableFilter.js
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

  let errorHandle = {
    // Modal Pop Out (components -> PopUpModal.js)
    popUpType: popUpType,
    modalTitle: modalTitle,
    modalText: getModalText(),
    isCheckboxListEmpty: isCheckboxListEmpty,
    handleClaimClick: handleClaimClick,
    handleModalClosed: handleModalClosed,
    showClaimModal: showClaimModal,

    // Error Pop Out (components -> ErrorPopUp.js)
    successMessageList: getSuccessMessageList(),
    errorMessageList: getErrorMessageList(),
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
          pageTitle="Task Pool"
          pageDescription="A dashboard for the task that is not assigned and available to claim by the user."
        />
        <TableFilter
          hasCheckbox={true}
          taskFilter={taskFilter}
          // Modal Pop Out
          showError={showError}
          errorHandle={errorHandle}
        />
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
          errorHandle={errorHandle}
          link="list"
          isLoading={isLoading}
          openBackdropLoading={openBackdropLoading}
          //checkbox
          hasCheckbox={true}
          handleIDCheckboxChange={handleIDCheckboxChange}
          handleRowCheckboxChange={handleRowCheckboxChange}
          selectedCheckbox={selectedCheckbox}
        />
      </Container>
    </Container>
  );
};

export default TaskPool;
