/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SubscriptionComponent} from "../../components/SearchResults/SearchResult";
import {viewClicked} from "../../actions/SearchResults/SearchResultsActions";
import {subscribe} from "../../actions/LoginModalActions";

const mapStateToProps = (state) => {
    return {
        searchResults: state.searchBar.searchResults,
        fetchInProgress: state.searchBar.fetchInProgress,
        loggedIn: state.loggedIn.loggedIn
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleViewClicked: (event, title) => {
            event.preventDefault();
            dispatch(viewClicked(title));
        },
        handleSubscribeClicked: (topic) => {
            dispatch(subscribe(topic));
        }
    }
};

const SearchResultsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SubscriptionComponent);

export default SearchResultsContainer;