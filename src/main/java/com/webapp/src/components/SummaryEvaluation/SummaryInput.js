/**
 * Created by kunalwagle on 04/04/2017.
 */
import {FormControl, Button, FormGroup} from "react-bootstrap";

export const SummaryInput = (firstBoxText, secondBoxText, thirdBoxText, handleFirstChange, handleSecondChange, handleThirdChange, handleSubmit) => (

    <form onSubmit={handleSubmit}>
        <FormGroup controlId="firstArticle">
            <ControlLabel>First Article text</ControlLabel>
            <FormControl componentClass="textarea" placeholder="First Article text" value={firstBoxText}
                         onChange={handleFirstChange}/>
        </FormGroup>
        <FormGroup controlId="secondArticle">
            <ControlLabel>Second Article text</ControlLabel>
            <FormControl componentClass="textarea" placeholder="Second Article text" value={secondBoxText}
                         onChange={handleSecondChange}/>
        </FormGroup>
        <FormGroup controlId="thirdArticle">
            <ControlLabel>Third Article text</ControlLabel>
            <FormControl componentClass="textarea" placeholder="Third Article text" value={thirdBoxText}
                         onChange={handleThirdChange}/>
        </FormGroup>
        <Button type="submit">
            Submit
        </Button>
    </form>

);