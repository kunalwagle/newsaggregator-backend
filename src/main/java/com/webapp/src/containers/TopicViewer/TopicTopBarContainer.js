/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {TopicTopBar} from "../../components/TopicViewer/TopicTopBar";
import {subscribe, login} from "../../actions/LoginModalActions";
import {contains, pluck} from "underscore";

const mapStateToProps = (state, ownProps) => {
    const loggedIn = state.loggedIn.loggedIn;
    const label = state.searchResults.label;
    let isSubscribed = false;

    if (loggedIn) {
        const labels = pluck(state.loggedIn.user.topics, "id");
        isSubscribed = contains(labels, ownProps.topicId);
    }

    return {
        label,
        loggedIn,
        isSubscribed,
        topicId: ownProps.topicId
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleLoginClicked: (email, action) => {
            dispatch(login(email, action));
        },
        handleSubscribeClicked: (topic, email) => {
            dispatch(subscribe(topic, email));
        }
    }
};

const TopicTopBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicTopBar);

export default TopicTopBarContainer;