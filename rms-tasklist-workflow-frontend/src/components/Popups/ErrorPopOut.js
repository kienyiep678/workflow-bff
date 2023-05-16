import { useState } from "react";
import { Collapse, Row, Col, Button, Card, Container } from "reactstrap";
import { UI_MESSAGE_SUCCESS } from "../../data/DataConst.js";

const ErrorPopOut = (props) => {
  const closeBackdrop = false;
  const { successBodyText } = props;

  function getTexts(successList) {
    let successText = "";

    // error 200
    if (successList !== null && props.showPartialError) {
      successText =
        successList
          .map((item) => {
            const textArr = item.split(" ");
            const textStr = textArr.join(", ");
            return textStr;
          })
          .join(", ") + UI_MESSAGE_SUCCESS;
    } else {
      successText = "";
    }

    return { successText };
  }
  const { successText } = getTexts(successBodyText);
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
              <p className="pl-4 text-white text-sm">Error Details:</p>
              {props.failBodyText && (
                <li
                  style={{
                    listStyle: "none",
                    paddingLeft: "0",
                    marginLeft: "0",
                  }}
                  className="text-white text-sm"
                >
                  {props.failBodyText}
                </li>
              )}

              {successText && (
                <ul className="text-white text-sm">{successText}</ul>
              )}
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default ErrorPopOut; //put inside TableFilter.js
