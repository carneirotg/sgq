import React from 'react';
import { Route, Redirect, Switch } from 'react-router-dom';

import NavbarSGQ from '../../Componentes/Navbar';
import ToastManager from '../../Componentes/ToastManager';

import Dashboard from './Dashboard';
import { ArtefatoNovo, ArtefatosLista, ConsultaNormas, NovaNC, ListaNC } from './Incidentes';
import { RelIncidente } from './Relatorios';
import { Destinatario } from './Transparencia'

import LoginManager from '../../Componentes/LoginManager';

let _lm = new LoginManager();

function RotaProtegida({ children, ...params }) {

    const papel = params.papel;
    const autorizado = _lm.possuiPapel(papel) || papel === undefined;

    const expirado = _lm.user() == null;

    if (expirado) {
        ToastManager.erro("Você precisar estar autenticado para acessar o dashboard")
    } else if (!autorizado) {
        ToastManager.atencao("Você não possui autorização para acessar este recurso");
    }

    return (
        <>
            {
                expirado ? <Redirect to="/login" /> : <></>
            }
            <NavbarSGQ />
            <Route {...params} render={({ location }) =>
                autorizado ?
                    children :
                    <Redirect to="/dashboard" />
            } />
        </>
    );
}

function RotasProtegidas(props) {
    const path = props.path;

    return (
        <>
            <Switch>
                <RotaProtegida exact path={path}>
                    <Dashboard />
                </RotaProtegida>

                <RotaProtegida exact path={`${path}/artefato`}>
                    <ArtefatoNovo />
                </RotaProtegida>

                <RotaProtegida exact path={`${path}/artefatos`}>
                    <ArtefatosLista />
                </RotaProtegida>

                <RotaProtegida exact path={`${path}/normas`}>
                    <ConsultaNormas />
                </RotaProtegida>

                <RotaProtegida exact path={`${path}/nc/nova`}>
                    <NovaNC />
                </RotaProtegida>

                <RotaProtegida exact path={`${path}/ncs`}>
                    <ListaNC />
                </RotaProtegida>

                <RotaProtegida papel="GESTOR" exact path={`${path}/destinatarios`}>
                    <Destinatario />
                </RotaProtegida>

                <RotaProtegida papel="GESTOR" exact path={`${path}/relatorios/incidentes`}>
                    <RelIncidente />
                </RotaProtegida>
            </Switch>
        </>
    )
}

export {
    RotasProtegidas
}