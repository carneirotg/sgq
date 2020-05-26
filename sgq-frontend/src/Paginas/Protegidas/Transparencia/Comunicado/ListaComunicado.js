import React, { Component } from "react";
import { cliente } from "../../../../Componentes/SGQClient";
import { parseDate } from '../../../../Componentes/ParseDate';
import ToastManager from '../../../../Componentes/ToastManager';

import { Pagination, Container, Row, Col, Table } from 'react-bootstrap';

class ListaComunicado extends Component {

    state = {
        comunicados: [],
        pagina: {
            habilitada: true,
            pagina: 0,
            paginas: 0,
            total: 0,
            registros: 10
        }
    }

    async componentDidMount() {
        this._consultar();
    }

    async _consultar(params = null) {

        const paramsPaginacao = params != null ? params : this.state.pagina;
        const resp = await cliente().comunicados.listar(paramsPaginacao);

        if (resp.sucesso) {
            const comunicados = resp.retorno;
            const { pagina } = paramsPaginacao;

            const hPagina = parseInt(resp.headers['x-sgq-pagina']);
            const hPaginas = parseInt(resp.headers['x-sgq-paginas']);
            const hTotal = parseInt(resp.headers['x-sgq-total']);

            if (this._atualizarComunicadosPaginas(pagina, paramsPaginacao, hPagina)) {
                this.setState(
                    {
                        comunicados: comunicados,
                        pagina: {
                            ...this.state.pagina,
                            pagina: hPagina,
                            paginas: hPaginas,
                            total: hTotal,
                        }
                    });
            }
        } else {
            if (resp.status === 404) {
                ToastManager.atencao(`Sem resultados de envios`);
                this.setState({ comunicados: [] });
            }
        }
    }

    async _paginar(pag) {
        if (parseInt(this.state.pagina.pagina) !== pag) {
            const paramsPaginacao = { ...this.state.pagina, pagina: pag };
            this._consultar(paramsPaginacao);
        }
    }

    _atualizarComunicadosPaginas(pagina, paramsPaginacao, hPagina) {
        return parseInt(pagina) === 0 || parseInt(paramsPaginacao.pagina) === parseInt(hPagina)
    }

    _paginador() {
        let itens = [];

        const { pagina, paginas } = this.state.pagina;

        itens.push(<Pagination.Item key="-1" onClick={this._paginar.bind(this, 1)}>|&lt;</Pagination.Item>);
        itens.push(<Pagination.Item key="0" onClick={this._navegar.bind(this, -1)}>&lt;</Pagination.Item>);

        for (let i = 1; i <= paginas; i++) {
            itens.push(
                <Pagination.Item key={i} active={i === parseInt(pagina)} onClick={this._paginar.bind(this, i)}>{i}</Pagination.Item>
            );
        }

        itens.push(<Pagination.Item key="1000" onClick={this._navegar.bind(this, 1)}>&gt;</Pagination.Item>);
        itens.push(<Pagination.Item key="10000" onClick={this._paginar.bind(this, this.state.pagina.paginas)}>&gt;|</Pagination.Item>);

        return (
            <Pagination>{itens}</Pagination>
        )

    }

    _navegar(direcao) {
        const { pagina, paginas } = this.state.pagina;

        if (direcao === 1 && pagina < paginas) {
            this._paginar(pagina + 1);
        } else if (direcao === -1 && pagina > 1) {
            this._paginar(pagina - 1);
        }
    }

    render() {

        let { comunicados } = this.state;

        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">

                        <Row>
                            <Col md><h1>Comunicados Enviados</h1></Col>
                        </Row>
                        <Row>
                            <Col md="1"></Col>
                            <Col>{this.state.comunicados.length > 0 ? (this._paginador()) : (<></>)}</Col>
                            <Col md="1"></Col>
                        </Row>
                        <Row>
                            <Col md="1"></Col>
                            <Col md className="overflow-auto">
                                <Table striped bordered hover>
                                    <thead>
                                        <tr>
                                            <th>Id. Comunicado</th>
                                            <th>Data</th>
                                            <th>Tipo</th>
                                            <th>Título</th>
                                            <th>Destinatário</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            comunicados.map(c => (
                                                <tr key={c.id}>
                                                    <td>{c.id}</td>
                                                    <td>{parseDate(c.dataEnvio)}</td>
                                                    <td>{c.comunicadoTipo}</td>
                                                    <td>{c.comunicadoTitulo}</td>
                                                    <td>{c.destinatario}</td>
                                                </tr>
                                            ))
                                        }
                                    </tbody>
                                </Table>
                            </Col>
                            <Col md="1"></Col>
                        </Row>
                    </div>
                </Container>
            </div>
        );
    }

}

export default ListaComunicado;