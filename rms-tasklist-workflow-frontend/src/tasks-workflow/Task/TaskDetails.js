// css
import "../../assets/css/rms-css/TaskDetail.css";

import { CardFooter, Col, Form, Row } from "reactstrap";

// MUI Components
import {
  Button,
  Card,
  CardHeader,
  CardContent,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Skeleton,
  Backdrop,
} from "@mui/material";
import CircularProgress from "@mui/material/CircularProgress";

import { createTheme } from "@mui/material/styles";
import SubdirectoryArrowLeftRoundedIcon from "@mui/icons-material/SubdirectoryArrowLeftRounded";

// core components
import AccordionComponent from "./Accordion.js";
import PopUpModal from "components/Popups/PopUpModal.js";
import ErrorMessageWarning from "components/Popups/ErrorMessageWarning.js";

// react components
import * as React from "react";
import { useState, useEffect, useRef } from "react";
import { useHistory } from "react-router-dom";

// JSON file
import {
  USERNAME,
  REDIRECT_URL_HOME,
  UI_POPUP_APPROVE,
  UI_POPUP_CLAIM,
  UI_POPUP_EXECUTE,
  UI_POPUP_RETURN_FROM_APPROVAL,
  UI_POPUP_RETURN_FROM_EXCEPTION,
  UI_POPUP_SUBMIT,
  UI_POPUP_SAVED,
  UI_POPUP_UPDATE,
  UI_POPUP_CANCEL,
  UI_POPUP_REJECT,
  UI_POPUP_CANCEL_FROM_EXCEPTION,
  UI_POPUP_DELETE,
  UI_MESSAGE_FAIL,
  UI_MESSAGE_ERROR_ROUTE,
} from "../../data/DataConst.js";
import TaskGlobalApi from "data/TaskGlobalApi.js";

