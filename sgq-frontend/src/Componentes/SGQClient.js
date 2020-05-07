import LoginManager from './LoginManager';
import Toast from './ToastManager';

const _lm = new LoginManager();

const artefatos = {
    salvar: async (artefato) => {
        return _post('artefatos', artefato);
    },
    buscarNome: async (nome, paginacao) => {
        return _get(`artefatos?nome=${nome}`, paginacao);
    },
    listar: async (paginacao) => {
        return _get('artefatos', paginacao);
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

async function _get(url, paginacao = { habilitada: false, pagina: 0 }) {
    let operationResponse = {};
    const response = await fetch(_trataParametrosRequest(`/api/${url}`, _paginar(paginacao)), { headers: _authHeader({}) });

    if (response.ok) {
        const retorno = await response.json();
        const headers = _headersSGQ(response);

        operationResponse = { sucesso: true, retorno, headers };
    } else {
        operationResponse = { sucesso: false };

        if (response.status == 404) {
            operationResponse['status'] = 404;
        } else {
            await _trataErroPadrao(response);
        }
    }

    return operationResponse;

}

function _trataParametrosRequest(url, params){ 
    if(url.includes('?')){
        url +=  '&';
    } else {
        url += '?';
    }

    return `${url}${params}`;
}

function _paginar(paginacao) {
    if (paginacao != null && paginacao.habilitada) {
        const pagAtual = paginacao.pagina > 0 ? paginacao.pagina : 1;

        return `pagina=${pagAtual}&registros=${paginacao.registros}`
    }

    return '';
}

function _headersSGQ(response) {
    const reqHeaders = response.headers;
    let headers = {};

    for (let h of reqHeaders.entries()) {
        if (h[0].includes("x-sgq")) {
            headers[h[0]] = h[1];
        }
    }

    return headers;
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
    const rJson = response.json();

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