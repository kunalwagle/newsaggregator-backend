/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import TopicPanelView from "../../components/TopicViewer/TopicPanelView";
import {articleClicked} from "../../actions/TopicViewer/TopicViewerActions";
import {viewClicked} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state, ownProps) => {
    return {
        topicId: ownProps.topicId,
        articles: state.searchResults.articles,
        fetchInProgress: state.searchResults.fetchInProgress,
        fetchInProgressCalled: state.searchResults.fetchInProgressCalled
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleArticleClick: (event, article) => {
            event.preventDefault();
            dispatch(articleClicked(article));
        },
        handleReloadNeeded: (topicId) => {
            dispatch(viewClicked(topicId));
        }
    }
};

const TopicPanelContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(TopicPanelView);

export default TopicPanelContainer;
