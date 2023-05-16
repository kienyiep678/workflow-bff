/*!

=========================================================
* Argon Dashboard React - v1.2.2
=========================================================

* Product Page: https://www.creative-tim.com/product/argon-dashboard-react
* Copyright 2022 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/argon-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import React, { useRef } from "react";
import { useEffect } from "react";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";
// react component that copies the given text inside your clipboard
import { CopyToClipboard } from "react-copy-to-clipboard";
import { Skeleton, Button, Backdrop } from "@mui/material";
import CircularProgress from "@mui/material/CircularProgress";
import "./Slider.css";
import Switch from "./Slider.js";
import PopUpModal from "../../components/Popups/PopUpModal.js";
import ConfirmModal from "./Confirmation.js";
// reactstrap components
import {
  Card,
  CardHeader,
  CardBody,
  Container,
  Form,
  FormGroup,
  Input,
  Row,
  Col,
  UncontrolledTooltip,
} from "reactstrap";
// core components
import TextField from "@mui/material/TextField";
import PageTitle from "../../table-layouts/PageTitle";
import { REDIRECT_URL_TaskTypeSetting } from "../../data/DataConst";
import TaskGlobalApi from "data/TaskGlobalApi.js";
import {
  UI_POPUP_UPDATE,
  UI_POPUP_DELETE,
  UI_POPUP_CONFIRM,
} from "../../data/DataConst.js";

export default function WorkflowTaskDetail() {
  //id from url
  const [url, setUrl] = useState("");
  useState(() => {
    setUrl(window.location.href);
  }, []);

  const getId = url.split("/").pop();
  const [taskName, setTaskName] = useState("");
  const [taskTypeCode, setTaskTypeCode] = useState("");
  const [taskDescription, setTaskDescription] = useState("");
  const [numberOfApproval, setNumberOfApproval] = useState("");
  const [updatedBy, setUpdatedBy] = useState("");
  const [dateUpdated, setDateUpdated] = useState("");
  const [createdBy, setCreatedBy] = useState("");
  const [dateCreated, setDateCreated] = useState("");
  const [active, setActive] = useState();
  const [loading, setLoading] = useState(true);

  const [showModal, setShowModal] = useState(false); // Modal pop-up
  const [showConfirmModal, setConfirmShowModal] = useState(false); // Modal pop-up
  const [popUpType, setPopUpType] = useState(false); // Modal text
  const [modalText, setModalText] = useState(""); // Modal text
  const [submitStatus, setSubmitStatus] = useState("");
  const [modalTitle, setModalTitle] = useState("");
  const [openBackdropLoading, setOpenBackdropLoading] = useState(false);
  const history = useHistory();

  //loading screen
  const handleBackdropOpen = () => {
    setOpenBackdropLoading(true);
  };
  const handleCombinedClick = (e) => {
    handleBackdropOpen();

    setTimeout(() => {
      submit(e);
    }, 1000);
  };

  const handleDeleteClick = (e) => {
    handleBackdropOpen();

    setTimeout(() => {
      closePopUpConfirmModal(e);
    }, 1000);
  };

  const showPopUpConfirmModal = () => {
    setConfirmShowModal(true);
  };

  const closeCancelPopUpConfirmModal = () => {
    setConfirmShowModal(false);
  };

  const closePopUpConfirmModal = (e) => {
    setConfirmShowModal(false);
    e.preventDefault();
    TaskGlobalApi.DeleteTask(getId).then(async (response) => {
      let data = await response.json();
      if (data.code === "200") {
        setModalText("Task type has susscessfully deleted");
        setSubmitStatus(200);
        setPopUpType(UI_POPUP_DELETE);
      } else {
        setPopUpType(null);
        setModalText("Data delete fail");
      }
      setOpenBackdropLoading(false);
      showPopUpModal();
    });
  };

  const showPopUpModal = () => {
    setShowModal(true);
  };

  const closePopUpModal = () => {
    setShowModal(false);

    if (submitStatus === 200) {
      history.push(REDIRECT_URL_TaskTypeSetting);
    }
  };

  useEffect(() => {
    TaskGlobalApi.GetTaskSettingDetail(getId)
      .then((res) => res.json())
      .then((data) => {
        setTaskName(data.data.taskName);
        setTaskTypeCode(data.data.taskTypeCode);
        setTaskDescription(data.data.taskDescription);
        setNumberOfApproval(data.data.numberOfApproval);
        setUpdatedBy(data.data.updatedBy);
        setDateUpdated(data.data.dateUpdated);
        setCreatedBy(data.data.createdBy);
        setDateCreated(data.data.dateCreated);
        setActive(data.data.active);
      });
  }, []);

  function submit(e) {
    e.preventDefault();
    setOpenBackdropLoading(true);
    try {
      if (
        taskName === "" ||
        taskName === null ||
        numberOfApproval === "" ||
        numberOfApproval === null
      ) {
        setOpenBackdropLoading(false);
      } else {
        const getUpdateDetail = () => {
          return {
            taskTypeCode: taskTypeCode,
            taskName: taskName,
            taskDescription: taskDescription,
            numberOfApproval: numberOfApproval,
            active: active,
          };
        };
        TaskGlobalApi.UpdateTask(getUpdateDetail()).then(async (response) => {
          let data = await response.json();
          if (data.code === "200") {
            setModalText("Task type has susscessfully updated");
            setSubmitStatus(200);
            setPopUpType(UI_POPUP_UPDATE);
          } else {
            setPopUpType(null);
            setModalText("Data update fail");
          }
          setOpenBackdropLoading(false);
          showPopUpModal();
        });
      }
    } catch (error) {
      console.error("Error Catch: ", error);
    }
  }

  const goHome = () => {
    history.push(REDIRECT_URL_TaskTypeSetting);
  };

  const handleConfirm = (e) => {
    setPopUpType(UI_POPUP_CONFIRM);
    showPopUpConfirmModal();
  };

  // const handleDelete = (e) => {
  //   e.preventDefault();
  //   TaskGlobalApi.DeleteTask(getId)
  //     .then(async (response) => {
  //       let data = await response.json();
  //       if (data.code === "200") {
  //         setModalText("Task type has susscessfully deleted");
  //         setSubmitStatus(200);
  //         setPopUpType(UI_POPUP_DELETE);
  //       } else {
  //         setPopUpType(null);
  //         setModalText(
  //           "Data delete fail"
  //         );
  //       }
  //       showPopUpModal();
  //     })
  // };

  const handleNumber = (e) => {
    const regex = /^[0-9\b]+$/;
    if (e.target.value === "" || regex.test(e.target.value)) {
      setNumberOfApproval(e.target.value);
    }
  };

  useEffect(() => {
    setTimeout(() => {
      setLoading(false);
    }, 1000);
  }, []);

  return (
    <>
      <Container fluid>
        {/* Page content */}
        <Container
          style={{
            marginTop: "50px",
            color: "white",
          }}
          fluid
        >
          <PageTitle
            pageTitle="Task Type Detail"
            pageDescription="Configuration for task type detail."
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
          {/* Table */}
          <Row>
            <div className="col">
              <Form onSubmit={(e) => submit(e)}>
                <Row>
                  <Col className="text-right" lg="12" style={{ marginTop: 45 }}>
                    <FormGroup>
                      {/* switch button */}
                      <div className="app">
                        <Switch
                          isOn={active}
                          handleToggle={() => setActive(!active)}
                        />
                      </div>
                      {/* END switch button */}
                    </FormGroup>
                  </Col>
                </Row>
                <Row>
                  <Col lg="6">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          error={!taskName}
                          helperText={
                            !taskName ? "This Field is Mandatory" : ""
                          }
                          id="taskName"
                          type="taskName"
                          name="taskName"
                          label="Task Type Name"
                          variant="outlined"
                          value={taskName}
                          onChange={(e) => setTaskName(e.target.value)}
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                  <Col lg="6">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          id="taskTypeCode"
                          type="taskTypeCode"
                          name="taskTypeCode"
                          label="Task Type Code"
                          variant="outlined"
                          value={taskTypeCode}
                          disabled
                          onChange={(e) => setTaskTypeCode(e.target.value)}
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30 }}>
                  <Col lg="12">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          id="taskDescription"
                          type="taskDescription"
                          name="taskDescription"
                          label="Task Type Description"
                          multiline
                          rows={2}
                          value={taskDescription}
                          onChange={(e) => setTaskDescription(e.target.value)}
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30 }}>
                  <Col lg="2">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          error={!numberOfApproval}
                          helperText={
                            !numberOfApproval ? "This Field is Mandatory" : ""
                          }
                          id="numberOfApproval"
                          type="numberOfApproval"
                          name="numberOfApproval"
                          label="Number of Approver"
                          variant="outlined"
                          onChange={handleNumber}
                          value={numberOfApproval}
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30 }}>
                  <Col lg="6">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          id="updatedBy"
                          type="updatedBy"
                          name="updatedBy"
                          label="Updated By"
                          variant="outlined"
                          value={updatedBy}
                          disabled
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                  <Col lg="6">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          id="dateUpdated"
                          type="dateUpdated"
                          name="dateUpdated"
                          label="Date Updated"
                          variant="outlined"
                          value={
                            dateUpdated
                              ? new Date(dateUpdated).toLocaleString()
                              : dateUpdated
                          }
                          disabled
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30 }}>
                  <Col lg="6">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          id="createdBy"
                          type="createdBy"
                          name="createdBy"
                          label="Created By"
                          variant="outlined"
                          value={createdBy}
                          disabled
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                  <Col lg="6">
                    <FormGroup>
                      {loading ? (
                        <Skeleton
                          variant="rectangular"
                          fullwidth="true"
                          height={50}
                          animation="wave"
                        />
                      ) : (
                        <TextField
                          id="dateCreated"
                          type="dateCreated"
                          name="dateCreated"
                          label="Date Created"
                          variant="outlined"
                          value={
                            dateCreated
                              ? new Date(dateCreated).toLocaleString()
                              : dateCreated
                          }
                          disabled
                          fullWidth
                        />
                      )}
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30, marginBottom: 30 }}>
                  <Col className="text-left" xs="10">
                    <FormGroup>
                      <Button
                        sx={{
                          color: "white",
                          bgcolor: "#A91E1E",
                          "&:hover": {
                            bgcolor: "#A91E1E",
                          },
                        }}
                        type="button"
                        onClick={handleConfirm}
                      >
                        Delete This Task
                      </Button>
                    </FormGroup>
                  </Col>
                  <Col className="text-right" xs="1">
                    <FormGroup>
                      <Button
                        sx={{
                          color: "white",
                          bgcolor: "#88BC4A",
                          "&:hover": {
                            bgcolor: "#88BC4A",
                          },
                        }}
                        fullWidth
                        type="button"
                        onClick={handleCombinedClick}
                      >
                        Update
                      </Button>
                    </FormGroup>
                  </Col>
                  <Col xs="1" className="text-right">
                    <FormGroup>
                      <Button
                        sx={{
                          color: "white",
                          bgcolor: "black",
                          "&:hover": {
                            bgcolor: "black",
                          },
                        }}
                        fullWidth
                        type="button"
                        onClick={(e) => goHome()}
                      >
                        Cancel
                      </Button>
                    </FormGroup>
                  </Col>
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
                  <PopUpModal
                    popUpType={popUpType}
                    closePopUpModal={closePopUpModal}
                    showModal={showModal}
                    modalTitle={modalTitle}
                    modalText={modalText}
                  />
                  <ConfirmModal
                    popUpType={popUpType}
                    handleDeleteClick={handleDeleteClick}
                    closeCancelPopUpConfirmModal={closeCancelPopUpConfirmModal}
                    showConfirmModal={showConfirmModal}
                  />
                </Row>
              </Form>
            </div>
          </Row>
        </Container>
      </Container>
    </>
  );
}
