import React from 'react';
import LoginManager from './LoginManager';

let _lm = new LoginManager();

function ComponenteProtegido({ children, ...params }) {
    const { papel } = params;

    if (_lm.possuiPapel(papel)) {
        return (children);
    } else {
        return (<></>);
    }
}

export default ComponenteProtegido;