import React from 'react';
import { Route, Redirect, Switch } from 'react-router-dom';

import NavbarSGQ from '../../Componentes/Navbar';
import ToastManager from '../../Componentes/ToastManager';

import Dashboard from './Dashboard';
import { ArtefatoNovo, ArtefatosLista, ConsultaNormas, NovaNC, ListaNC, NovoIncidente, ListaIncidente } from './Incidentes';
import { RelIncidente } from './Relatorios';
import { Destinatario, NovaCampanha, ListaCampanha } from './Transparencia'

import LoginManager from '../../Componentes/LoginManager';

let _lm = new LoginManager();

function RotaProtegida({ component: Componente, ...params }) {
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
            <Route {...params} render={(props) => {

                if (expirado) {
                    return <Redirect to="/login" />;
                } else if (!autorizado) {
                    return <Redirect to="/dashboard" />
                } else {

                    return <>
                        <NavbarSGQ />
                        <Componente {...props} />
                    </>

                }

            }

            } />
        </>
    );
}

function RotasProtegidas(props) {
    const path = props.path;

    return (
        <>
            <Switch>
                <RotaProtegida exact path={path} component={Dashboard} />

                <RotaProtegida exact path={`${path}/artefato`} component={ArtefatoNovo} />

                <RotaProtegida exact path={`${path}/artefatos`} component={ArtefatosLista} />

                <RotaProtegida exact path={`${path}/normas`} component={ConsultaNormas} />

                <RotaProtegida exact path={`${path}/nc`} component={NovaNC} />

                <RotaProtegida exact path={`${path}/nc/:id`} component={NovaNC} />

                <RotaProtegida exact path={`${path}/ncs`} component={ListaNC} />

                <RotaProtegida exact path={`${path}/incidente`} component={NovoIncidente} />

                <RotaProtegida exact path={`${path}/incidente/:id`} component={NovoIncidente} />

                <RotaProtegida exact path={`${path}/incidentes`} component={ListaIncidente} />

                <RotaProtegida papel="GESTOR" exact path={`${path}/destinatarios`} component={Destinatario} />

                <RotaProtegida papel="GESTOR" exact path={`${path}/relatorios/incidentes`} component={RelIncidente} />

                <RotaProtegida papel="GESTOR" exact path={`${path}/campanha`} component={NovaCampanha} />

                <RotaProtegida papel="GESTOR" exact path={`${path}/campanha/:id`} component={NovaCampanha} />

                <RotaProtegida papel="GESTOR" exact path={`${path}/campanhas`} component={ListaCampanha} />

            </Switch>
        </>
    )
}

export {
    RotasProtegidas
}