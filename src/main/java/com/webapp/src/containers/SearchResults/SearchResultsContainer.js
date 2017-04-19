/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SearchResults} from "../../components/SearchResults/SearchResult";
import {viewClicked} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state) => {
    return {
        searchResults: state.searchBar.searchResults,
        fetchInProgress: state.searchBar.fetchInProgress
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleViewClicked: (event, title) => {
            event.preventDefault;
            dispatch(viewClicked(title));
        }
    }
};

const SearchResultsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SearchResults);

export default SearchResultsContainer;