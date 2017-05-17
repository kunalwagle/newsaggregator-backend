/**
 * Created by kunalwagle on 19/04/2017.
 */
import {connect} from "react-redux";
import DigestPanelView from "../../components/Digest/DigestPanelView";
import {digestArticleClicked, reloadDigest} from "../../actions/Digest/DigestActions";

const mapStateToProps = (state, ownProps) => {
    return {
        digestId: ownProps.digestId,
        articles: state.digest.articles,
        fetchInProgress: state.digest.fetchInProgress,
        fetchInProgressCalled: state.digest.fetchInProgressCalled,
        mediaType: state.browser.mediaType
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        handleArticleClick: (event, topicId, articleId) => {
            event.preventDefault();
            dispatch(digestArticleClicked(topicId, articleId));
        },
        handleReloadNeeded: (topicId, topic) => {
            dispatch(reloadDigest(topicId, topic));
        }
    }
};

const DigestPanelContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(DigestPanelView);

export default DigestPanelContainer;
