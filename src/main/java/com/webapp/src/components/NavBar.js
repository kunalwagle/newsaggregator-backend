/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem, OverlayTrigger, Popover, Button} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import LoginModal from "./LoginModal";

const loginPopover = (handleLoginClicked, action) => {
    return (
        <LoginModal handleLoginClicked={handleLoginClicked} action={action}/>
    );
};

const leftNavItem = (loggedIn, handleLoginClicked, handleSubscriptionSearch) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                            overlay={loginPopover(handleLoginClicked, handleSubscriptionSearch)}>
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

const rightNavItem = (loggedIn, handleLoginClicked, handleSettingsSearch) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                            overlay={loginPopover(handleLoginClicked, () => handleLoginClicked(false))}>
                <NavItem eventKey={3}>Settings</NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <NavItem eventKey={3} onSelect={() => handleSettingsSearch()}>Settings</NavItem>
        )
    }
};

export const NavBarComponent = ({loggedIn, user, handleSettingsSearch, handleLoginClicked, handleSubscriptionSearch}) => {

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
                    {leftNavItem(loggedIn, handleLoginClicked, handleSubscriptionSearch)}
                    {rightNavItem(loggedIn, handleLoginClicked, handleSettingsSearch)}
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    </div>

    )
};
