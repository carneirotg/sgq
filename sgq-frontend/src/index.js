import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import './index.css';
import * as Abertas from './Paginas/Abertas/';
import Footer from './Footer';
import * as serviceWorker from './serviceWorker';

import 'bootstrap/dist/css/bootstrap.min.css';

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Switch>
      <Route exact path="/">
          <Abertas.Abertura/>
        </Route>
        <Route path="/login">
          <Abertas.Login/>
        </Route>
        <Route path="/eventos">
          <Abertas.Eventos/>
        </Route>
      </Switch>
    </BrowserRouter>
    <Footer />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
