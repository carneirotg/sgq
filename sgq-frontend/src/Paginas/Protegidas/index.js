import React from 'react';
import { Route, Redirect } from 'react-router-dom';

import NavbarSGQ from '../../Componentes/Navbar';
import ToastManager from '../../Componentes/ToastManager';
import Dashboard from './Dashboard';
import { ArtefatoNovo, ArtefatosLista, ConsultaNormas } from './Incidentes';
import { RelIncidente } from './Relatorios';
import { Destinatario } from './Transparencia'

import LoginManager from '../../Componentes/LoginManager';

let _lm = new LoginManager();

function RotaProtegida({ children, ...params }) {
    const expirado = _lm.user() == null;
    const papel = params.papel;
    const autorizado = _lm.possuiPapel(papel) || papel === undefined;
    
    if(!autorizado){
        ToastManager.atencao("Você não possui autorização para acessar este recurso");
    }

    return (
        <>
            <NavbarSGQ />
            <Route render={({ location }) =>
                expirado ? 
                    <Redirect to="/login" /> : 
                    autorizado ? 
                        children : 
                        <Redirect to="/dashboard" />
            } />
        </>
    );
}

export {
    RotaProtegida,
    Dashboard,
    ArtefatoNovo, ArtefatosLista,
    ConsultaNormas,
    Destinatario,
    RelIncidente
}