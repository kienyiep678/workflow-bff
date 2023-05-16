import { useEffect } from "react";
import { useState } from "react";
// react component that copies the given text inside your clipboard
import PageTitle from "../../table-layouts/PageTitle";
// import Records from './record.json';
import {
  REDIRECT_URL_TaskTypeCreate,
  REDIRECT_URL_TaskTypeDetail,
} from "../../data/DataConst.js";
import TaskGlobalApi from "data/TaskGlobalApi.js";
import PaginationTable from "../../table-layouts/Pagination";
import { useHistory } from "react-router-dom";
// reactstrap components
import {
  Container,
  Form,
  FormGroup,
  Input,
  Row,
  Col,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
} from "reactstrap";
// core components
import {
  Skeleton,
  Button,
  TableContainer,
  TableHead,
  Table,
  TableRow,
  TableBody,
  TableCell,
  Typography,
} from "@mui/material";

export default function TaskSetting() {
  //filter
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  //display data in table
  const [record, setRecord] = useState([]);
  // pagination
  const [currentPage, setCurrentPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPage] = useState(0);

  const history = useHistory();

  const headerRowStyle = {
    paddingTop: "10px",
    paddingBottom: "10px",
    fontWeight: "bold",
    verticalAlign: "middle",
    textAlign: "left",
    // border: "1px solid #e4e4e4",
  };

  useEffect(() => {
    getData();
    timeCount();
    setCurrentPage(currentPage);
    setPageSize(10);
  }, [currentPage]);

  const getData = () => {
    TaskGlobalApi.GetTaskSettingList(currentPage)
      .then((res) => res.json())
      .then((data) => {
        setRecord(data.data.taskTypePreviewList);
        setTotalPage(data.data.countOfPage);
      });
  };

  const handleSubmit = (id) => {
    history.push(REDIRECT_URL_TaskTypeDetail + id);
  };

  const handleCreate = () => {
    history.push(REDIRECT_URL_TaskTypeCreate);
  };

  function submit(e) {
    e.preventDefault();
  }

  const timeCount = () => {
    setTimeout(() => {
      setLoading(false);
    }, 1500);
  };

  const handlePageChange = (pageNum) => {
    setCurrentPage(pageNum);
  };

  return (
    <>
      {/* Page content */}
      <Container fluid>
        <Container
          style={{
            marginTop: "50px",
            color: "white",
          }}
          fluid
        >
          <PageTitle
            pageTitle="Task Type Setting"
            pageDescription="Configuration for various task type."
          />
          <Row>
            <Col
              lg="3"
              style={{
                marginTop: 47,
              }}
            >
              <FormGroup>
                <label className="text-sm">Filter by Task Type Name</label>
                <InputGroup className="input-group-rounded input-group-merge">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <span className="fa fa-search" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    className="form-control-rounded form-control-prepended text-x"
                    id="input-stage_name"
                    type="stageName"
                    name="StageName"
                    onChange={(e) => setSearch(e.target.value)}
                    style={{
                      color: "black",
                    }}
                  />
                </InputGroup>
              </FormGroup>
            </Col>
            <Col className="text-right" lg="8"></Col>
            <Col
              className="text-right"
              lg="1"
              style={{
                marginTop: 60,
              }}
            >
              <Form onSubmit={(e) => submit(e)}>
                <Button
                  type="create"
                  sx={{
                    color: "white",
                    bgcolor: "#88BC4A",
                    "&:hover": {
                      bgcolor: "#88BC4A",
                    },
                  }}
                  onClick={(e) => handleCreate()}
                >
                  Create Task
                </Button>
              </Form>
            </Col>
          </Row>
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
            <div className="col" style={{ marginBottom: "15px" }}>
              <Table style={{ marginTop: "20px" }}>
                <TableHead
                  style={{
                    backgroundColor: "white",
                    color: "gray",
                  }}
                >
                  <TableRow>
                    <TableCell style={headerRowStyle}>No</TableCell>
                    <TableCell style={headerRowStyle}>Task Type Name</TableCell>
                    <TableCell style={headerRowStyle}>
                      Last modified by
                    </TableCell>
                    <TableCell style={headerRowStyle}>
                      Last modified date
                    </TableCell>
                    <TableCell style={headerRowStyle}>Status</TableCell>
                    <TableCell style={headerRowStyle}>Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody style={{ backgroundColor: "" }}>
                  {record
                    .filter((record) => {
                      return search.toLowerCase() === ""
                        ? record
                        : record.taskName.toLowerCase().includes(search);
                    })
                    .map((record, i) => (
                      <TableRow key={record.taskTypeCode}>
                        {loading ? (
                          <TableCell>
                            <Skeleton
                              variant="rectangular"
                              width={20}
                              height={5}
                              animation="wave"
                            />
                          </TableCell>
                        ) : (
                          <TableCell>{i + 1}</TableCell>
                        )}
                        {loading ? (
                          <TableCell>
                            <Skeleton
                              variant="rectangular"
                              width={100}
                              height={5}
                              animation="wave"
                            />
                          </TableCell>
                        ) : (
                          <TableCell>{record.taskName}</TableCell>
                        )}
                        {loading ? (
                          <TableCell>
                            <Skeleton
                              variant="rectangular"
                              width={80}
                              height={5}
                              animation="wave"
                            />
                          </TableCell>
                        ) : (
                          <TableCell>{record.lastModifiedBy}</TableCell>
                        )}
                        {loading ? (
                          <TableCell>
                            <Skeleton
                              variant="rectangular"
                              width={80}
                              height={5}
                              animation="wave"
                            />
                          </TableCell>
                        ) : (
                          <TableCell>
                            {new Date(record.lastDateModified).toLocaleString()}
                          </TableCell>
                        )}
                        {loading ? (
                          <TableCell>
                            <Skeleton
                              variant="rectangular"
                              width={80}
                              height={5}
                              animation="wave"
                            />
                          </TableCell>
                        ) : (
                          <TableCell>
                            {record.active ? "Active" : "Inactive"}
                          </TableCell>
                        )}
                        {loading ? (
                          <TableCell>
                            <Skeleton
                              variant="rectangular"
                              width={50}
                              height={5}
                              animation="wave"
                            />
                          </TableCell>
                        ) : (
                          <TableCell>
                            <i
                              className="my-2 ni ni-settings-gear-65"
                              style={{ color: "#88BC4A", fontSize: 23 }}
                              type="submit"
                              onClick={(e) => handleSubmit(record.taskTypeCode)}
                            ></i>
                          </TableCell>
                        )}
                      </TableRow>
                    ))}
                </TableBody>
              </Table>
            </div>
          </Row>
          {record === 0 && (
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
        </Container>
      </Container>
    </>
  );
}
