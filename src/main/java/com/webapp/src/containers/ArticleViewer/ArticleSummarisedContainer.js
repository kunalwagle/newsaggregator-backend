/**
 * Created by kunalwagle on 20/04/2017.
 */
import {connect} from "react-redux";
import {ArticlesSummarised} from "../../components/ArticleViewer/ArticlesSummarised";

const mapStateToProps = (state) => {
    return {
        article: state.topicViewer.article
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
