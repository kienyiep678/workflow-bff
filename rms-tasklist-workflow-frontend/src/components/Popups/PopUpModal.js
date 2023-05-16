//React-strap component
import { Button, Modal, ModalBody, ModalFooter, Row } from "reactstrap";
import { Container } from "@mui/material";

import TickImage from "../../assets/img/icons/success.png";
import CrossImage from "../../assets/img/icons/reject.png";
import Exclamation from "../../assets/img/icons/exclamation.png";
import DeleteImage from "../../assets/img/icons/delete.png";

import {
  UI_POPUP_APPROVE,
  UI_POPUP_CLAIM,
  UI_POPUP_EXECUTE,
  UI_POPUP_RETURN_FROM_APPROVAL,
  UI_POPUP_RETURN_FROM_EXCEPTION,
  UI_POPUP_SUBMIT,
  UI_POPUP_SAVED,
  UI_POPUP_UPDATE,
  UI_POPUP_CANCEL,
  UI_POPUP_REJECT,
  UI_POPUP_CANCEL_FROM_EXCEPTION,
  UI_POPUP_DELETE,
} from "../../data/DataConst.js";

const PopUpModal = (props) => {
  /* -------------------- Object Destructuring -------------------- */
  const {
    closePopUpModal,
    showModal,
    popUpType,
    modalTitle,
    modalText,
    additionalModalNavigation, //For additional link besides the modal button
  } = props;

  /* -------------------- Get Image Based on Pop Up Type -------------------- */
  const getImage = () => {
    // return popUpType === "APPROVE" || popUpType === "CLAIM" || popUpType === "EXECUTE" || popUpType === "RETURN_FROM_APPROVAL" || popUpType === "RETURN_FROM_EXCEPTION" ||
    //   popUpType === "SUBMIT" || popUpType === "SAVED" || popUpType === "UPDATE"
    //     ? TickImage
    //   : popUpType === "CANCEL" || popUpType === "REJECT" || "CANCEL_FROM_EXCEPTION"
    //     ? CrossImage
    //   : popUpType === "DELETE"
    //     ? DeleteImage
    //   : Exclamation

    if (
      popUpType === UI_POPUP_APPROVE ||
      popUpType === UI_POPUP_CLAIM ||
      popUpType === UI_POPUP_EXECUTE ||
      popUpType === UI_POPUP_RETURN_FROM_APPROVAL ||
      popUpType === UI_POPUP_RETURN_FROM_EXCEPTION ||
      popUpType === UI_POPUP_SUBMIT ||
      popUpType === UI_POPUP_SAVED ||
      popUpType === UI_POPUP_UPDATE
    ) {
      return TickImage;
    } else if (
      popUpType === UI_POPUP_CANCEL ||
      popUpType === UI_POPUP_REJECT ||
      popUpType === UI_POPUP_CANCEL_FROM_EXCEPTION
    ) {
      return CrossImage;
    } else if (popUpType === UI_POPUP_DELETE) {
      return DeleteImage;
    } else {
      return Exclamation;
    }
  };

  return (
    <>
      {/* -------------------- Modal -------------------- */}
      <div>
        <Modal
          isOpen={showModal}
          toggle={closePopUpModal}
          style={{ width: "400px", marginTop: "120px" }}
        >
          <div className="modal-dialog">
            <ModalBody>
              <img
                src={getImage()}
                alt={popUpType}
                style={{
                  maxWidth: "120px",
                  height: "auto",
                  margin: "auto",
                  display: "flex",
                  justifyContent: "center",
                }}
              />
              <h1 className="text-center">{modalTitle}</h1>
              <p></p>
              <p className="text-center">{modalText}</p>
            </ModalBody>
          </div>

          <ModalFooter className="mt--4">
            <Container>
              <Row style={{ display: "flex", justifyContent: "center" }}>
                {additionalModalNavigation ? additionalModalNavigation : null}
              </Row>

              <Row style={{ display: "flex", justifyContent: "center" }}>
                <Button color="dark" onClick={closePopUpModal}>
                  Close
                </Button>
              </Row>
            </Container>
          </ModalFooter>
        </Modal>
      </div>
    </>
  );
};

export default PopUpModal;
