/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import TopicPanelView from "../../components/TopicViewer/TopicPanelView";
import {articleClicked} from "../../actions/TopicViewer/TopicViewerActions";
import {viewClicked} from "../../actions/SearchResults/SearchResultsActions";

const mapStateToProps = (state, ownProps) => {
    let articles = state.searchResults.articles;
    let topicId = ownProps.topicId;
    let fetchInProgressCalled = state.searchResults.fetchInProgressCalled;
    if (ownProps.topic !== undefined) {
        articles = ownProps.topic.clusters;
        topicId = ownProps.topic.id;
        fetchInProgressCalled = true;
    }
    return {
        topicId,
        articles,
        fetchInProgress: state.searchResults.fetchInProgress,
        fetchInProgressCalled
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleArticleClick: (event, topicId, articleId) => {
            event.preventDefault();
            dispatch(articleClicked(topicId, articleId));
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
