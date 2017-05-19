/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem, OverlayTrigger, Popover, Button} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import SearchBarContainer from "../containers/Home/SearchBarContainer";
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
                <NavItem eventKey={3}>
                    My Topics
                </NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <NavItem eventKey={3} onSelect={() => handleSubscriptionSearch()}>
                    My Topics
            </NavItem>

        )
    }
};

const rightNavItem = (loggedIn, handleLoginClicked, handleSettingsSearch) => {
    if (!loggedIn) {
        return (
            <OverlayTrigger trigger="click" rootClose placement="bottom" container={this}
                            overlay={loginPopover(handleLoginClicked, handleSettingsSearch)}>
                <NavItem eventKey={4}>Settings</NavItem>
            </OverlayTrigger>
        )
    } else {
        return (
            <NavItem eventKey={4} onSelect={() => handleSettingsSearch()}>Settings</NavItem>
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
                <Nav>
                    <NavItem eventKey={2}>
                        <SearchBarContainer hidden={true}/>
                    </NavItem>
                </Nav>
            </Navbar.Header>

            <Navbar.Collapse>
                <Nav pullRight>
                    {leftNavItem(loggedIn, handleLoginClicked, handleSubscriptionSearch)}
                    {rightNavItem(loggedIn, handleLoginClicked, handleSettingsSearch)}
                </Nav>
            </Navbar.Collapse>
        </Navbar>
    </div>

    )
};
