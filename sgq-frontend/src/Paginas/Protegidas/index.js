import React from 'react';
import { Route, Redirect } from 'react-router-dom';

import NavbarSGQ from '../../Componentes/Navbar';
import Dashboard from './Dashboard';
import { ArtefatoNovo, ArtefatosLista, ConsultaNormas } from './Incidentes';
import { Destinatario } from './Transparencia'

import LoginManager from '../../Componentes/LoginManager';

let _lm = new LoginManager();

function RotaProtegida({ children, ...params }) {

    let expirado = _lm.user() == null;

    return (
        <>
            <NavbarSGQ />
            <Route {...params} render={({ location }) =>
                expirado ?
                    <Redirect to="/login" />
                    :
                    children
            } />
        </>
    );
}

export { RotaProtegida, Dashboard, ArtefatoNovo, ArtefatosLista, ConsultaNormas, Destinatario }