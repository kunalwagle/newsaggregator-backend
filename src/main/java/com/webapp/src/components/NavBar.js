/**
 * Created by kunalwagle on 07/02/2017.
 */
import React from "react";
import {Navbar, Nav, NavItem, OverlayTrigger, Popover} from "react-bootstrap";
import FacebookLogin from "react-facebook-login";
import GoogleLogin from "react-google-login";

const loginPopover = (handleFacebookLogin, handleGoogleSuccess, handleGoogleFailure) => (
    <Popover title="Login" id="loginModalPopover">
        <FacebookLogin appId="540333079424464"
                       autoLoad={false}
                       fields="name,email,picture"
                       callback={handleFacebookLogin}/>
        <br/><br/><br/>
        <GoogleLogin onSuccess={handleGoogleSuccess}
                     onFailure={handleGoogleFailure}
                     clientId="454694778698-pcb41ncbaqevjkcgvnko36faneenrb73.apps.googleusercontent.com"/>
    </Popover>
);

export const NavBarComponent = ({handleFacebookLogin, handleGoogleSuccess, handleGoogleFailure}) => (
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
                <OverlayTrigger trigger="click" placement="bottom"
                                overlay={loginPopover(handleFacebookLogin, handleGoogleSuccess, handleGoogleFailure)}>
                    <NavItem eventKey={2}>Register</NavItem>
                </OverlayTrigger>

                <NavItem eventKey={3}>Sign In</NavItem>
            </Nav>
        </Navbar.Collapse>
    </Navbar>
    </div>

);
