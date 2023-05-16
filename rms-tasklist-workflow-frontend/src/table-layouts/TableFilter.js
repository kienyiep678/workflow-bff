// reactstrap components
import {
  Container,
  Row,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Input,
} from "reactstrap";

import MultiSelect from "react-select"; //install -> yarn add react-select
import ErrorMessageWarning from "../components/Popups/ErrorMessageWarning.js";
import React, { Component } from "react";

const TableFilter = (props) => {
  const {
    selectedTask,
    handleTaskChange,
    selectedRefNo,
    handleRefNoChange,
    selectedStatus,
    handleStatusChange,
    dropdownStatus,
    dropdownTaskType,
    selectedTaskType,
    handleTaskTypeChange,
  } = props.taskFilter;

  const {
    popUpType,
    modalText,
    isCheckboxListEmpty,
    handleClaimClick,
    handleModalClosed,
    showClaimModal,
    showError,
    successMessageList,
    errorMessageList,
  } = props.errorHandle ?? {}; //check is null, because some pages no need error popup

  /* ==============================================================================================================================
                                                Dropdown Options
       ==============================================================================================================================
    */
  // set label and value from get api status
  const dropdownStageOptions = dropdownStatus.map((option) => ({
    value: option.stageCode,
    label: option.stageName,
  }));

  //add 'All' to display all data
  dropdownStageOptions.unshift({ value: "", label: "All" });

  // set label and value from get api task type
  const dropdownTaskTypeOptions = dropdownTaskType.map((option) => ({
    value: option.typeCode,
    label: option.typeName,
  }));

  //add 'All' to display all data
  dropdownTaskTypeOptions.unshift({ value: "", label: "All" });

  /* ==============================================================================================================================
                                                       CSS
       ==============================================================================================================================
    */
  const customStyles = {
    control: (provided) => ({
      ...provided,
      minHeight: "45px",
      height: "45px",
    }),
  };

  /* ==============================================================================================================================
                                                       RETURN
       ==============================================================================================================================
    */

  return (
    <>
      <div className="mt-5">
        {/*============== Title ==============*/}
        <div data-testid="show-error-message">
          {props.showError && (
            <Row style={{ margin: "5px", display: "flex" }}>
              <div className="col">
                <ErrorMessageWarning
                  // API Message
                  errorMessageList={errorMessageList}
                  successMessageList={successMessageList}
                />
              </div>
            </Row>
          )}
        </div>

        {/*============== Search ==============*/}
        <Row style={{ margin: "5px", display: "flex" }}>
          {/* Filter task name */}
          <div className="col-sm-4 col-md-2" data-testid="filter-task-name">
            <label>Filter by Task Name</label>
            <div>
              <InputGroup className="input-group input-group-rounded input-group-merge">
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <span className="fa fa-search" />
                  </InputGroupText>
                </InputGroupAddon>
                <Input
                  aria-label="Search"
                  className="form-control form-control-rounded form-control-prepended"
                  // placeholder="Search Task Name"
                  type="text"
                  value={selectedTask}
                  onChange={handleTaskChange}
                />
              </InputGroup>
            </div>
          </div>

          {/* Filter ref no */}
          <div className="col-sm-4 col-md-2" data-testid="filter-task-refNo">
            <label>Filter by Reference No.</label>

            <div>
              <InputGroup className="input-group input-group-rounded input-group-merge">
                <InputGroupAddon addonType="prepend">
                  <InputGroupText>
                    <span className="fa fa-search" />
                  </InputGroupText>
                </InputGroupAddon>
                <Input
                  aria-label="Search"
                  className="form-control form-control-rounded form-control-prepended"
                  // placeholder="Search Ref No"
                  type="text"
                  value={selectedRefNo}
                  onChange={handleRefNoChange}
                />
              </InputGroup>
            </div>
          </div>

          {/* Status Select */}
          <div className="col-sm-4 col-md-2" data-testid="filter-task-stage">
            <label>Filter by Stage</label>

            <MultiSelect
              // isMulti
              className="text-xs text-gray"
              styles={customStyles}
              placeholder="All"
              options={dropdownStageOptions}
              onChange={handleStatusChange}
              // value={dropdownStageOptions.find(
              //   (option) => option.stageName === selectedStatus
              // )}
            />
          </div>

          <div className="col-sm-4 col-md-2" data-testid="filter-task-type">
            <label>Filter by Task Type</label>

            <MultiSelect
              id="select-task-type"
              // isMulti
              className="text-xs text-gray"
              styles={customStyles}
              placeholder="All"
              options={dropdownTaskTypeOptions}
              onChange={handleTaskTypeChange}
              // value={dropdownTaskTypeOptions.find(
              //   (option) => option.typeName === selectedTaskType
              // )}
            />
          </div>
        </Row>
      </div>
    </>
  );
};

export default TableFilter;
