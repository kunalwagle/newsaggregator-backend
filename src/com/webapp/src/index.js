import React from "react";
import ReactDOM from "react-dom";
import {createStore, compose, applyMiddleware} from "redux";
import {Provider} from "react-redux";
import SearchBarContainer from "./containers/Home/SearchBarContainer";
import {Router, Route, browserHistory} from "react-router";
import App from "./App";
import "babel-polyfill";
import thunk from "redux-thunk";
//import {reducers} from './reducers'

const store = createStore(App, compose(applyMiddleware(thunk)));
const rootEl = document.getElementById('root');

const render = () => ReactDOM.render((
        <Provider store={store}>
            <Router history={browserHistory}>
                <Route path="/" component={SearchBarContainer}/>
            </Router>
        </Provider>
    ),
    rootEl
);

render();

