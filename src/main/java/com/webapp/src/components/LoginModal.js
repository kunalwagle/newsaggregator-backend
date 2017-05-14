/**
 * Created by kunalwagle on 20/04/2017.
 */
import React, {Component} from "react";
import {Popover} from "react-bootstrap";

export default class LoginModal extends Component {

    constructor(props) {
        super(props);
        this.state = {emailAddress: ""};
        this.handleEmailChange = this.handleEmailChange.bind(this)
    }

    handleEmailChange(event) {
        this.setState({
            emailAddress: event.target.value
        });
    }

    render() {
        return (
            <Popover title="Login" id="loginModalPopover" {...this.props}>
                <strong className="white centred-div">Registering is simple. As no sensitive data is stored we just need
                    your email address.</strong>
                <br/><br/><br/>
                <input className="bar-standard" onChange={this.handleEmailChange} placeholder="Email address"
                       value={this.state.emailAddress}/>
                <br/><br/><br/>
                <button className="login-button"
                        onClick={() => this.props.handleLoginClicked(this.state.emailAddress, this.props.action)}>Login
                </button>
            </Popover>
        );
    }
};