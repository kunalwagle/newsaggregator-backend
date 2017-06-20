/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {pluck} from "underscore";
import {SubscriptionComponent} from "../../components/Subscriptions/SubscriptionComponent";
import {subscriptionTabSelected} from "../../actions/SearchResults/SearchResultsActions";
import {login, getSubscriptions} from "../../actions/LoginModalActions";

const mapStateToProps = (state) => {
    let index = 0;

    const fetchInProgressLoginCalled = state.loggedIn.fetchInProgressCalled;
    if (state.searchResults.activeIndex != undefined) {
        index = state.searchResults.activeIndex;
    }
    return {
        fetchInProgress: state.loggedIn.fetchInProgress,
        topics: state.loggedIn.topics,
        user: state.loggedIn.user,
        loggedIn: state.loggedIn.loggedIn,
        fetchInProgressLoginCalled,
        index
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleTopicChange: (articles, index) => {
            dispatch(subscriptionTabSelected(index));
        },
        handleReloadLogin: () => {
            dispatch(login());
        },
        handleLoginClicked: (email, action) => {
            dispatch(login(email, action));
        },
        handleSubscriptionSearch: () => {
            dispatch(getSubscriptions(true))
        }
    }
};

const SubscriptionContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SubscriptionComponent);

export default SubscriptionContainer;