/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {pluck} from "underscore";
import {SettingsComponent} from "../../components/Settings/SettingsComponent";
import {changeTopic} from "../../actions/Settings/SettingsActions";
import {login, logout} from "../../actions/LoginModalActions";

const mapStateToProps = (state) => {
    let index = 0;
    if (state.settings.activeIndex != undefined) {
        index = state.settings.activeIndex;
    }
    return {
        fetchInProgress: state.loggedIn.fetchInProgress,
        fetchInProgressLoginCalled: state.loggedIn.fetchInProgressCalled,
        fetchInProgressCalled: state.settings.fetchInProgressCalled,
        user: state.loggedIn.user,
        loggedIn: state.loggedIn.loggedIn,
        index
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleTopicChange: (topic, index) => {
            dispatch(changeTopic(topic, index));
        },
        handleReloadNeeded: (topic) => {
            dispatch(changeTopic(topic, 0));
        },
        handleReloadLogin: () => {
            dispatch(login());
        },
        handleLogout: () => {
            dispatch(logout());
        }
    }
};

const SettingsComponentContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SettingsComponent);

export default SettingsComponentContainer;