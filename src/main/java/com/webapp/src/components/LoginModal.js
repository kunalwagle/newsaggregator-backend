/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {Popover} from "react-bootstrap";
import FacebookLogin from "react-facebook-login";
import GoogleLogin from "react-google-login";

export const LoginModal = ({handleFacebookLogin, handleGoogleSuccess, handleGoogleFailure}) => (
    <Popover title="Login" id="loginModalPopover">
        <FacebookLogin appId="540333079424464"
                       autoLoad={true}
                       fields="name,email,picture"
                       callback={handleFacebookLogin}/>
        <br/><br/><br/>
        <GoogleLogin onSuccess={handleGoogleSuccess}
                     onFailure={handleGoogleFailure}
                     clientId="454694778698-pcb41ncbaqevjkcgvnko36faneenrb73.apps.googleusercontent.com"/>
    </Popover>
);