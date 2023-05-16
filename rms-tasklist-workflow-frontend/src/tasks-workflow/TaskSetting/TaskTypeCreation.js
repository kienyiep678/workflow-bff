import React, { useRef } from "react";
import { useEffect } from "react";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";
// react component that copies the given text inside your clipboard
import { CopyToClipboard } from "react-copy-to-clipboard";
import TextField from "@mui/material/TextField";
import PageTitle from "../../table-layouts/PageTitle";
import PopUpModal from "../../components/Popups/PopUpModal.js";
import CircularProgress from "@mui/material/CircularProgress";
import { REDIRECT_URL_TaskTypeSetting } from "../../data/DataConst";
import TaskGlobalApi from "data/TaskGlobalApi.js";
import { UI_POPUP_SAVED } from "../../data/DataConst.js";

import { Button, Backdrop } from "@mui/material";

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
} from "reactstrap";
// core components

export default function TaskCreation() {
  const [taskName, setTaskName] = useState("");
  const [taskTypeCode, setTaskTypeCode] = useState("");
  const [taskDescription, setTaskDescription] = useState("");
  const [numberOfApproval, setNumberOfApproval] = useState("");
  const [showModal, setShowModal] = useState(false); // Modal pop-up
  const [popUpType, setPopUpType] = useState(false); // Modal text
  const [modalText, setModalText] = useState(""); // Modal text
  const [submitStatus, setSubmitStatus] = useState("");
  const [modalTitle, setModalTitle] = useState("");
  const [hideValidation, setHideValidation] = useState(true);
  const [openBackdropLoading, setOpenBackdropLoading] = useState(false);
  const history = useHistory();

  const handleBackdropOpen = () => {
    setOpenBackdropLoading(true);
  };
  const handleCombinedClick = (e) => {
    handleBackdropOpen();

    setTimeout(() => {
      submit(e);
    }, 1000);
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

  function submit(e) {
    e.preventDefault();
    setOpenBackdropLoading(true);
    try {
      if (
        taskTypeCode === "" ||
        taskTypeCode === null ||
        taskName === "" ||
        taskName === null ||
        numberOfApproval === "" ||
        numberOfApproval === null
      ) {
        setHideValidation(false);
        setOpenBackdropLoading(false);
      } else {
        const getSubmitNewTask = () => {
          return {
            taskTypeCode: taskTypeCode,
            taskName: taskName,
            taskDescription: taskDescription,
            numberOfApproval: numberOfApproval,
          };
        };
        TaskGlobalApi.SaveNewTask(getSubmitNewTask()).then(async (response) => {
          let data = await response.json();
          if (data.code === "200") {
            setModalText("Task type has susscessfully created");
            setSubmitStatus(200);
            setPopUpType(UI_POPUP_SAVED);
          } else {
            setPopUpType(null);
            setModalText("Data saved fail, Task Code might be available!");
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

  const handleNumber = (e) => {
    const regex = /^[0-9\b]+$/;
    if (e.target.value === "" || regex.test(e.target.value)) {
      setNumberOfApproval(e.target.value);
    }
  };

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
            pageTitle="Task Type Creation"
            pageDescription="Creating new task type."
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
          <Row className="mt-5">
            <div className="col" style={{ marginBottom: "15px" }}>
              <Form onSubmit={(e) => submit(e)}>
                <Row>
                  <Col lg="6" style={{ marginTop: 45 }}>
                    <FormGroup>
                      <TextField
                        error={!hideValidation && !taskName}
                        helperText={
                          hideValidation ? "" : "This Field is mandatory"
                        }
                        id="taskName"
                        type="taskName"
                        name="taskName"
                        label="Task Type Name"
                        variant="outlined"
                        fullWidth
                        onChange={(e) => setTaskName(e.target.value)}
                        data-testid="taskname-inputtest"
                      />
                    </FormGroup>
                  </Col>
                  <Col lg="6" style={{ marginTop: 45 }}>
                    <FormGroup>
                      <TextField
                        error={!hideValidation && !taskTypeCode}
                        helperText={
                          hideValidation ? "" : "This Field is mandatory"
                        }
                        id="taskTypeCode"
                        type="taskTypeCode"
                        name="taskTypeCode"
                        label="Task Type Code"
                        variant="outlined"
                        fullWidth
                        onChange={(e) => setTaskTypeCode(e.target.value)}
                      />
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30 }}>
                  <Col lg="12">
                    <FormGroup>
                      <TextField
                        id="taskDescription"
                        type="taskDescription"
                        name="taskDescription"
                        label="Task Type Description"
                        multiline
                        rows={2}
                        onChange={(e) => setTaskDescription(e.target.value)}
                        fullWidth
                      />
                    </FormGroup>
                  </Col>
                </Row>
                <Row style={{ marginTop: 30 }}>
                  <Col lg="2">
                    <FormGroup>
                      <TextField
                        error={!hideValidation && !numberOfApproval}
                        helperText={
                          hideValidation
                            ? ""
                            : "This Field is Mandatory and Must be Number"
                        }
                        id="numberOfApproval"
                        type="numberOfApproval"
                        name="numberOfApproval"
                        label="Number of Approver"
                        variant="outlined"
                        onChange={handleNumber}
                        fullWidth
                      />
                    </FormGroup>
                  </Col>
                </Row>
                <Row>
                  <Col xs="10"></Col>
                  <Col className="text-right" xs="1">
                    <Button
                      sx={{
                        color: "white",
                        bgcolor: "#88BC4A",
                        "&:hover": {
                          bgcolor: "#88BC4A",
                        },
                      }}
                      type="button"
                      fullWidth
                      onClick={handleCombinedClick}
                      data-testid="test-submit"
                    >
                      Save
                    </Button>
                  </Col>
                  <Col xs="1" className="text-right">
                    <Button
                      sx={{
                        color: "white",
                        bgcolor: "black",
                        "&:hover": {
                          bgcolor: "black",
                        },
                      }}
                      type="button"
                      fullWidth
                      onClick={(e) => goHome()}
                      data-testid="test-cancel"
                    >
                      Cancel
                    </Button>
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
                </Row>
              </Form>
            </div>
          </Row>
        </Container>
      </Container>
    </>
  );
}
