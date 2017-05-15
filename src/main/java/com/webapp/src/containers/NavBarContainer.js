/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {NavBarComponent} from "../components/NavBar";
import {emailAddressChanged, login, getSubscriptions} from "../actions/LoginModalActions";
import {initialise} from "../actions/Settings/SettingsActions";


const mapStateToProps = (state) => {
    return {
        loggedIn: state.loggedIn.loggedIn,
        user: state.loggedIn.user
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleLoginClicked: (email, action) => {
            dispatch(login(email, action));
        },
        handleEmailChange: (event) => {
            dispatch(emailAddressChanged(event.target.value));
        },
        handleSubscriptionSearch: () => {
            dispatch(getSubscriptions())
        },
        handleSettingsSearch: () => {
            dispatch(initialise());
        }
    }
};

const NavBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(NavBarComponent);

export default NavBarContainer;
