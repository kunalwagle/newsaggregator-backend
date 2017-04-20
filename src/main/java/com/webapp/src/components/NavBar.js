/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem, OverlayTrigger, Popover, Button} from "react-bootstrap";


const loginPopover = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked) => {

    if (loggedIn) {
        return (
            <Popover title="Log Out" id="loginModalPopover">
                You are logged in as <strong>{emailAddress}</strong>
                <Button onClick={handleLoginClicked}>Log Out</Button>
            </Popover>
        )
    } else {
        return (
            <Popover title="Login" id="loginModalPopover">
                <strong>Give an email address to log in</strong>
                <input onChange={handleEmailChange} placeholder="Email address"/>
                <Button onClick={handleLoginClicked}>Login</Button>
            </Popover>
        );
    }
};

const navItems = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked) => {
    if (loggedIn) {
        return (
            <div>
                <OverlayTrigger rootClose
                                overlay={loginPopover(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}>
                    <NavItem eventKey={2}>Register</NavItem>
                </OverlayTrigger>
                <OverlayTrigger rootClose
                                overlay={loginPopover(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}>
                    <NavItem eventKey={3}>Sign In</NavItem>
                </OverlayTrigger>
            </div>
        )
    } else {
        return (
            <div>
                <NavItem href="myTopics" eventKey={2}>My Topics</NavItem>
                <OverlayTrigger rootClose
                                overlay={loginPopover(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}>
                    <NavItem eventKey={3}>Log Out</NavItem>
                </OverlayTrigger>
            </div>
        )
    }
};

export const NavBarComponent = ({loggedIn, emailAddress, handleEmailChange, handleLoginClicked}) => (
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
                    {navItems(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    </div>

);
