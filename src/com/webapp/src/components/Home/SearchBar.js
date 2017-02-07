/**
 * Created by kunalwagle on 06/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";

export const SearchBar = ({searchValue, handleSearchValueChanged, handleSearchButtonPressed}) => (
    <Jumbotron className="col-md-8 col-md-offset-2">
        <h2 style={{"textAlign": "center"}}>
            News Aggregator and Summariser
        </h2>
        <br></br>
        <p style={{"textAlign": "center"}}>
            I really need to think of a better name...
        </p>
        <br></br>
        <p className="col-md-6 col-md-offset-3">
            <Form inline type="submit">
                <FormGroup bsClass="col-lg-10" controlId="searchForm">
                    <FormControl bsClass="col-lg-12" type="text"
                                 placeholder="Search Keywords" value={searchValue}
                                 onChange={(searchValue) => handleSearchValueChanged(searchValue.target.value)}
                    />
                </FormGroup>
                {'  '}
                <Button onClick={event => handleSearchButtonPressed(event)} bsClass="col-lg-2" type="submit">
                    Search
                </Button>
            </Form>
        </p>
    </Jumbotron>
);