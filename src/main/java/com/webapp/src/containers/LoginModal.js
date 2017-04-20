/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {LoginModal} from "../components/LoginModal";
import {hideModal, facebookLogin, googleSuccess, googleFailure} from "../actions/LoginModalActions";

const mapStateToProps = (state) => {
    return {
        shouldShow: state.loggedIn.show
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleFacebookLogin: (response) => {
            dispatch(facebookLogin(response))
        },
        handleGoogleSuccess: (response) => {
            dispatch(googleSuccess(response))
        },
        handleGoogleFailure: (response) => {
            dispatch(googleFailure(response))
        },
        handleHide: () => {
            dispatch(hideModal());
        }
    }
};

const LoginModalContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(LoginModal);

export default LoginModalContainer;