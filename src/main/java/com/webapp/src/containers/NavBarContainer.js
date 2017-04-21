/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {NavBarComponent} from "../components/NavBar";
import {emailAddressChanged, loginChanged} from "../actions/LoginModalActions";


const mapStateToProps = (state) => {
    return {
        loggedIn: state.loggedIn.loggedIn,
        emailAddress: state.emailAddress
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleLoginClicked: (loggedIn) => {
            dispatch(loginChanged(!loggedIn));
        },
        handleEmailChange: (email) => {
            dispatch(emailAddressChanged(email));
        }
    }
};

const NavBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBarComponent);

export default NavBarContainer;
