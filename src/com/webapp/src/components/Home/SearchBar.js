/**
 * Created by kunalwagle on 06/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";

export const SearchBar = ({searchValue, handleSearchValueChanged, handleSearchButtonPressed}) => (
    <Jumbotron>
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