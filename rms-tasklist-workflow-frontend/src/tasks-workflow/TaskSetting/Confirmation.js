//React-strap component
import { Modal, Button, ModalBody, ModalFooter, Row, Col } from "reactstrap";
import { Container, } from "@mui/material";

import Exclamation from "../../assets/img/icons/exclamation.png";

import {
    UI_POPUP_CONFIRM,
} from "../../data/DataConst.js";

const ConfirmModal = (props) => {
    /* -------------------- Object Destructuring -------------------- */
    const {
        closePopUpConfirmModal,
        closeCancelPopUpConfirmModal,
        showConfirmModal,
        popUpType,
        modalTitle,
        modalText,
    } = props;

    const getImage = () => {

        if (
            popUpType === UI_POPUP_CONFIRM
        ) {
            return Exclamation;
        }
    };

    return (
        <>
            {/* -------------------- Modal -------------------- */}
            <div>
                <Modal
                    isOpen={showConfirmModal}
                    toggle={closePopUpConfirmModal}
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
                            <p></p>
                            <h1 className="text-center">Are You Sure</h1>
                            <p className="text-center">Do you really want to delete these task type? 
                            This process cannot be undone.</p>
                        </ModalBody>
                    </div>

                    <ModalFooter className="mt--4">
                        <Container>
                            <Row>
                                <Col xs="6" className="text-right">
                                    <Button
                                        style={{
                                            color: 'white',
                                            backgroundColor: '#A91E1E',
                                        }}
                                        onClick={closePopUpConfirmModal}
                                    >
                                        Confirm
                                    </Button>
                                </Col>
                                <Col xs="1" className="text-left">
                                    <Button
                                        color="darker"
                                        onClick={closeCancelPopUpConfirmModal}
                                    >
                                        Cancel
                                    </Button>
                                </Col>
                            </Row>
                        </Container>
                    </ModalFooter>
                </Modal>
            </div>
        </>
    );
};

export default ConfirmModal;
