import React from "react";
import ReactDOM from "react-dom";
import {createStore, compose, applyMiddleware} from "redux";
import {Provider} from "react-redux";
import {SearchBarJumbotron} from "./components/Home/SearchBarJumbotron";
import {Route, IndexRoute, browserHistory, Router} from "react-router";
import {syncHistoryWithStore, routerMiddleware} from "react-router-redux";
import {responsiveStoreEnhancer} from "redux-responsive";
import {persistStore, autoRehydrate} from "redux-persist";
import localForage from "localforage";
import App from "./App";
import "babel-polyfill";
import thunk from "redux-thunk";
import NavBarContainer from "./containers/NavBarContainer";
import {SearchResultPage} from "./components/SearchResults/SearchResultPage";
import {SummaryEvaluation} from "./components/SummaryEvaluation/SummaryEvaluation";
import {TopicViewerPage} from "./components/TopicViewer/TopicViewer";
import {ArticleViewerPage} from "./components/ArticleViewer/ArticleViewerPage";
import {SubscriptionPage} from "./components/Subscriptions/Subscriptions";
import {TopicsList} from "./components/Settings/TopicsList";
import {DigestViewerPage} from "./components/Digest/DigestViewer";
import NotFound from "./components/NotFound";
import ReduxToastr from "react-redux-toastr";
//import {reducers} from './reducers'


const middleware = routerMiddleware(browserHistory);
const store = createStore(App, compose(responsiveStoreEnhancer, autoRehydrate({log: true}), applyMiddleware(thunk, middleware)));
const rootEl = document.getElementById('root');
const history = syncHistoryWithStore(browserHistory, store);


class NavigationBar extends React.Component {
    render() {
        return (
            <div>
                <NavBarContainer/>
                {this.props.children}
            </div>
        );
    }
}

class AppProvider extends React.Component {

    constructor() {
        super();
        this.state = {rehydrated: false}
    }

    componentWillMount() {
        // persistStore(store, {storage: localForage}).purge();
        persistStore(store, {whitelist: ['loggedIn'], storage: localForage}, () => {
            this.setState({rehydrated: true});
        });
    }

    render() {
        if (!this.state.rehydrated) {
            return <div className="loader"></div>
        }
        return (
            <Provider store={store}>
                <div>
                    <Router history={history}>
                        <Route path="/" component={NavigationBar}>
                            <IndexRoute component={SearchBarJumbotron}/>
                            <Route path="searchResults/:searchTerm" component={SearchResultPage}/>
                            <Route path="summaryEvaluation" component={SummaryEvaluation}/>
                            <Route path="topic/:topicId" component={TopicViewerPage}/>
                            <Route path="topic/:topicId/article/:articleId" component={ArticleViewerPage}/>
                            <Route path="subscription/:userId" component={SubscriptionPage}/>
                            <Route path="settings" component={TopicsList}/>
                            <Route path="digest/:digestId" component={DigestViewerPage}/>
                            <Route path="*" component={NotFound}/>
                        </Route>
                    </Router>
                    <ReduxToastr
                        timeOut={4000}
                        newestOnTop={false}
                        preventDuplicates
                        position="bottom-right"
                        transitionIn="fadeIn"
                        transitionOut="fadeOut"/>
                </div>
            </Provider>
        )
    }
}

const render = () => {
    ReactDOM.render((
            <AppProvider/>
        ),
        rootEl
    );
};

render();

