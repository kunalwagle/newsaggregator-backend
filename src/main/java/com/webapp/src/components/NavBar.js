/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem} from "react-bootstrap";

export const NavBarComponent = () => (
    <Navbar inverse collapseOnSelect>
        <Navbar.Header>
            <Navbar.Brand>
                <a href="#">News Aggregator</a>
            </Navbar.Brand>
            <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
            <Nav>
                <NavItem eventKey={1} href="#">About</NavItem>
            </Nav>
            <Nav pullRight>
                <NavItem eventKey={1}>
                    <input placeholder="Search"/>
                </NavItem>
                <NavItem eventKey={2} href="#">Register</NavItem>
                <NavItem eventKey={3} href="#">Sign In</NavItem>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
);
