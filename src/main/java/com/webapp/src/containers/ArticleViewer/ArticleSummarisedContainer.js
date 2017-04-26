/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticlesSummarised} from "../../components/ArticleViewer/ArticlesSummarised";
import {find} from "underscore";

const mapStateToProps = (state, ownProps) => {
    let article = {
        articles: [null]
    };
    if (state.searchResults.fetchInProgressCalled) {
        article = find(state.searchResults.articles, (art) => {
            return art.id === ownProps.articleId;
        });
    }
    return {
        article,
        fetchInProgressCalled: state.searchResults.fetchInProgressCalled
    }
};

const mapDispatchToProps = () => {
    return {}
};

const ArticleSummarisedContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(ArticlesSummarised);

export default ArticleSummarisedContainer;
