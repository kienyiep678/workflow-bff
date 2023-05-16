// import React from "react";
// import { render, screen } from "@testing-library/react";
// import "@testing-library/jest-dom";
// import TableDisplay from "./TableDisplay";

// describe("TableDisplay", () => {
//   const mockTableData = [
//     {
//       taskReferenceNo: "TF00000401",
//       taskName: "Check api",
//       stageName: "TASK_CANCELLED",
//     },
//     {
//       taskReferenceNo: "TF00000454",
//       taskName: "Check dropdown",
//       stageName: "PENDING_APPROVAL",
//     },
//   ];

//   beforeAll(() => {
//     // Mock the global fetch function to return the mock data
//     global.fetch = jest.fn(() =>
//       Promise.resolve({
//         json: () => Promise.resolve(mockTableData),
//       })
//     );
//   });

//   afterAll(() => {
//     // Restore the original implementation of fetch
//     global.fetch.mockRestore();
//   });

//   it("displays the correct number of table rows", async () => {
//     render(<TableDisplay />);

//     const tableRows = screen.getAllByRole("row");

//     expect(tableRows).toHaveLength(mockTableData.length + 1); // Add 1 for header row
//   });

//   it("displays the correct table data", async () => {
//     render(<TableDisplay />);

//     const tableCells = screen.getAllByRole("cell");

//     expect(tableCells[0]).toHaveTextContent(mockTableData[0].taskReferenceNo);
//     expect(tableCells[1]).toHaveTextContent(mockTableData[0].taskName);
//     expect(tableCells[2]).toHaveTextContent(mockTableData[0].stageName);
//     expect(tableCells[3]).toHaveTextContent(mockTableData[1].taskReferenceNo);
//     expect(tableCells[4]).toHaveTextContent(mockTableData[1].taskName);
//     expect(tableCells[5]).toHaveTextContent(mockTableData[1].stageName);
//   });
// });
