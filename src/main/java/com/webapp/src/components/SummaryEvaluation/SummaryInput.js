/**
 * Created by kunalwagle on 04/04/2017.
 */
import {FormControl, Button, FormGroup, ControlLabel, Panel, Grid, Row, Col} from "react-bootstrap";
import React from "react";

export const SummaryInput = ({firstBoxText, secondBoxText, thirdBoxText, extractive, abstractive, handleFirstChange, handleSecondChange, handleThirdChange, handleSubmit}) => (
    <div>
        <div>
            <form>
                <FormGroup controlId="firstArticle">
                    <ControlLabel>First Article URL (Independent)</ControlLabel>
                    <FormControl componentClass="input" value={firstBoxText}
                                 onChange={handleFirstChange}/>
                </FormGroup>
                <FormGroup controlId="secondArticle">
                    <ControlLabel>Second Article URL (Telegraph)</ControlLabel>
                    <FormControl componentClass="input" value={secondBoxText}
                                 onChange={handleSecondChange}/>
                </FormGroup>
                <FormGroup controlId="thirdArticle">
                    <ControlLabel>Third Article URL (Reuters)</ControlLabel>
                    <FormControl componentClass="input" value={thirdBoxText}
                                 onChange={handleThirdChange}/>
                </FormGroup>
                <Button type="button" onClick={handleSubmit}>
                    Submit
                </Button>
            </form>
        </div>
        <div><br/><br/><br/></div>
        <div>
            <Grid>
                <Row>
                    <Col md={6}>
                        <Panel header="Extractive Summary" bsStyle="info">
                            {extractive}
                        </Panel>
                    </Col>
                    <Col md={6}>
                        <Panel header="Abstractive Summary" bsStyle="success">
                            {abstractive}
                        </Panel>
                    </Col>
                </Row>
            </Grid>
        </div>
    </div>



);