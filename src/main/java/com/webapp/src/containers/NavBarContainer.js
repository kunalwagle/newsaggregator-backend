/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {NavBarComponent} from "../components/NavBar";
import {emailAddressChanged, login, logout, getSubscriptions} from "../actions/LoginModalActions";


const mapStateToProps = (state) => {
    return {
        loggedIn: state.loggedIn.loggedIn,
        user: state.loggedIn.user
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleLoginClicked: (loggedIn, action) => {
            if (!loggedIn) {
                dispatch(login(action));
            } else {
                dispatch(logout());
            }
        },
        handleEmailChange: (event) => {
            dispatch(emailAddressChanged(event.target.value));
        },
        handleSubscriptionSearch: () => {
            dispatch(getSubscriptions())
        }
    }
};

const NavBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBarComponent);

export default NavBarContainer;
