/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {TopicTopBar} from "../../components/TopicViewer/TopicTopBar";
import {subscribe, unsubscribe, login} from "../../actions/LoginModalActions";
import {contains, pluck} from "underscore";

const mapStateToProps = (state, ownProps) => {
    const loggedIn = state.loggedIn.loggedIn;
    let label = state.searchResults.label;

    if (ownProps.topic) {
        label = ownProps.topic.label;
    }

    return {
        label,
        loggedIn,
        isSubscribed: state.searchResults.isSubscribed,
        topicId: ownProps.topicId
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleLoginClicked: (email, action) => {
            dispatch(login(email, action));
        },
        handleSubscribeClicked: (isSubscribed, topic, email) => {
            if (isSubscribed) {
                dispatch(unsubscribe(topic, email));
            } else {
                dispatch(subscribe(topic, email));
            }
        }
    }
};

const TopicTopBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicTopBar);

export default TopicTopBarContainer;