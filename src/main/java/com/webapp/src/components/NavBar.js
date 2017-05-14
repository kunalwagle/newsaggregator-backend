/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem, OverlayTrigger, Popover, Button} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import LoginModal from "./LoginModal";

const loginPopover = (emailAddress, handleEmailChange, handleLoginClicked, action) => {
    return (
        <LoginModal handleEmailChange={handleEmailChange} handleLoginClicked={handleLoginClicked} action={action}
                    emailAddress={emailAddress}/>
    );
};

const leftNavItem = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked, handleSubscriptionSearch) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                            overlay={loginPopover(emailAddress, handleEmailChange, handleLoginClicked, handleSubscriptionSearch)}>
                <NavItem eventKey={2}>
                    My Topics
                </NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <NavItem eventKey={2} onSelect={() => handleSubscriptionSearch()}>
                    My Topics
            </NavItem>

        )
    }
};

const rightNavItem = (loggedIn, emailAddress, handleEmailChange, handleLoginClicked) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                            overlay={loginPopover(emailAddress, handleEmailChange, handleLoginClicked, () => handleLoginClicked(false))}>
                <NavItem eventKey={3}>Settings</NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <NavItem eventKey={3} onSelect={() => handleLoginClicked(loggedIn)}>Settings</NavItem>
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
                    <a href="/">
                        <img src={"/LogoShortForm.png"} className="navbar-image"/>
                    </a>
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
