/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SubscriptionComponent} from "../../components/SearchResults/SearchResult";
import {viewClicked} from "../../actions/SearchResults/SearchResultsActions";
import {subscribe} from "../../actions/LoginModalActions";
import {searchOnReload} from "../../actions/Home/SearchBarActions";

const mapStateToProps = (state, ownProps) => {
    return {
        searchTerm: ownProps.searchTerm,
        searchResults: state.searchBar.searchResults,
        fetchInProgress: state.searchBar.fetchInProgress,
        fetchInProgressCalled: state.searchBar.fetchInProgressCalled,
        user: state.loggedIn.user,
        loggedIn: state.loggedIn.loggedIn
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleViewClicked: (event, title, articleCount) => {
            event.preventDefault();
            dispatch(viewClicked(title, undefined, 1, articleCount));
        },
        handleSubscribeClicked: (topic) => {
            dispatch(subscribe(topic));
        },
        handleSearchEmpty: (searchTerm) => {
            dispatch(searchOnReload(searchTerm));
        }
    }
};

const SearchResultsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SubscriptionComponent);

export default SearchResultsContainer;