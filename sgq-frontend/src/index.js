import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import './index.css';

import NavbarSGQ from './Componentes/Navbar';
import * as Abertas from './Paginas/Abertas/';
import * as Protegidas from './Paginas/Protegidas/';
import Footer from './Footer';

import { ToastContainer } from 'react-toastify';

import * as serviceWorker from './serviceWorker';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-toastify/dist/ReactToastify.css';


ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <Switch>
        <Route exact path="/">
          <Abertas.Abertura />
        </Route>
        <Route path="/login">
          <Abertas.Login />
        </Route>
        <Route path="/eventos">
          <Abertas.Eventos />
        </Route>


        <Protegidas.RotaProtegida exact path="/dashboard">
          <NavbarSGQ />
          <Protegidas.Dashboard />
        </Protegidas.RotaProtegida>

        <Protegidas.RotaProtegida path="/dashboard/artefato">
          <NavbarSGQ />
          <Protegidas.ArtefatoNovo />
        </Protegidas.RotaProtegida>

        <Protegidas.RotaProtegida path="/dashboard/artefatos">
          <NavbarSGQ />
          <Protegidas.ArtefatosLista />
        </Protegidas.RotaProtegida>

        <Protegidas.RotaProtegida path="/dashboard/normas">
          <NavbarSGQ />
          <Protegidas.ConsultaNormas />
        </Protegidas.RotaProtegida>

      </Switch>
    </BrowserRouter>
    <ToastContainer/>
    <Footer />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
