import HttpClient from './HttpClient';

const http = new HttpClient();

const artefatos = {
    salvar: async (artefato) => {
        return http.post('artefatos', artefato);
    },
    buscarNome: async (nome, paginacao) => {
        return http.get(`artefatos?nome=${nome}`, paginacao);
    },
    listar: async (paginacao) => {
        return http.get('artefatos', paginacao);
    },
    consulta: async (id) => {
        return http.get(`artefatos/${id}`);
    },
    depreciar: async (id) => {
        return http.patch(`artefatos/${id}/depreciado`);
    }
}

const normas = {
    listar: async () => {
        return http.get('normas');
    },
    consulta: async (id) => {
        return http.get(`normas/${id}`);
    }
}

const destinatarios = {
    listar: async () => {
        return http.get('destinatarios');
    },
    salvar: async (destinatario) => {
        const id = destinatario.id;
        let resp;

        if (id === null || id === 0) {
            resp = http.post(`destinatarios`, destinatario);
        } else {
            resp = http.put(`destinatarios/${id}`, destinatario);
        }

        return resp;
    },
    remover: async (id) => {
        return http.delete(`destinatarios/${id}`);
    }
}

const incidentes = {
    salvar: (incidente) => {
        const id = incidente.id;
        let resp;

        if (id === null || id === 0) {
            resp = http.post(`incidentes`, incidente);
        } else {
            resp = http.put(`incidentes/${id}`, incidente);
        }

        return resp;
    },
    consultaId: async (id) => {
        return http.get(`incidentes/${id}`);
    },
    consultaEstado: async (estado, pagina) => {
        return http.get(`incidentes/?estado=${estado}`, pagina);
    },
    consultaEstadoTitulo: async (estado, titulo) => {
        return http.get(`incidentes/?estado=${estado}&titulo=${titulo}`);
    },
    mudarEstado: async (id, estado) => {
        return http.patch(`incidentes/${id}/estado/${estado}`);
    }
}

const naoConformidades = {
    salvar: async (nc) => {
        const id = nc.id;
        let resp;

        if (id === null || id === 0) {
            resp = http.post(`ncs`, nc);
        } else {
            resp = http.put(`ncs/${id}`, nc);
        }

        return resp;
    },
    consultaEstado: async (estado, pagina) => {
        return http.get(`ncs/?estado=${estado}`, pagina);
    },
    consultaEstadoTitulo: async (estado, titulo) => {
        return http.get(`ncs/?estado=${estado}&titulo=${titulo}`);
    },
    consultaConcluidaTitulo: async (titulo) => {
        return http.get(`ncs/?estado=concluidas&titulo=${titulo}`);
    },
    consultaTitulo: async (titulo) => {
        return http.get(`ncs/?titulo=${titulo}`);
    },
    consultaId: async (id) => {
        return http.get(`ncs/${id}`);
    },
    mudarEstado: async (id, estado) => {
        return http.patch(`ncs/${id}/estado/${estado}`);
    },
    atualizarChecklist: async (id, cl) => {
        return http.patchBody(`/ncs/${id}/norma/checklist`, cl);
    }
}

const relatorios = {
    gerar: async (tipo, periodo = null) => {

        switch (tipo) {
            case "mesAtual": return http.getBlob("relatorios/incidentes", tipo);
            case "anoAtual": return http.getBlob("relatorios/incidentes/ano-corrente", tipo);
            case "seisMeses": return http.getBlob("relatorios/incidentes/ultimos-seis-meses", tipo);
            case "dozeMeses": return http.getBlob("relatorios/incidentes/ultimos-doze-meses", tipo);
            case "periodo": {
                const inicio = periodo.inicio.toISOString().split('T')[0];
                const fim = periodo.fim.toISOString().split('T')[0];

                return http.getBlob(`relatorios/incidentes/de/${inicio}/ate/${fim}`, tipo);
            }
        }
    }
}

const campanhas = {
    salvar: async (camp) => {
        const id = camp.id;
        let resp;

        if (id === null || id === 0) {
            resp = http.post(`campanhas`, camp);
        } else {
            resp = http.patchBody(`campanhas/${id}`, camp);
        }

        return resp;
    },
    consultaId: async(id) => {
        return http.get(`campanhas/${id}`);
    },
    consultaEstado: async (estado, pagina) => {
        return http.get(`campanhas/?estado=${estado}`, pagina);
    },
    consultaEstadoTitulo: async (estado, titulo) => {
        return http.get(`campanhas/?estado=${estado}&titulo=${titulo}`);
    },
    concluir: async (id) => {
        return http.patch(`campanhas/${id}/estado/concluida`)
    }
}

const eventos = {
    consultaUltimosIncidentes: async() => {
        return http.get('/eventos')
    }
}

export function cliente() {
    return {
        artefatos, normas, destinatarios, relatorios, naoConformidades, incidentes, eventos, campanhas
    }
}