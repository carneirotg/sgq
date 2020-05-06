import React, { Component } from 'react';
import { Route, Redirect } from 'react-router-dom';

import Dashboard from './Dashboard';

import LoginManager from '../../Componentes/LoginManager';

let _lm = new LoginManager();

function RotaProtegida({ children, ...params }) {

    let expirado = _lm.user() == null;

    return (
        <Route {...params} render={({ location }) =>
            expirado ?
                <Redirect to="/login" />
                :
                children
        } />
    );
}

export { RotaProtegida, Dashboard }