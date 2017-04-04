/**
 * Created by kunalwagle on 04/04/2017.
 */
import {FormControl, Button, FormGroup, ControlLabel} from "react-bootstrap";
import React from "react";

export const SummaryInput = ({firstBoxText, secondBoxText, thirdBoxText, handleFirstChange, handleSecondChange, handleThirdChange, handleSubmit}) => (

    <form>
        <FormGroup controlId="firstArticle">
            <ControlLabel>First Article URL (Independent)</ControlLabel>
            <FormControl componentClass="textarea" value={firstBoxText}
                         onChange={handleFirstChange}/>
        </FormGroup>
        <FormGroup controlId="secondArticle">
            <ControlLabel>Second Article URL (Telegraph)</ControlLabel>
            <FormControl componentClass="textarea" value={secondBoxText}
                         onChange={handleSecondChange}/>
        </FormGroup>
        <FormGroup controlId="thirdArticle">
            <ControlLabel>Third Article URL (Reuters)</ControlLabel>
            <FormControl componentClass="textarea" value={thirdBoxText}
                         onChange={handleThirdChange}/>
        </FormGroup>
        <Button type="button" onClick={handleSubmit}>
            Submit
        </Button>
    </form>

);