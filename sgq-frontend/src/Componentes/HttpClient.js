import LoginManager from './LoginManager';

const _lm = new LoginManager();

class HttpClient {

    async patch(url) {
        let operationResponse = {};
        const response = await fetch(`/api/${url}`, { method: 'PATCH', headers: this._authHeader({}) });

        if (response.ok) {
            operationResponse = { sucesso: true };
        } else {
            operationResponse = { sucesso: false };
            await this._trataErroPadrao(response, operationResponse);
        }

        return operationResponse;
    }

    async patchBody(url, corpo) {
        return this._pxxt('PATCH', url, corpo);
    }

    async delete(url) {
        let operationResponse = {};
        const response = await fetch(`/api/${url}`, { method: 'DELETE', headers: this._authHeader({}) });

        if (response.ok) {
            operationResponse = { sucesso: true };
        } else {
            operationResponse = { sucesso: false };
            await this._trataErroPadrao(response, operationResponse);
        }

        return operationResponse;
    }

    async get(url, paginacao = { habilitada: false, pagina: 0 }) {
        let operationResponse = {};
        const response = await fetch(this._trataParametrosRequest(`/api/${url}`, this._paginar(paginacao)), { headers: this._authHeader({}) });

        if (response.ok) {
            const retorno = await response.json();
            const headers = this._headersSGQ(response);

            operationResponse = { sucesso: true, retorno, headers };
        } else {
            operationResponse = { sucesso: false };

            if (response.status === 404) {
                operationResponse['status'] = 404;
            } else {
                await this._trataErroPadrao(response, operationResponse);
            }
        }

        return operationResponse;

    }

    async getBlob(url, tipo) {
        let operationResponse = {};
        const response = await fetch(`/api/${url}`, { headers: this._authHeader({}) });

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
            }

            await this._trataErroPadrao(response, operationResponse);

        }

        return operationResponse;

    }

    async  post(url, objBody) {
        return this._pxxt('POST', url, objBody);
    }

    async  put(url, objBody) {
        return this._pxxt('PUT', url, objBody);
    }

    async _pxxt(metodo, url, objBody) {
        let operationResponse = {};
        const body = JSON.stringify(objBody);

        const response = await fetch(`/api/${url}`, { body, method: metodo, headers: this._authHeader({ 'Content-Type': 'application/json' }) });

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

    _trataParametrosRequest(url, params) {
        if (url.includes('?')) {
            url += '&';
        } else {
            url += '?';
        }

        return `${url}${params}`;
    }

    _paginar(paginacao) {
        if (paginacao != null && paginacao.habilitada) {
            const pagAtual = paginacao.pagina > 0 ? paginacao.pagina : 1;

            return `pagina=${pagAtual}&registros=${paginacao.registros}`
        }

        return '';
    }

    _headersSGQ(response) {
        const reqHeaders = response.headers;
        let headers = {};

        for (let h of reqHeaders.entries()) {
            if (h[0].includes("x-sgq")) {
                headers[h[0]] = h[1];
            }
        }

        return headers;
    }

    _authHeader(headers) {
        headers['Authorization'] = `Bearer ${_lm.token()}`;

        return headers
    }

    async _trataErroPadrao(response, operationResponse = {}) {
        let erro = `Erro no servidor: ${response.status}`;

        try {
            const rJson = await response.json();

            if (rJson != null) {
                let detalhe = rJson.erroCompleto;

                erro = rJson.erro || rJson.error;

                if (detalhe != null && detalhe.length <= 150) {
                    erro = detalhe;
                }
            }
        } catch (ex) {
            console.error(`Erro tratando retorno de exceção: ${ex}`);
        }

        operationResponse['erroDetalhado'] = erro;

    }
}

export default HttpClient;