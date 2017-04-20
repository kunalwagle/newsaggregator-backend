/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {NavBarComponent} from "../components/NavBar";
import {facebookLogin, googleSuccess, googleFailure} from "../actions/LoginModalActions";


const mapStateToProps = () => {
    return {}
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
        }
    }
};

const NavBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBarComponent);

export default NavBarContainer;