const TaskDetails = () => {
  /* -------------------- useState -------------------- */
  const [display, setDisplay] = useState(false);

  const [taskInformation, setTaskInformation] = useState(null); // Task Information / Details
  const [name, setName] = useState(""); // Task Name
  const [description, setDescription] = useState(""); // Task Description
  const [remarks, setRemarks] = useState(""); // Task Remarks
  const [taskStage, setTaskStage] = useState(""); // Task Stage
  const [isCompleted, setIsCompleted] = useState(""); // Is Task Completed?

  const [dropdownOption, setDropdownOption] = useState(null);
  const [dropdownValue, setDropdownValue] = useState(""); // Dropdown for submission option
  const [submitStatus, setSubmitStatus] = useState(""); // Response status after submit

  const [isDisabled, setIsDisabled] = useState(true); // Set Button Disabled
  const [showModal, setShowModal] = useState(false); // Modal pop-up
  const [popUpType, setPopUpType] = useState(false); // Modal text
  const [modalTitle, setModalTitle] = useState(""); // Modal Title
  const [modalText, setModalText] = useState(""); // Modal text
  const [additionalModalNavigation, setAdditionalModalNavigation] =
    useState(""); // Extra Modal Navigator

  const [errorMessage, setErrorMessage] = useState("");

  const [saveButtonText, setSaveButtonText] = useState("Save");
  const [frameUrl, setFrameUrl] = useState("");

  //handle screen backdrop when click submit button
  const [openBackdropLoading, setOpenBackdropLoading] = useState(false);

  /* -------------------- Loading screen -------------------- */
  const handleBackdropOpen = () => {
    setOpenBackdropLoading(true);
  };

  const handleCombinedClick = () => {
    handleBackdropOpen();

    setTimeout(() => {
      onSubmitDetails();
    }, 1000);
  };

  /* -------------------- useRef -------------------- */
  const taskNameRef = useRef();
  const taskDescriptionRef = useRef();
  const taskRemarkRef = useRef();

  /* -------------------- useHistory -------------------- */
  const history = useHistory();

  /* -------------------- Get ID and Navigate From the Params -------------------- */
  const params = window.location.pathname;
  const detailsURL = params.split("/");
  const navigateFrom = detailsURL.pop();
  const taskIdentifier = detailsURL.pop();

  /* -------------------- Set Params based on Navigate From -------------------- */
  const getTaskDetailsParams = () => {
    return navigateFrom === "inquiry"
      ? `${taskIdentifier}?history=true`
      : `${taskIdentifier}`;
  };

  /* -------------------- Get Data from Back-end -------------------- */
  const getTaskDetails = () => {
    setTimeout(() => {
      TaskGlobalApi.GetTaskDetails(getTaskDetailsParams())
        .then((response) => response.json())
        .then((data) => {
          setTaskInformation(data.data);

          setName(data.data.taskName);
          setDescription(data.data.description);
          setRemarks(data.data.remarks);
          setTaskStage(data.data.stageName);
          setIsCompleted(data.data.endStage);

          data.data.taskUrlJsonObject.forEach((urlObj) => {
            if (urlObj.urlType === "VIEW_URL") setFrameUrl(urlObj.url);
          });

          if (!isCompleted) setDropdownOption(data.data.actionList);

          taskNameRef.current = data.data.taskName;
          taskDescriptionRef.current = data.data.description;
          taskRemarkRef.current = data.data.remarkMessage;
        });
    }, 1500);
  };

  /* -------------------- useEffect -------------------- */
  useEffect(() => {
    getTaskDetails();
  }, []);

  useEffect(() => {
    setDisplay(true);
  }, [taskInformation]);

  useEffect(() => {
    taskNameRef.current = name;
  }, [name]);

  useEffect(() => {
    taskDescriptionRef.current = description;
  }, [description]);

  useEffect(() => {
    taskRemarkRef.current = remarks;
  }, [remarks]);

  /* -------------------- Input Field Handler -------------------- */
  const handleNameChange = (value) => {
    setName(value);
    setSaveButtonText("Save");
  };

  const handleDescriptionChange = (value) => {
    setDescription(value);
    setSaveButtonText("Save");
  };

  const handleRemarkChange = (value) => {
    setRemarks(value);
    setSaveButtonText("Save");
  };

  const handleInputClick = (event) => {
    event.target.select();
  };

  /* -------------------- Get Value from Dropdown & Enable Submit Button -------------------- */
  const handleSelect = (event) => {
    setDropdownValue(event.target.value);
    setPopUpType(event.target.value);
    setIsDisabled(false);
  };

  /* -------------------- Modal Props -------------------- */
  const showPopUpModal = () => {
    setShowModal(true);
  };

  const closePopUpModal = () => {
    setShowModal(false);
    setOpenBackdropLoading(true);

    if (submitStatus === 200) {
      setTimeout(() => {
        history.push(REDIRECT_URL_HOME);
      }, 1000);
    }
  };

  const getExceptionLink = () => {
    return (
      <Button
        variant="text"
        endIcon={<SubdirectoryArrowLeftRoundedIcon />}
        onClick={() => {
          window.location.reload(true);
        }}
        sx={{
          textTransform: "none",
          marginBottom: "10px",
        }}
      >
        Refer the error details here
      </Button>
    );
  };

  /* -------------------- Set Modal Title -------------------- */
  const getModalTitle = () => {
    return dropdownValue === UI_POPUP_APPROVE
      ? "Task Approved"
      : dropdownValue === UI_POPUP_RETURN_FROM_APPROVAL ||
        dropdownValue === UI_POPUP_RETURN_FROM_EXCEPTION
      ? "Task Returned"
      : dropdownValue === UI_POPUP_EXECUTE
      ? "Task Executed"
      : dropdownValue === UI_POPUP_SUBMIT
      ? "Task Submitted"
      : dropdownValue === UI_POPUP_CANCEL ||
        dropdownValue === UI_POPUP_CANCEL_FROM_EXCEPTION
      ? "Task Cancelled"
      : "Task Rejected";
  };

  /* -------------------- Submit Response Body -------------------- */
  const getSubmitBodyResponse = () => {
    if (taskStage === "Task Creation") {
      return {
        userId: `${USERNAME}`,
        assignedUserId: `${USERNAME}`,
        taskId: taskInformation.taskId,
        taskName: name,
        taskDescription: description,
        action: dropdownValue,
      };
    } else {
      return {
        userId: `${USERNAME}`,
        assignedUserId: `${USERNAME}`,
        taskId: taskInformation.taskId,
        remarkMessage: remarks,
        action: dropdownValue,
      };
    }
  };

  /* -------------------- Save Response Body -------------------- */
  const getSaveBodyResponse = () => {
    if (taskStage === "Task Creation") {
      return {
        userId: `${USERNAME}`,
        taskId: taskInformation.taskId,
        taskName: name,
        taskDescription: description,
      };
    } else {
      return {
        userId: `${USERNAME}`,
        taskId: taskInformation.taskId,
        remarkMessage: remarks,
      };
    }
  };

  /* -------------------- Save Data. -------------------- */
  const onSaveDetails = async () => {
    try {
      if (!name) {
        window.location.href = "#task-name";
      } else {
        TaskGlobalApi.SaveTaskDetails(getSaveBodyResponse()).then(
          async (response) => {
            let data = await response.json();

            if (data.code === 200 || data.code === "200") {
              setSaveButtonText("Saved");
            }
          }
        );
      }
    } catch (error) {
      console.error("Error Catch: ", error);
    }
  };

  /* -------------------- Submit Data. -------------------- */
  const onSubmitDetails = async () => {
    setOpenBackdropLoading(true);

    try {
      if (!name) {
        window.location.href = "#task-name";
      } else {
        TaskGlobalApi.SubmitTaskDetails(getSubmitBodyResponse()).then(
          async (response) => {
            let data = await response.json();

            if (data.code === 200 || data.code === "200") {
              if (data.data.routeFlag === false) {
                setPopUpType(null);
                setModalTitle("Execution Error");
                setModalText(
                  `${taskInformation.taskReferenceNo}: ${UI_MESSAGE_ERROR_ROUTE}`
                );
                setAdditionalModalNavigation(getExceptionLink());
              } else {
                setModalTitle(getModalTitle());
                setModalText(
                  `${data.data.taskReferenceNo}: has been routed to ${data.data.taskStageName} stage.`
                );
              }

              setSubmitStatus(200);
            } else {
              setPopUpType(null);
              setModalTitle("Execution Error");
              setModalText(UI_MESSAGE_FAIL);
              setErrorMessage(
                `${taskInformation.taskReferenceNo}: ${data.message}`
              );
            }

            setOpenBackdropLoading(false);
            showPopUpModal();
          }
        );
      }
    } catch (error) {
      console.error("Error Catch: ", error);
      setPopUpType(null);
      setModalTitle("Execution Error");
      setModalText(UI_MESSAGE_FAIL);
      setErrorMessage(`${error}`);

      showPopUpModal();
    }
  };

  /* -------------------- Accordion Body -------------------- */
  const getAccordionBody = () => {
    return (
      <Row className="align-items-center">
        <Col xs="12">
          <AccordionComponent
            taskName={name}
            taskNameRef={taskNameRef}
            taskDescriptionRef={taskDescriptionRef}
            taskRemarkRef={taskRemarkRef}
            handleInputClick={handleInputClick}
            taskDetails={taskInformation}
            taskStage={taskStage}
            taskType={taskInformation.taskTypeName}
            taskHistory={
              navigateFrom === "inquiry" ? taskInformation.history : null
            }
            isCompleted={isCompleted}
            onRemarkChange={handleRemarkChange}
            onNameChange={handleNameChange}
            onDescriptionChange={handleDescriptionChange}
            navigateFrom={navigateFrom}
            frameUrl={frameUrl}
            onSaveDetails={onSaveDetails}
            saveButtonText={saveButtonText}
          />
        </Col>
      </Row>
    );
  };

  /* -------------------- Error Message Body -------------------- */
  const getErrorMessageBody = () => {
    return errorMessage ? (
      <Row style={{ paddingBottom: "0.5rem", display: "flex" }}>
        <div className="col">
          <ErrorMessageWarning
            successMessageList={null}
            errorMessageList={errorMessage}
          />
        </div>
      </Row>
    ) : null;
  };

  const containerTheme = createTheme({
    breakpoints: {
      values: {
        xs: 0,
        sm: 1080,
        md: 1440,
        lg: 1680,
        xl: 2440,
      },
    },
  });

  /* -------------------- If there's no data thru API, return loading screen. -------------------- */
  if (!taskInformation) {
    return (
      <Container
        className="mt-8 justify-content-center"
        maxWidth="xl"
        theme={containerTheme}
      >
        <Row>
          <Col className="order-xl-1" xl="12">
            <Card sx={{ m: 2 }} className="card-styling">
              <CardHeader
                className="header-styling"
                title={
                  <Skeleton
                    sx={{ bgcolor: "grey.800" }}
                    animation="wave"
                    height={15}
                    width="100%"
                  />
                }
              />
              <CardContent>
                <React.Fragment>
                  <Skeleton
                    variant="text"
                    sx={{ fontSize: "3rem" }}
                    style={{ marginBottom: 6 }}
                  />
                  <Skeleton
                    animation="wave"
                    height={15}
                    style={{ marginBottom: 6 }}
                  />
                  <Skeleton
                    variant="text"
                    sx={{ fontSize: "3rem" }}
                    style={{ marginBottom: 6 }}
                  />
                  <Skeleton animation="wave" height={15} />
                </React.Fragment>
              </CardContent>
            </Card>
          </Col>
        </Row>
      </Container>
    );
  }

  return (
    <>
      {/* -------------------- Page Content -------------------- */}
      <Container
        className="mt-5 justify-content-center"
        maxWidth="xl"
        theme={containerTheme}
      >
        <Form style={{ padding: "60px 15px" }}>
          <Row>
            <Col className="order-xl-1" xl="12">
              <Card sx={{ m: 2 }} className="card-styling">
                <CardHeader
                  className="header-styling"
                  title={
                    <Col xs="8">
                      <h3>Task Details</h3>
                    </Col>
                  }
                />

                {/* -------------------- Accordion & Error Messages -------------------- */}
                <CardContent>
                  {errorMessage && getErrorMessageBody()}
                  {getAccordionBody()}
                </CardContent>

                {/* -------------------- Submit Option & Button -------------------- */}
                <CardFooter
                  className="footer-styling"
                  style={{
                    display:
                      navigateFrom === "inquiry" || isCompleted ? "none" : true,
                  }}
                >
                  <Row lg="12">
                    <Col
                      style={{ display: "flex", justifyContent: "flex-end" }}
                    >
                      <FormControl sx={{ m: 1, minWidth: 200 }} size="small">
                        <InputLabel id="select-option">
                          Select Option
                        </InputLabel>
                        <Select
                          labelId="select-option"
                          id="select-option"
                          label="Select Option"
                          defaultValue={dropdownValue}
                          onChange={handleSelect}
                          MenuProps={{
                            anchorOrigin: {
                              vertical: "bottom",
                              horizontal: "left",
                            },
                            transformOrigin: {
                              vertical: "top",
                              horizontal: "left",
                            },
                            getContentAnchorEl: null,
                          }}
                        >
                          {!isCompleted
                            ? dropdownOption.map((select) => {
                                return (
                                  <MenuItem
                                    key={select.actionValue}
                                    value={select.actionValue}
                                  >
                                    {select.actionName}
                                  </MenuItem>
                                );
                              })
                            : null}
                        </Select>
                      </FormControl>
                    </Col>

                    <Col className="text-right" lg="2">
                      <FormControl sx={{ m: 1 }} size="small">
                        <Button
                          className="btn"
                          variant="contained"
                          color="success"
                          size="large"
                          disabled={isDisabled}
                          onClick={handleCombinedClick}
                          sx={{
                            textTransform: "none",
                            color: "white",
                            bgcolor: "#88BC4A",
                            "&:hover": {
                              color: "white",
                            },
                          }}
                        >
                          Submit
                        </Button>

                        {openBackdropLoading ? (
                          <Backdrop
                            sx={{
                              color: "#fff",
                              zIndex: (theme) => theme.zIndex.drawer + 1,
                            }}
                            open={true}
                          >
                            <CircularProgress color="inherit" />
                          </Backdrop>
                        ) : null}
                      </FormControl>

                      {/* -------------------- Pop Up -------------------- */}
                      <PopUpModal
                        popUpType={popUpType}
                        closePopUpModal={closePopUpModal}
                        showModal={showModal}
                        modalTitle={modalTitle}
                        modalText={modalText}
                        additionalModalNavigation={additionalModalNavigation}
                      />
                    </Col>
                  </Row>
                </CardFooter>
              </Card>
            </Col>
          </Row>
        </Form>
      </Container>
    </>
  );
};

export default TaskDetails;
