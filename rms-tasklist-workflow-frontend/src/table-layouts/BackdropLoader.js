import React, { Component } from "react";

import { Backdrop } from "@mui/material";
import CircularProgress from "@mui/material/CircularProgress";

const BackdropLoader = ({ openBackdrop }) => {
  return (
    <>
      <Backdrop
        sx={{
          color: "#fff",
          zIndex: (theme) => theme.zIndex.drawer + 1,
          backgroundColor: "rgba(0, 0, 0, 0.3)", // set the background color with alpha value
        }}
        open={openBackdrop}
      >
        <CircularProgress color="inherit" />
      </Backdrop>
    </>
  );
};

export default BackdropLoader;
