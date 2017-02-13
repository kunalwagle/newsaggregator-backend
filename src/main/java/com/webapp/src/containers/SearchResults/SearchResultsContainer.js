/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SearchResults} from "../../components/SearchResults/SearchResult";

const mapStateToProps = (state) => {
    return {
        searchResults: state.searchBar.searchResults,
        fetchInProgress: state.searchBar.fetchInProgress
    }
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

const SearchResultsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SearchResults);

export default SearchResultsContainer;