import { Row, Col, Container } from "reactstrap";

const ErrorMessageWarning = (props) => {
  const { errorMessageList, successMessageList } = props;

  return (
    <>
      <Container
        style={{
          backgroundColor: "#bf4f51",
          width: "400px",
          paddingLeft: "0",
          marginLeft: "0",
          borderRadius: "10px",
        }}
      >
        <Row>
          <Col>
            <div className="mt-3">
              <p
                style={{
                  fontWeight: "bold",
                  paddingLeft: "15px",
                  color: "white",
                }}
              >
                Error Details:
              </p>
              {errorMessageList && (
                <li
                  data-testid="popup-error-list"
                  style={{
                    listStyle: "none",
                    margin: "16px",
                  }}
                  className="text-white text-sm"
                >
                  {errorMessageList}
                </li>
              )}

              {successMessageList && (
                <li
                  data-testid="popup-success-list"
                  style={{
                    listStyle: "none",
                    margin: "16px",
                  }}
                  className="text-white text-sm"
                >
                  {props.successMessageList}
                </li>
              )}
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default ErrorMessageWarning; //put inside TableFilter.js
