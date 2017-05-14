/**
 * Created by kunalwagle on 20/04/2017.
 */
import React, {Component} from "react";
import {Popover} from "react-bootstrap";

export default class LoginModal extends Component {
    render() {
        return (
            <Popover title="Login" id="loginModalPopover" {...this.props}>
                <strong>Give an email address to log in</strong>
                <br/><br/><br/>
                <input onChange={this.props.handleEmailChange} placeholder="Email address"/>
                <br/><br/><br/>
                <button onClick={() => this.props.handleLoginClicked(this.props.loggedIn, this.props.action)}>Login
                </button>
            </Popover>
        );
    }
};