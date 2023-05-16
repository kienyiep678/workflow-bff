import React, { Component } from "react";

// reactstrap components
import { Badge } from "reactstrap";
import { Link } from "react-router-dom";
import PaginationTable from "./Pagination";
import {
  Button,
  Skeleton,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableBody,
  TableCell,
  Typography,
} from "@mui/material";

import { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";

import PopUpModal from "../components/Popups/PopUpModal";

import TableSkeletonLoader from "./TableSkeletonLoader.js";
import BackdropLoader from "./BackdropLoader.js";

import { REDIRECT_URL_TASKDETAILS, DELOITTE_GREEN } from "../data/DataConst.js";

const TableDisplay = (props) => {
  const {
    tableData,
    currentPage,
    totalPages,
    handlePageChange,
    handleSortOrder,
  } = props.taskDisplay;

  const {
    handleModalClosed,
    showClaimModal,
    popUpType,
    modalTitle,
    modalText,
    isCheckboxListEmpty,
    handleClaimClick,
    showError,
    successMessageList,
    errorMessageList,
  } = props.errorHandle ?? {}; //check is null, because some pages no need error popup

  const [showTimeoutMessage, setShowTimeoutMessage] = useState(false);

  const [openBackdrop, setOpenBackdrop] = useState(false);
  const history = useHistory();

  /* ==============================================================================================================================
                                                         Handle loading screen
       ==============================================================================================================================
  */
  //#region

  // Backdrop - loading screen
  const handleOpenBackdrop = () => {
    setOpenBackdrop(true);
  };

  // Task name onclick - show loading
  const setLinkPath = (id, name, link) => (event) => {
    event.preventDefault();
    setOpenBackdrop(true);

    setTimeout(() => {
      history.push(
        `${REDIRECT_URL_TASKDETAILS.replace(":taskid", id).replace(
          ":link",
          link
        )}`
      );
    }, 1000);
  };

  // Onclick Claim button
  const handleCombinedClick = () => {
    handleClaimClick();
    handleOpenBackdrop();
  };

  //#endregion

  /* ==============================================================================================================================
                                                          USE EFFECT
       ==============================================================================================================================
  */
  useEffect(() => {
    const timeout = setTimeout(() => {
      setShowTimeoutMessage(true);
    }, 3000);

    return () => clearTimeout(timeout);
  }, []);

  /* ==============================================================================================================================
                                                           CSS
       ==============================================================================================================================
  */
  //#region
  //for table header
  const headerRowStyle = {
    paddingTop: "10px",
    paddingBottom: "10px",
    fontWeight: "bold",
    verticalAlign: "middle",
    textAlign: "left",
    // border: "1px solid #e4e4e4",
  };

  //for each row style
  const rowStyle = {
    paddingTop: "10px",
    paddingBottom: "10px",
    // border: "1px solid #e4e4e4",
  };
  //#endregion

  /* ==============================================================================================================================
                                                        SKELETON FUNCTION
       ==============================================================================================================================
  */
  //#region

  let tableRows;
  if (props.hasCheckbox) {
    tableRows = (
      <TableSkeletonLoader
        rowsNum={11}
        colsNum={7}
        hasCheckbox={true}
        key="table-rows-with-checkbox"
      />
    );
  } else {
    tableRows = (
      <TableSkeletonLoader
        rowsNum={11}
        colsNum={6}
        hasCheckbox={false}
        key="table-rows-without-checkbox"
      />
    );
  }

  //#endregion
  /* ==============================================================================================================================
                                                          RETURN
       ==============================================================================================================================
  */

  return (
    <TableContainer>
      {props.hasCheckbox && (
        <div className="mt-3 mr-3 d-flex justify-content-end">
          <Button
            variant="contained"
            color="success"
            size="large"
            disabled={isCheckboxListEmpty}
            onClick={handleCombinedClick}
            sx={{
              textTransform: "none",
              color: "white",
              bgcolor: DELOITTE_GREEN,
              "&:hover": {
                color: "white",
              },
            }}
          >
            Claim
          </Button>

          {props.openBackdropLoading ? <BackdropLoader openBackdrop /> : null}

          <PopUpModal
            popUpType={popUpType}
            closePopUpModal={handleModalClosed}
            showModal={showClaimModal}
            // Modal Text
            modalTitle={modalTitle}
            modalText={modalText}
          />
        </div>
      )}

      {/* Table */}
      <Table
        style={{
          marginTop: "20px",
        }}
      >
        {/*   
             ===================== 
                Table Header
            ===================== */}
        <TableHead
          style={{
            backgroundColor: "white",
            color: "gray",
          }}
        >
          <TableRow>
            {props.hasCheckbox && <TableCell></TableCell>}
            <TableCell style={headerRowStyle}>Reference No.</TableCell>
            <TableCell style={headerRowStyle}>Task Name</TableCell>
            <TableCell style={headerRowStyle}>Task Type</TableCell>
            <TableCell style={headerRowStyle}>Stage</TableCell>
            <TableCell style={headerRowStyle}>Creator</TableCell>
            <TableCell style={headerRowStyle}>
              Modified Date
              <button
                style={{
                  backgroundColor: "transparent",
                  border: "none",
                  marginLeft: "20px",
                }}
                onClick={handleSortOrder}
              >
                ↑↓
              </button>
            </TableCell>
          </TableRow>
        </TableHead>

        {/* 
             ===================== 
                Table Body
            ===================== */}
        <TableBody data-testid="skeleton-loading">
          {!props.isLoading && tableData.length !== null ? (
            <>
              {tableData.map((task, index) => (
                <TableRow key={index}>
                  {props.hasCheckbox && (
                    <TableCell
                      style={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                      }}
                    >
                      <input
                        style={{ ...rowStyle, transform: "scale(1.5)" }}
                        type="checkbox"
                        onChange={(event) =>
                          props.handleRowCheckboxChange(event, task.taskId)
                        }
                        checked={props.selectedCheckbox.includes(task.taskId)}
                      />
                    </TableCell>
                  )}

                  <TableCell style={rowStyle}>
                    <span className="">{task.taskReferenceNo}</span>
                    {/* <span className="">{task.taskId}</span> */}
                  </TableCell>
                  {/* 
                    
                      ===================== 
                          Task Name
                      ===================== */}

                  <TableCell style={rowStyle}>
                    <span>
                      {!props.hasCheckbox ? (
                        // <Link
                        //   to={`${REDIRECT_URL_TASKDETAILS.replace(
                        //     ":taskid",
                        //     task.taskId
                        //   ).replace(":link", props.link)}`}
                        //   style={{
                        //     color: "#88BC4A",
                        //   }}
                        // >
                        //   {task.taskName}
                        // </Link>
                        <Link
                          to="#"
                          style={{
                            color: DELOITTE_GREEN,
                          }}
                          onClick={setLinkPath(
                            task.taskId,
                            task.taskName,
                            props.link
                          )}
                        >
                          {task.taskName}
                          {openBackdrop ? (
                            <BackdropLoader openBackdrop />
                          ) : null}
                        </Link>
                      ) : (
                        <span>{task.taskName}</span>
                      )}
                    </span>
                  </TableCell>
                  {/* 
                     
                      ===================== 
                          Task Type
                      ===================== */}
                  <TableCell style={rowStyle}>
                    <span className="tasktype-group">{task.taskTypeName}</span>
                  </TableCell>

                  {/* 
                     
                      ===================== 
                          Task Status
                      ===================== */}
                  <TableCell style={rowStyle}>
                    <Badge className="badge-dot mr-4">
                      {/* 
                       bg-light - pending
                       bg-success - completed
                       bg-danger - rejected
                       */}

                      {task.stageName === "Pending Approval" ||
                      task.stageName === "Task Creation" ? (
                        <i className="bg-light" />
                      ) : task.stageName === "Task Completed" ? (
                        <i className="bg-success" />
                      ) : task.stageName === "Task Rejected" ? (
                        <i style={{ backgroundColor: "#f1c232" }} />
                      ) : task.stageName === "Process Exception" ||
                        task.stageName === "Task Cancelled" ? (
                        <i className="bg-danger" />
                      ) : null}
                      <span className="text-dark">{task.stageName}</span>
                    </Badge>
                  </TableCell>

                  {/* 
                     
                      ===================== 
                          Task Creater
                      ===================== */}
                  <TableCell style={rowStyle}>
                    <span className="creater-group">{task.createdBy}</span>
                  </TableCell>

                  {/* 
                     
                      ===================== 
                          Task Date
                      ===================== */}
                  <TableCell style={rowStyle}>
                    <span>{new Date(task.dateUpdated).toLocaleString()}</span>
                  </TableCell>
                </TableRow>
              ))}
            </>
          ) : (
            [tableRows]
          )}
        </TableBody>
      </Table>

      {/* If Empty Table */}
      {showTimeoutMessage && tableData.length === 0 && !props.isLoading && (
        <Typography
          style={{
            display: "flex",
            justifyContent: "center",
            color: "gray",
            marginTop: "20px",
          }}
        >
          No data available
        </Typography>
      )}

      {/* Pagination */}
      <div
        style={{
          marginTop: "20px",
          marginLeft: "20px",
          justifyContent: "start",
        }}
      >
        Total Pages: {totalPages}
        <PaginationTable
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
        />
      </div>
    </TableContainer>
  );
};

export default TableDisplay;
