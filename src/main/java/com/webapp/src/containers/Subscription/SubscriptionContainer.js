/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {pluck} from "underscore";
import {SubscriptionComponent} from "../../components/Subscriptions/SubscriptionComponent";
import {subscriptionTabSelected} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state) => {
    let index = 0;
    if (state.searchResults.activeIndex != undefined) {
        index = state.searchResults.activeIndex;
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
        handleTopicChange: (articles, index) => {
            dispatch(subscriptionTabSelected(articles, index));
        }
    }
};

const SubscriptionContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SubscriptionComponent);

export default SubscriptionContainer;