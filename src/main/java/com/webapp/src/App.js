/**
 * Created by kunalwagle on 07/02/2017.
 */
import searchBar from "./reducers/Home/SearchBarReducers";
import summaryEvaluation from "./reducers/SummaryEvaluation/SummaryEvaluation";
import searchResults from "./reducers/SearchResults/SearchResultsReducers";
import topicViewer from "./reducers/TopicViewer/TopicViewerReducers";
import loggedIn from "./reducers/LoginModalReducers";
import {combineReducers} from "redux";

const App = combineReducers({
    searchBar,
    summaryEvaluation,
    searchResults,
    topicViewer,
    loggedIn
});

export default App;