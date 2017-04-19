/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import {TopicTopBar} from "../../components/TopicViewer/TopicTopBar";
import {subscribeClicked} from "../../actions/TopicViewer/TopicViewerActions";

const mapStateToProps = (state) => {
    return {
        label: state.topicViewer.label
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleSubscribeClicked: (event) => {
            event.preventDefault();
            dispatch(subscribeClicked());
        }
    }
};

const TopicTopBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicTopBar);

export default TopicTopBarContainer;