/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Jumbotron, Button, Form, FormGroup, FormControl} from "react-bootstrap";
import {Link} from "react-router";

export const SearchBar = ({searchValue, hidden, handleSearchValueChanged, handleSearchButtonPressed}) => {

    let overallClassName = "col-lg-8 col-sm-7 col-xs-5 col-md-8";
    let searchBarClassName = "search-bar-standard";
    let buttonClassName = "search-bar-button col-lg-3 col-sm-3 col-xs-3 col-md-3";

    if (hidden) {
        buttonClassName = "";
        overallClassName = "col-lg-12 col-sm-9 col-xs-9 col-md-9 pull-left";
        searchBarClassName = "search-bar-small";
    }

    return (
        <Form inline type="submit" bsClass="centred-div">
            <FormGroup bsClass={overallClassName} controlId="searchForm">
                <FormControl bsClass={"col-lg-12 " + searchBarClassName} type="text"
                             placeholder="Search Keywords" value={searchValue}
                             onChange={(searchValue) => handleSearchValueChanged(searchValue.target.value)}
                />
            </FormGroup>
            {'  '}
            <Button onClick={event => handleSearchButtonPressed(event)} hidden={hidden}
                    bsClass={buttonClassName} type="submit">
                Search
            </Button>
        </Form>
    )
};