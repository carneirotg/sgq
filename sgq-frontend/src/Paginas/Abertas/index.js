import React from 'react';
import { Route, Switch } from 'react-router-dom';

import Abertura from './Abertura';
import Login from './Login';
import Eventos from './Eventos';


function RotasAbertas() {
    return (
        <Switch>
            <Route exact path="/">
                <Abertura />
            </Route>
            <Route path="/login">
                <Login />
            </Route>
            <Route path="/eventos">
                <Eventos />
            </Route>
        </Switch>
    )
}


export { RotasAbertas }