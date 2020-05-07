import LoginManager from './LoginManager';
import Toast from './ToastManager';

const _lm = new LoginManager();

const artefatos = {
    salvar: async (artefato) => {
        return _post('artefatos', artefato);
    },
    listar: async () => {
        return _get('artefatos');
    },
    consulta: async (id) => {

    },
    depreciar: async (id) => {
        return _patch(`artefatos/${id}/depreciado`);
    }
}

const incidentes = {
    novo: (incidente) => {

    }
}

async function _patch(url) {
    let operationResponse = {};
    const response = await fetch(`/api/${url}`, { method: 'PATCH', headers: _authHeader({}) });

    if (response.ok) {
        operationResponse = { sucesso: true };
    } else {
        operationResponse = { sucesso: false };
        await _trataErroPadrao(response);
    }

    return operationResponse;
}

async function _get(url) {
    let operationResponse = {};
    const response = await fetch(`/api/${url}`, { headers: _authHeader({}) });

    if (response.ok) {
        const retorno = await response.json();

        operationResponse = { sucesso: true, retorno };
    } else {
        operationResponse = { sucesso: false };
        await _trataErroPadrao(response);
    }

    return operationResponse;

}

async function _post(url, objBody) {
    let operationResponse = {};
    const body = JSON.stringify(objBody);

    const response = await fetch(`/api/${url}`, { body, method: 'POST', headers: _authHeader({ 'Content-Type': 'application/json' }) });

    if (response.ok) {
        operationResponse = { sucesso: true };
    } else {
        operationResponse = { sucesso: false }


    }

    return operationResponse;
}

function _authHeader(headers) {
    headers['Authorization'] = `Bearer ${_lm.token()}`;

    return headers
}

async function _trataErroPadrao(response) {
    let erro = `Erro no servidor: ${response.status}`;
    const rJson = await response.json();

    if (rJson != null) {
        erro = rJson.erro;
    }

    Toast.erro(`${erro}`);
}

export function cliente() {
    return {
        artefatos
    }
}