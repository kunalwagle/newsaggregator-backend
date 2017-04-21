/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {TopicTopBar} from "../../components/TopicViewer/TopicTopBar";
import {subscribe} from "../../actions/LoginModalActions";

const mapStateToProps = (state) => {
    return {
        label: state.searchResults.label,
        loggedIn: state.loggedIn.loggedIn
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleSubscribeClicked: (topic) => {
            dispatch(subscribe(topic));
        }
    }
};

const TopicTopBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicTopBar);

export default TopicTopBarContainer;