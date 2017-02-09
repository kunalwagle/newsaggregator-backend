/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";
import {Link} from "react-router";

export const SearchBar = ({searchValue, handleSearchValueChanged, handleSearchButtonPressed}) => (
    <Form inline type="submit">
        <FormGroup bsClass="col-md-8" controlId="searchForm">
            <FormControl bsClass="col-lg-12" type="text"
                         placeholder="Search Keywords" value={searchValue}
                         onChange={(searchValue) => handleSearchValueChanged(searchValue.target.value)}
            />
        </FormGroup>
        {'  '}
        <Button onClick={event => handleSearchButtonPressed(event)} bsClass="col-md-3" type="submit">
            <Link to="/searchResults">Search</Link>
        </Button>
    </Form>
);