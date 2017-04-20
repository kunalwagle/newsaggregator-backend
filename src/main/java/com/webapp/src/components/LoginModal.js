/**
 * Created by kunalwagle on 20/04/2017.
 */
import React from "react";
import {Modal} from "react-bootstrap";
import FacebookLogin from "react-facebook-login";
import GoogleLogin from "react-google-login";

export const LoginModal = ({shouldShow, handleFacebookLogin, handleGoogleSuccess, handleGoogleFailure, handleHide}) => (
    <Modal show={shouldShow} onHide={handleHide}>
        <Modal.Header closeButton>
            <Modal.Title>Login</Modal.Title>
            <Modal.Body>
                <FacebookLogin appId="540333079424464"
                               autoLoad={true}
                               fields="name,email,picture"
                               callback={handleFacebookLogin}/>
                <br/><br/><br/>
                <GoogleLogin onSuccess={handleGoogleSuccess}
                             onFailure={handleGoogleFailure}
                             clientId="454694778698-pcb41ncbaqevjkcgvnko36faneenrb73.apps.googleusercontent.com"/>
            </Modal.Body>
        </Modal.Header>
    </Modal>
);