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
        return _get(`artefatos/${id}`);
    },
    depreciar: async (id) => {
        return _patch(`artefatos/${id}/depreciado`);
    }
}

const normas = {
    listar: async () => {
        return _get('normas');
    },
    consulta: async (id) => {
        return _get(`normas/${id}`);
    }
}

const destinatarios = {
    listar: async () => {
        return _get('destinatarios');
    },
    salvar: async (destinatario) => {
        const id = destinatario.id;
        let resp;

        if (id === null || id === 0) {
            resp = _post(`destinatarios`, destinatario);
        } else {
            resp = _put(`destinatarios/${id}`, destinatario);
        }

        return resp;
    },
    remover: async (id) => {
        return _delete(`destinatarios/${id}`);
    }
}

const incidentes = {
    novo: (incidente) => {

    }
}

const naoConformidades = {
    salvar: async (nc) => {
        const id = nc.id;
        let resp;

        if (id === null || id === 0) {
            resp = _post(`ncs`, nc);
        } else {
            resp = _put(`ncs/${id}`, nc);
        }

        return resp;
    },
    consultaEstado: async (estado, pagina) => {
        return _get(`ncs/?estado=${estado}`, pagina);
    },
    consultaEstadoTitulo: async (estado, titulo) => {
        return _get(`ncs/?estado=${estado}&titulo=${titulo}`);
    },
    mudarEstado: async (id, estado) => {
        return _patch(`ncs/${id}/estado/${estado}`);
    }
}

const relatorios = {
    gerar: async (tipo, periodo = null) => {

        switch (tipo) {
            case "mesAtual": return _getBlob("relatorios/incidentes", tipo);
            case "anoAtual": return _getBlob("relatorios/incidentes/ano-corrente", tipo);
            case "seisMeses": return _getBlob("relatorios/incidentes/ultimos-seis-meses", tipo);
            case "dozeMeses": return _getBlob("relatorios/incidentes/ultimos-doze-meses", tipo);
            case "periodo": {
                const inicio = periodo.inicio.toISOString().split('T')[0];
                const fim = periodo.fim.toISOString().split('T')[0];

                return _getBlob(`relatorios/incidentes/de/${inicio}/ate/${fim}`, tipo);
            }
        }
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

async function _delete(url) {
    let operationResponse = {};
    const response = await fetch(`/api/${url}`, { method: 'DELETE', headers: _authHeader({}) });

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

        if (response.status === 404) {
            operationResponse['status'] = 404;
        } else {
            await _trataErroPadrao(response);
        }
    }

    return operationResponse;

}

async function _getBlob(url, tipo) {
    let operationResponse = {};
    const response = await fetch(`/api/${url}`, { headers: _authHeader({}) });

    if (response.ok) {

        const relBlob = await response.blob();
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(relBlob);
        link.download = `relatorio-${tipo}.pdf`;
        link.click();

        operationResponse = { sucesso: true };
    } else {
        operationResponse = { sucesso: false };

        if (response.status === 404) {
            operationResponse['status'] = 404;
        } else {
            await _trataErroPadrao(response);
        }
    }

    return operationResponse;

}

function _trataParametrosRequest(url, params) {
    if (url.includes('?')) {
        url += '&';
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
    return _pxxt('POST', url, objBody);
}

async function _put(url, objBody) {
    return _pxxt('PUT', url, objBody);
}

async function _pxxt(metodo, url, objBody) {
    let operationResponse = {};
    const body = JSON.stringify(objBody);

    const response = await fetch(`/api/${url}`, { body, method: metodo, headers: _authHeader({ 'Content-Type': 'application/json' }) });

    if (response.ok) {
        operationResponse = { sucesso: true };

        const location = response.headers.get('Location');

        if (location != null) {
            const splitted = location.split('/');
            const chave = splitted[splitted.length - 1];

            operationResponse['resourceId'] = parseInt(chave);
        }

    } else {
        operationResponse = { sucesso: false };
        const rJson = await response.json();

        if (rJson != null) {
            let detalhe = rJson.erroCompleto;

            let erro = rJson.erro || rJson.error;

            if (detalhe != null && detalhe.length <= 50) {
                erro += ` - ${detalhe}`;
            }

            operationResponse['erro'] = erro;
        }

    }

    return operationResponse;
}

function _authHeader(headers) {
    headers['Authorization'] = `Bearer ${_lm.token()}`;

    return headers
}

async function _trataErroPadrao(response) {
    let erro = `Erro no servidor: ${response.status}`;

    try {
        const rJson = await response.json();

        if (rJson != null) {
            let detalhe = rJson.erroCompleto;

            erro = rJson.erro || rJson.error;

            if (detalhe != null && detalhe.length <= 50) {
                erro += ` - ${detalhe}`;
            }
        }
    } catch (ex) {
        console.error(`Erro tratando retorno de exceção: ${ex}`);
    }

}

export function cliente() {
    return {
        artefatos, normas, destinatarios, relatorios, naoConformidades
    }
}