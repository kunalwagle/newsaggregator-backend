/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem, OverlayTrigger, Popover, Button} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";

const loginPopover = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked) => {
    return (
        <Popover title="Login" id="loginModalPopover">
            <strong>Give an email address to log in</strong>
            <br/><br/><br/>
            <input onChange={handleEmailChange} placeholder="Email address"/>
            <br/><br/><br/>
            <Button onClick={() => handleLoginClicked(loggedIn)}>Login</Button>
        </Popover>
    );
};

const leftNavItem = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked, handleSubscriptionSearch) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom"
                            overlay={loginPopover(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}>
                <NavItem eventKey={2}>Register</NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <LinkContainer onClick={handleSubscriptionSearch} to="/subscription" activeHref="active">
                <NavItem>
                    My Topics
                </NavItem>
            </LinkContainer>

        )
    }
};

const rightNavItem = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom"
                            overlay={loginPopover(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}>
                <NavItem eventKey={3}>Sign In</NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <NavItem eventKey={3} onSelect={() => handleLoginClicked(loggedIn)}>Log Out</NavItem>
        )
    }
};

export const NavBarComponent = ({loggedIn, user, handleEmailChange, handleLoginClicked, handleSubscriptionSearch}) => {

    let emailAddress = "";
    if (user != undefined) {
        emailAddress = user.emailAddress;
    }

    return (
    <div>
        <Navbar inverse collapseOnSelect>
            <Navbar.Header>
                <Navbar.Brand>
                    <a href="/">News Aggregator</a>
                </Navbar.Brand>
                <Navbar.Toggle />
            </Navbar.Header>
            <Navbar.Collapse>
                <Nav>
                    <NavItem eventKey={1} href="#">About</NavItem>
                </Nav>
                <Nav pullRight>
                    {leftNavItem(loggedIn, emailAddress, handleEmailChange, handleLoginClicked, handleSubscriptionSearch)}
                    {rightNavItem(loggedIn, emailAddress, handleEmailChange, handleLoginClicked)}
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    </div>

    )
};
