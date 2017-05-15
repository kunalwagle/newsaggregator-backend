/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {pluck} from "underscore";
import {SettingsComponent} from "../../components/Settings/SettingsComponent";
import {subscriptionTabSelected} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state) => {
    let index = 0;
    if (state.settings.activeIndex != undefined) {
        index = state.settings.activeIndex;
    }
    return {
        fetchInProgress: state.loggedIn.fetchInProgress,
        topics: pluck(state.loggedIn.user.topics, "labelHolder"),
        loggedIn: state.loggedIn.loggedIn,
        index
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleTopicChange: (topic, index) => {
            dispatch(subscriptionTabSelected(topic, index));
        }
    }
};

const SettingsComponentContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SettingsComponent);

export default SettingsComponentContainer;