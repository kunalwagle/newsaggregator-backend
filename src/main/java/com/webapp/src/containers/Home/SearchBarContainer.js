/**
 * Created by kunalwagle on 07/02/2017.
 */
import {connect} from "react-redux";
import {SearchBar} from "../../components/Home/SearchBar";
import {searchValueChanged, search} from "../../actions/Home/SearchBarActions";

const mapStateToProps = (state, ownProps) => {
    return {
        searchValue: state.searchBar.searchValue,
        hidden: ownProps.hidden
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleSearchValueChanged: (searchValue) => {
            dispatch(searchValueChanged(searchValue));
        },
        handleSearchButtonPressed: (event) => {
            event.preventDefault();
            dispatch(search());
        }
    }
};

const SearchBarContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(SearchBar);

export default SearchBarContainer;