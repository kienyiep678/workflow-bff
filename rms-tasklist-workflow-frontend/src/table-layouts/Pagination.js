import React, { Component } from "react";

function PaginationTable({ currentPage, totalPages, onPageChange }) {
  // Define the maximum number of page buttons to show at a time
  const maxButtonsToShow = 3;

  // calculate the range of pages to show based on the current page
  const pageRangeStart = Math.max(
    1,
    Math.min(
      currentPage - Math.floor(maxButtonsToShow / 2),
      totalPages - maxButtonsToShow + 1
    )
  );
  const pageRangeEnd = Math.min(
    totalPages,
    pageRangeStart + maxButtonsToShow - 1
  );

  const pageNumbers = [...Array(pageRangeEnd - pageRangeStart + 1).keys()].map(
    (num) => num + pageRangeStart
  );

  return (
    <div className="pagination mb-3 d-flex justify-content-end">
      {totalPages > 0 ? (
        <>
          <button
            className="page-link"
            disabled={currentPage === 1}
            onClick={() => onPageChange(currentPage - 1)}
            style={{
              borderRadius: "50%",
              margin: "3px",
              color: "#696969",
            }}
          >
            &laquo;
          </button>
          {pageNumbers.map((pageNumber) => (
            <button
              key={pageNumber}
              className="page-link"
              style={{
                backgroundColor:
                  currentPage === pageNumber ? "#88BC4A" : "white",
                borderRadius: "50%",
                margin: "3px",
                color: "#696969",
              }}
              onClick={() => onPageChange(pageNumber)}
            >
              {pageNumber}
            </button>
          ))}
          <button
            className="page-link"
            disabled={currentPage === totalPages}
            onClick={() => onPageChange(currentPage + 1)}
            style={{ borderRadius: "50%", margin: "3px", color: "#696969" }}
          >
            &raquo;
          </button>
        </>
      ) : null}
    </div>
  );
}

export default PaginationTable;
