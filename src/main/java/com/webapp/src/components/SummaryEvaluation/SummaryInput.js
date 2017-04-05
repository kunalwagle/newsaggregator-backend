/**
 * Created by kunalwagle on 04/04/2017.
 */
import {FormControl, Form, Button, FormGroup, ControlLabel, Panel, Grid, Row, Col} from "react-bootstrap";
import React from "react";

function linesToParagraphs(node, fetchInProgress) {
    if (fetchInProgress) return "Summarising. Results will be returned shortly";
    if (node === undefined) return "Summary will appear here";
    return node.split('\n').map(text => <p>{text}</p>)
        .reduce((nodes, node) => nodes.concat(node), []);
}

export const SummaryInput = ({firstBoxText, secondBoxText, thirdBoxText, extractive, abstractive, fetchInProgress, handleFirstChange, handleSecondChange, handleThirdChange, handleSubmit}) => (
    <div>
        <div>
            <Form inline={true}>
                {"\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"}
                <FormGroup controlId="firstArticle">
                    <ControlLabel>First Article URL (Independent)</ControlLabel>
                    {"\u00A0\u00A0\u00A0"}
                    <FormControl componentClass="input" value={firstBoxText}
                                 onChange={handleFirstChange}/>
                </FormGroup>
                {"\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"}
                <FormGroup controlId="secondArticle">
                    <ControlLabel>Second Article URL (Telegraph)</ControlLabel>
                    {"\u00A0\u00A0\u00A0"}
                    <FormControl componentClass="input" value={secondBoxText}
                                 onChange={handleSecondChange}/>
                </FormGroup>
                {"\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"}
                <FormGroup controlId="thirdArticle">
                    <ControlLabel>Third Article URL (Reuters)</ControlLabel>
                    {"\u00A0\u00A0\u00A0"}
                    <FormControl componentClass="input" value={thirdBoxText}
                                 onChange={handleThirdChange}/>
                </FormGroup>
                {"\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0"}
                <Button type="button" onClick={handleSubmit}>
                    Submit
                </Button>
            </Form>
        </div>
        <div><br/><br/><br/></div>
        <div>
            <Grid>
                <Row>
                    <Col lg={6}>
                        <Panel lg={12} header="Extractive Summary" bsStyle="info">
                            {linesToParagraphs(extractive, fetchInProgress)}
                        </Panel>
                    </Col>
                    <Col lg={6}>
                        <Panel lg={12} header="Abstractive Summary Phase One (Restoring pronoun sentences)"
                               bsStyle="success">
                            {linesToParagraphs(abstractive, fetchInProgress)}
                        </Panel>
                    </Col>
                </Row>
            </Grid>
        </div>
    </div>



);