import React, { Component } from "react";
import { Skeleton, TableRow, TableCell } from "@mui/material";

const TableSkeletonLoader = ({ rowsNum, colsNum, hasCheckbox }) => {
  //row style when page is loading
  const loadingRowStyle = {
    paddingTop: "20px",
    paddingBottom: "20px",
  };

  const rowWidth = 100;
  const rowHeight = 5;

  const cells = [];
  for (let i = 0; i < colsNum; i++) {
    cells.push(
      <TableCell key={`col-${i}`} style={loadingRowStyle}>
        <Skeleton
          animation="wave"
          variant="rectangle"
          width={rowWidth}
          height={rowHeight}
        />
      </TableCell>
    );
  }

  const tableRows = [...Array(rowsNum)].map((row, index) => (
    <TableRow key={`row-${index}`}>
      {hasCheckbox ? (
        <TableCell style={loadingRowStyle}>
          <Skeleton
            animation="wave"
            variant="rectangle"
            width={20}
            height={rowHeight}
          />
        </TableCell>
      ) : null}

      {cells}
    </TableRow>
  ));

  return tableRows;
};

export default TableSkeletonLoader;
