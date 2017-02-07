/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SearchResults} from "../../components/SearchResults/SearchResult";

const mapStateToProps = (state) => {
    return {
        searchResults: state.searchBar.searchResults,
        fetchInProgress: true //state.searchBar.fetchInProgress
    }
};

const mapDispatchToProps = (dispatch) => {
    return {}
};

export default SearchResultsContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SearchResults);