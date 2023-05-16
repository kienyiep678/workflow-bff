import React, { Component } from "react";

const { Container } = require("reactstrap");

const PageTitle = (props) => {
  return (
    <div className="ml-3">
      <h2
        style={{
          color: "white",
          fontWeight: "bold",
        }}
      >
        {props.pageTitle}
      </h2>
      <h5
        style={{
          color: "white",
        }}
      >
        {props.pageDescription}
      </h5>
    </div>
  );
};

export default PageTitle;
