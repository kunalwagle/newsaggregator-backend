/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem} from "react-bootstrap";
import LoginModalContainer from "../containers/LoginModal";

export const NavBarComponent = ({handleSelect}) => (
    <div>
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
                <NavItem eventKey={2} onSelect={handleSelect}>Register</NavItem>
                <NavItem eventKey={3} onSelect={handleSelect}>Sign In</NavItem>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
        <LoginModalContainer/>
    </div>

);
