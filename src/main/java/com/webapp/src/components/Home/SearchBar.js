/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";
import {Link} from "react-router";

export const SearchBar = ({searchValue, handleSearchValueChanged, handleSearchButtonPressed}) => (
    <Form inline type="submit" bsClass="centred-div">
        <FormGroup bsClass="col-lg-8 col-sm-7 col-xs-5 col-md-8" controlId="searchForm">
            <FormControl bsClass="col-lg-12 search-bar-standard" type="text"
                         placeholder="Search Keywords" value={searchValue}
                         onChange={(searchValue) => handleSearchValueChanged(searchValue.target.value)}
            />
        </FormGroup>
        {'  '}
        <Button onClick={event => handleSearchButtonPressed(event)}
                bsClass="search-bar-button col-lg-3 col-sm-3 col-xs-3 col-md-3" type="submit">
            Search
        </Button>
    </Form>
);