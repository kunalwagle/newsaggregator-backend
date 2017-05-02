/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SubscriptionComponent} from "../../components/Subscriptions/SubscriptionComponent";
import {subscriptionTabSelected} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state) => {
    return {
        fetchInProgress: state.loggedIn.fetchInProgress,
        topics: state.loggedIn.user.topics,
        loggedIn: state.loggedIn.loggedIn
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleTopicChange: (articles) => {
            dispatch(subscriptionTabSelected(articles));
        }
    }
};

const SubscriptionContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SubscriptionComponent);

export default SubscriptionContainer;