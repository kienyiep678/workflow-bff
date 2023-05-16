// CSS
import "../../assets/css/rms-css/Accordion.css";

// react-stap component
import { Col, FormGroup, Row } from "reactstrap";

// MUI Components
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import SaveAsRoundedIcon from '@mui/icons-material/SaveAsRounded';

import Accordion from "@mui/material/Accordion";
import AccordionDetails from "@mui/material/AccordionDetails";
import AccordionSummary from "@mui/material/AccordionSummary";
import Button from '@mui/material/Button';
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";

import { styled } from '@mui/material/styles';

// react component
import React, { useState, useEffect } from "react";

const AccordionComponent = (props) => {
  /* -------------------- Object Destructuring -------------------- */
  const {
    taskName,
    taskNameRef,
    taskDescriptionRef,
    taskRemarkRef,
    handleInputClick,

    taskDetails,
    taskStage,
    taskType,
    taskHistory,

    onNameChange,
    onDescriptionChange,
    onRemarkChange,
    navigateFrom,
    frameUrl,
    onSaveDetails,
    saveButtonText,
  } = props;

  /* -------------------- Remark Input Handler -------------------- */
  const nameHandler = (event) => {
    onNameChange(event.target.value);
  };

  const descriptionHandler = (event) => {
    onDescriptionChange(event.target.value);
  };

  const remarkHandler = (event) => {
    onRemarkChange(event.target.value);
  };

  /* -------------------- Accordion Handler -------------------- */
  const [expanded, setExpanded] = useState("panel1");

  const handleChange = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : false);
  };

  /* -------------------- iFrame Handler -------------------- */
  const [iframeUrl, setIframeUrl] = useState("");

  useEffect(() => {
    setIframeUrl(frameUrl);
  }, [frameUrl]);

  const handleLoad = () => {
    const urlRegex = new RegExp('^(ftp|http|https)://[^ "]+$');
    const iframe = document.getElementById("liveDetail");
    const url = iframe.getAttribute("src");

    if (iframeUrl) {
      if (!urlRegex.test(url)) {
        console.log("Invalid URL");
        setIframeUrl("");
      }
    }
  };

  /* -------------------- Table Styler -------------------- */
  const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#696969",
      color: theme.palette.common.white,
      fontSize: 16,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 16,
    },
  }));

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.action.hover,
    },
    // hide last border
    '&:last-child td, &:last-child th': {
      border: 0,
    },
  }));

  return (
    
    <div>
      {/* -------------------- Task Information -------------------- */}
      <Accordion
        className="accordion"
        expanded={expanded === "panel1"}
        onChange={handleChange("panel1")}
      >
        <AccordionSummary
          className="accordion-summary"
          aria-controls="panel1bh-content"
          id="panel1bh-header"
          expandIcon={<ExpandMoreIcon sx={{ color: "white" }} />}
          sx={{
            backgroundColor: "black",
            color: "white",
            border: "1px solid",
            borderRadius: "8px",
          }}
        >
          <Typography sx={{ width: "33%", flexShrink: 0 }}>
            Task Information
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <div className="pl-lg-4 accordion-content">
            <Row>
              <Col lg="12" style={{display: "flex", justifyContent: "flex-end"}}>
                <FormGroup>
                  <Button
                    variant="outlined"
                    startIcon={saveButtonText === "Save" ? <SaveAsRoundedIcon /> : <CheckCircleRoundedIcon/>}
                    size="large"
                    onClick={onSaveDetails}
                    style={{
                      display: 
                        (taskStage === "Pending Approval" || taskStage === "Task Creation") && navigateFrom !== "inquiry" ?
                          true : 'none'
                    }}
                    sx={{
                      textTransform: 'none',
                      color: 'white',
                      bgcolor: saveButtonText === "Save" ? '#404040' : "#88BC4A",
                      '&:hover': {
                        bgcolor: '#707070',
                      },
                    }}
                  >
                    {saveButtonText}
                  </Button>
                </FormGroup>
              </Col>
            </Row>

            <Row>
              {/* Task Name */}
              <Col lg="8">
                <FormGroup>
                  <TextField
                    required
                    error={!taskName ? true : false}
                    fullWidth
                    size="small"
                    id="task-name"
                    label="Task Name"
                    helperText="This field cannot be blank."
                    onChange={nameHandler}
                    onFocus={handleInputClick}
                    ref={taskNameRef}
                    defaultValue={taskNameRef.current}
                    disabled={
                      taskStage === "Task Creation" && navigateFrom === "list"
                        ? false
                        : true
                    }
                    sx={{ m: 1 }}
                    FormHelperTextProps={{
                      style: {
                        margin: "0 10px",
                        color: "red",
                        display:
                        !taskName ? true : "none",
                      },
                    }}
                  />
                </FormGroup>
              </Col>

              {/* Task Status */}
              <Col lg="4">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-reference-no"
                    label="Reference No"
                    defaultValue={taskDetails.taskReferenceNo}
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            <Row className="accordion-input-row">
              {/* Task Description */}
              <Col lg="12">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-description"
                    label="Description"
                    onChange={descriptionHandler}
                    onFocus={handleInputClick}
                    ref={taskDescriptionRef}
                    defaultValue={taskDescriptionRef.current}
                    disabled={
                      taskStage === "Task Creation" && navigateFrom === "list"
                        ? false
                        : true
                    }
                    multiline
                    maxRows={4}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            <Row>
              {/* Task Type */}
              <Col lg="7">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-type"
                    label="Task Type"
                    defaultValue={taskType}
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>

              {/* Status */}
              <Col lg="5">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-stage"
                    label="Current Stage"
                    defaultValue={taskStage}
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            <Row>
              {/* Remarks */}
              <Col lg="12">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-remark"
                    label="Remark"
                    onChange={remarkHandler}
                    onFocus={handleInputClick}
                    ref={taskRemarkRef}
                    defaultValue={taskRemarkRef.current ? taskRemarkRef.current : "   "}
                    disabled={
                      navigateFrom === "inquiry" ||
                      taskStage === "Task Creation" ||
                      taskStage === "Process Exception"
                        ? true
                        : false
                    }
                    multiline
                    maxRows={4}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            {taskDetails.processExceptionMessage && (
              <Row>
                {/* Process Exception */}
                <Col lg="12">
                  <FormGroup>
                    <TextField
                      error
                      fullWidth
                      size="small"
                      id="process-exception"
                      label="Error Details"
                      defaultValue={taskDetails.processExceptionMessage}
                      disabled={true}
                      multiline
                      maxRows={4}
                      color="warning"
                      sx={{
                        m: 1,
                        "& .MuiInputBase-input.Mui-disabled": {
                          WebkitTextFillColor: "red",
                        },
                      }}
                    />
                  </FormGroup>
                </Col>
              </Row>
            )}
          </div>
        </AccordionDetails>
      </Accordion>

      {/* -------------------- Additional Details -------------------- */}
      <Accordion
        className="accordion"
        expanded={expanded === "panel2"}
        onChange={handleChange("panel2")}
      >
        <AccordionSummary
          aria-controls="panel1bh-content"
          id="panel1bh-header"
          expandIcon={<ExpandMoreIcon sx={{ color: "white" }} />}
          sx={{
            backgroundColor: "black",
            color: "white",
            border: "1px solid",
            borderRadius: "8px",
          }}
        >
          <Typography sx={{ width: "33%", flexShrink: 0 }}>
            Additional Details
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          {iframeUrl !== "" ? (
            <iframe
              id="liveDetail"
              src={iframeUrl}
              title="Example Iframe"
              style={{ width: "100%", height: "100vh" }}
              onLoad={handleLoad}
            ></iframe>
          ) : (
            <Typography>Live detail not available</Typography>
          )}
        </AccordionDetails>
      </Accordion>

      {/* -------------------- Other Information -------------------- */}
      <Accordion
        className="accordion"
        expanded={expanded === "panel3"}
        onChange={handleChange("panel3")}
      >
        <AccordionSummary
          aria-controls="panel1bh-content"
          id="panel1bh-header"
          expandIcon={<ExpandMoreIcon sx={{ color: "white" }} />}
          sx={{
            backgroundColor: "black",
            color: "white",
            border: "1px solid",
            borderRadius: "8px",
          }}
        >
          <Typography sx={{ width: "33%", flexShrink: 0 }}>
            Other Information
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <div className="accordion-content">
            {/* Assignee */}
            <Row>
              <Col md="6">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-assignee"
                    label="Assignee"
                    defaultValue={
                      taskDetails.currentHolderUserId
                        ? taskDetails.currentHolderUserId
                        : "   "
                    }
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            {/* Approved By */}
            <Row>
              <Col md="8">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-action-by"
                    label={
                      taskDetails.actionType === 2
                        ? "Rejected By"
                        : taskDetails.actionType === 3
                        ? "Cancelled By"
                        : "Approved By"
                    }
                    defaultValue={
                      taskDetails.actionBy ? taskDetails.actionBy : "   "
                    }
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>

              <Col md="4">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-action-date"
                    label={
                      taskDetails.actionType === 2
                        ? "Date Rejected"
                        : taskDetails.actionType === 3
                        ? "Date Cancelled"
                        : "Date Approved"
                    }
                    defaultValue={
                      taskDetails.dateAction ? taskDetails.dateAction : "   "
                    }
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            {/* Last Update */}
            <Row>
              <Col md="8">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-updated-by"
                    label="Updated By"
                    defaultValue={
                      taskDetails.updatedBy ? taskDetails.updatedBy : "   "
                    }
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>

              <Col md="4">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-date-updated"
                    label="Date Updated"
                    defaultValue={
                      taskDetails.dateUpdated ? taskDetails.dateUpdated : "   "
                    }
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>

            {/* Task Creator */}
            <Row>
              <Col md="8">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-created-by"
                    label="Created By"
                    defaultValue={
                      taskDetails.createdBy ? taskDetails.createdBy : "   "
                    }
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>

              <Col md="4">
                <FormGroup>
                  <TextField
                    fullWidth
                    size="small"
                    id="task-date-created"
                    label="Date Created"
                    defaultValue={taskDetails.dateCreated}
                    disabled={true}
                    sx={{ m: 1 }}
                  />
                </FormGroup>
              </Col>
            </Row>
          </div>
        </AccordionDetails>
      </Accordion>

      {/* -------------------- Workflow History -------------------- */}
      <Accordion
        className="accordion"
        expanded={expanded === "panel4"}
        onChange={handleChange("panel4")}
        sx={{
          display: navigateFrom === "inquiry" ? true : "none",
        }}
      >
        <AccordionSummary
          aria-controls="panel1bh-content"
          id="panel1bh-header"
          expandIcon={<ExpandMoreIcon sx={{ color: "white" }} />}
          sx={{
            backgroundColor: "black",
            color: "white",
            border: "1px",
            borderRadius: "8px",
          }}
        >
          <Typography sx={{ width: "33%", flexShrink: 0 }}>
            Task Workflow History
          </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <TableContainer component={Paper}>
            <Table sx={{ minWidth: 700 }} aria-label="customized table">
              <TableHead>
                <TableRow>
                  <StyledTableCell>No.</StyledTableCell>
                  <StyledTableCell>Action</StyledTableCell>
                  <StyledTableCell>Previous Stage</StyledTableCell>
                  <StyledTableCell>Current Stage</StyledTableCell>
                  <StyledTableCell>Previous Holder</StyledTableCell>
                  <StyledTableCell>Current Holder</StyledTableCell>
                  <StyledTableCell>Routed In Date</StyledTableCell>
                  <StyledTableCell>Routed Out Date</StyledTableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {taskHistory ? (
                  taskHistory.map((hist, index) => {
                    return (
                      <StyledTableRow key={index}>
                        <StyledTableCell>{index + 1}</StyledTableCell>
                        <StyledTableCell>{hist.actionName}</StyledTableCell>
                        <StyledTableCell>{hist.stageFrom}</StyledTableCell>
                        <StyledTableCell>{hist.stageTo}</StyledTableCell>
                        <StyledTableCell>{hist.holderFromUserId}</StyledTableCell>
                        <StyledTableCell>{hist.holderToUserId}</StyledTableCell>
                        <StyledTableCell>{hist.dateStart}</StyledTableCell>
                        <StyledTableCell>{hist.dateEnd}</StyledTableCell>
                      </StyledTableRow>
                    );
                  })
                ) : (
                  <TableRow>
                    <StyledTableCell align="center" colSpan={8}>No Data Available</StyledTableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </AccordionDetails>
      </Accordion>
    </div>
  );
};

export default AccordionComponent;
