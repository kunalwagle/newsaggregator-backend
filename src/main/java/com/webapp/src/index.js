import React from "react";
import ReactDOM from "react-dom";
import {createStore, compose, applyMiddleware} from "redux";
import {Provider} from "react-redux";
import {SearchBarJumbotron} from "./components/Home/SearchBarJumbotron";
import {Router, Route, hashHistory} from "react-router";
import App from "./App";
import "babel-polyfill";
import thunk from "redux-thunk";
import {NavBarComponent} from "./components/NavBar";
import {SearchResultPage} from "./components/SearchResults/SearchResultPage";
import {SummaryEvaluation} from "./components/SummaryEvaluation/SummaryEvaluation";
import {TopicViewerPage} from "./components/TopicViewer/TopicViewer";
//import {reducers} from './reducers'

const store = createStore(App, compose(applyMiddleware(thunk)));
const rootEl = document.getElementById('root');
const navBar = document.getElementById('navbar');

const render = () => {
    ReactDOM.render((
            <NavBarComponent/>
        ),
        navBar
    );
    ReactDOM.render((
            <Provider store={store}>
                <Router history={hashHistory}>
                    <Route path="/" component={SearchBarJumbotron}/>
                    <Route path="/searchResults" component={SearchResultPage}/>
                    <Route path="/summaryEvaluation" component={SummaryEvaluation}/>
                    <Route path="/topic" component={TopicViewerPage}/>
                </Router>
            </Provider>
        ),
        rootEl
    );
};

render();

