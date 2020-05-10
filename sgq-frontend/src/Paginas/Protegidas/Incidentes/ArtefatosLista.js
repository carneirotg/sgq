import React, { Component } from 'react';
import { Button, Container, Form, Pagination, Table, Row, Col } from 'react-bootstrap';

import './Artefato.css';

import { cliente } from '../../../Componentes/SGQClient';
import ToastManager from '../../../Componentes/ToastManager';

class ArtefatosLista extends Component {

    state = {
        buscaArtefato: '',
        artefatos: [],
        pagina: {
            habilitada: true,
            pagina: 0,
            paginas: 0,
            total: 0,
            registros: 5
        }
    }

    timer = null;

    async componentDidMount() {
        this._consultar();
    }

    async _depreciar(artefato) {
        const resp = await cliente().artefatos.depreciar(artefato.id);

        if (resp.sucesso) {
            ToastManager.atencao("Artefato foi marcado como depreciado.");

            this.setState({
                artefatos: this.state.artefatos.map((it, ix) => {
                    if (it.id === artefato.id) {
                        it.depreciado = true;
                    }
                    return it;
                })
            });
        }
    }

    async _consultar(params = null) {

        const paramsPaginacao = params != null ? params : this.state.pagina;
        const nome = this.state.buscaArtefato;
        let resp;

        if (this.state.buscaArtefato !== '') {
            resp = await cliente().artefatos.buscarNome(nome, paramsPaginacao);
        } else {
            resp = await cliente().artefatos.listar(paramsPaginacao);
        }

        if (resp.sucesso) {
            const artefatos = resp.retorno;
            const { pagina } = paramsPaginacao;

            const hPagina = parseInt(resp.headers['x-sgq-pagina']);
            const hPaginas = parseInt(resp.headers['x-sgq-paginas']);
            const hTotal = parseInt(resp.headers['x-sgq-total']);

            if (this._atualizarArtefatosPaginas(pagina, paramsPaginacao, hPagina)) {
                this.setState(
                    {
                        artefatos: artefatos,
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
                ToastManager.atencao(`Sem resultados`);
                this.setState({ artefatos: [], buscaArtefato: '' });

                setTimeout(() => this._consultar(), 1000);
            }
        }
    }

    async _paginar(pag) {
        if (parseInt(this.state.pagina.pagina) !== pag) {
            const paramsPaginacao = { ...this.state.pagina, pagina: pag };
            this._consultar(paramsPaginacao);
        }
    }

    _atualizarArtefatosPaginas(pagina, paramsPaginacao, hPagina) {
        return parseInt(pagina) === 0 || parseInt(paramsPaginacao.pagina) === parseInt(hPagina)
    }

    _valores(event) {
        const name = event.target.name;
        const value = event.target.value;

        this.setState({ ...this.state, [name]: value });

        if (this.state.buscaArtefato.length > 3) {
            clearTimeout(this.timer);
            this.timer = setTimeout(() => this._consultar(), 300)
        } else if(this.state.buscaArtefato.length === 0){
            this._consultar();
        }
    }

    _simNao(val) {
        return val ? "Sim" : "Não";
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
        } else if(direcao === -1 && pagina > 1) {
            this._paginar(pagina - 1);
        }
    }

    render() {

        let arts = this.state.artefatos;

        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">

                        <Row>
                            <Col md><h1>Artefatos</h1></Col>
                        </Row>
                        <Row>
                            <Col md></Col>
                            <Col md="2">
                                <Form.Group controlId="buscaArtefato">
                                    <Form.Label>Buscar por</Form.Label>
                                    <Form.Control type="text" name="buscaArtefato" value={this.state.buscaArtefato} onChange={this._valores.bind(this)} />
                                </Form.Group>
                            </Col>
                            <Col md="2"></Col>
                        </Row>
                        <Row>
                            <Col md="2"></Col>
                            <Col md>
                                <Table striped bordered hover>
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Nome</th>
                                            <th>Descrição</th>
                                            <th style={{ textAlign: 'center' }}>Depreciado</th>
                                            <th style={{ textAlign: 'center' }}>Ação</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            arts.map(a => (
                                                <tr key={a.id}>
                                                    <td>{a.id}</td>
                                                    <td>{a.nome}</td>
                                                    <td>{a.descricao}</td>
                                                    <td style={{ textAlign: 'center' }}>{this._simNao(a.depreciado)}</td>
                                                    <td style={{ textAlign: 'center' }}>{
                                                        !a.depreciado ?
                                                            <Button variant="danger" onClick={this._depreciar.bind(this, a)}>Depreciar</Button>
                                                            :
                                                            ''
                                                    }</td>
                                                </tr>
                                            ))
                                        }
                                    </tbody>
                                </Table>
                            </Col>
                            <Col md="2"></Col>
                        </Row>
                        <Row>
                            <Col md="2"></Col>
                            <Col>{this._paginador()}</Col>
                            <Col md="2"></Col>
                        </Row>

                    </div>
                </Container>
            </div>
        );
    }

}

export default ArtefatosLista;