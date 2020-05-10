import React, { Component } from 'react';
import { FaEdit, FaAngleRight, FaAngleLeft, FaAngleDoubleRight, FaEye } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Tabs, Tab, Table, Alert, Modal, Pagination } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';

import NOMES from './nomes.json';


class ListaNC extends Component {

    state = {
        tipo: 'abertas',
        buscaNC: '',
        ncs: [],
        pagina: {
            habilitada: true,
            pagina: 0,
            paginas: 0,
            total: 0,
            registros: 10
        },
        modalEstado: {
            nc: null,
            estado: null,
            visivel: false
        }

    }

    async componentDidMount() {
        this._carregaNCEscopo(this.state.tipo);
    }

    _valoresNC() {

    }

    async _carregaNCEscopo(tipo, params = null) {

        console.log(`${tipo} :: ${params}`);

        if (this.state.tipo !== tipo) {
            this.setState({
                ...this.setState,
                tipo,
                pagina: {
                    habilitada: true,
                    pagina: 0,
                    paginas: 0,
                    total: 0,
                    registros: 10
                }
            })
        }

        const paramsPaginacao = params != null ? params : this.state.pagina;

        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.consultaEstado(tipo, params);

        if (resp.sucesso) {
            const { pagina } = paramsPaginacao;

            const ncs = resp.retorno;

            const hPagina = parseInt(resp.headers['x-sgq-pagina']);
            const hPaginas = parseInt(resp.headers['x-sgq-paginas']);
            const hTotal = parseInt(resp.headers['x-sgq-total']);

            if (this._atualizarPaginas(pagina, paramsPaginacao, hPagina)) {
                this.setState(
                    {
                        ncs,
                        tipo,
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
                ToastManager.atencao("Não existem não conformidades no estado desejado");
                this.setState({ ...this.state, ncs: [] });
            }
        }
    }

    _atualizarPaginas(pagina, paramsPaginacao, hPagina) {
        console.log(`_atualizarPaginas(${pagina}, ${JSON.stringify(paramsPaginacao)}, ${hPagina})`)
        return parseInt(pagina) === 0 || parseInt(paramsPaginacao.pagina) === parseInt(hPagina)
    }

    async _paginar(pag) {
        if (parseInt(this.state.pagina.pagina) !== pag) {
            const paramsPaginacao = { ...this.state.pagina, pagina: pag };
            const { tipo } = this.state;

            this._carregaNCEscopo(tipo, paramsPaginacao);
        }
    }

    _confirmaMudancaEstado(nc, estado) {

        if (nc !== null) {
            this.setState({
                modalEstado: {
                    nc, estado,
                    visivel: true
                }
            });
        } else {
            this.setState({
                modalEstado: {
                    nc: null,
                    estado: null,
                    visivel: false
                }
            });
        }
    }

    async _mudarEstadoNC(nc, estado, nomeEstado) {
        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.mudarEstado(nc.id, estado);

        if (resp.sucesso) {
            ToastManager.informativo(`Não conformidade movida para fase de ${nomeEstado}`);
            this.setState({ ...this.state, ncs: this.state.ncs.filter(n => n.id != nc.id) });
        }

        this._confirmaMudancaEstado(null);
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
        return (
            <div className="App">
                <Container>
                    <Row>
                        <Col md><h1 style={{ textAlign: 'center' }}>Não Conformidades</h1></Col>
                    </Row>
                    <Row>
                        <Col md="3"></Col>
                        <Col md="6">
                            <Form.Group controlId="paginador">
                                <Col>{this.state.ncs.length > 0 ? (this._paginador()) : (<></>)}</Col>
                            </Form.Group>
                        </Col>
                        <Col md="3">
                            <Form.Group controlId="buscaNC">
                                <Form.Control placeholder="DETRAN" type="text" name="buscaNC" value={this.state.buscaNC} onChange={this._valoresNC.bind(this)} />
                            </Form.Group>
                        </Col>

                    </Row>
                    <Row>
                        <Col md></Col>
                        <Col md="10">
                            <Tabs defaultActiveKey="abertas" id="tabNCS" onSelect={(tab) => this._carregaNCEscopo(tab)}>
                                <Tab eventKey="abertas" title="Abertas" />
                                <Tab eventKey="em_analise" title="Em Análise" />
                                <Tab eventKey="concluidas" title="Concluídas" />
                            </Tabs>
                            {this._SecaoEstados()}
                        </Col>
                        <Col md></Col>
                    </Row>
                </Container>
                {this._ModalEstado()}
            </div>
        );
    }

    _SecaoEstados() {

        const { tipo } = this.state;

        return (
            <Table responsive striped hover>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Setor Afetado</th>
                        <th>Tipo</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        this.state
                            .ncs
                            .map(n => (
                                <tr key={n.id}>
                                    <td>{n.id}</td>
                                    <td>{n.titulo}</td>
                                    <td>{NOMES[n.setor]}</td>
                                    <td>{NOMES[n.tipoNaoConformidade]}</td>
                                    <td>
                                        <Button variant="success" onClick={() => false}><FaEye /></Button>{' '}
                                        {
                                            tipo !== 'concluidas' ?
                                                (
                                                    <>
                                                        <Button variant="primary" onClick={() => false}><FaEdit /></Button>{' '}
                                                    </>
                                                ) : (<></>)
                                        }
                                        {
                                            tipo === 'abertas' ?
                                                (
                                                    <>
                                                        <Button variant="info" onClick={this._confirmaMudancaEstado.bind(this, n, 'em_analise')}><FaAngleRight /></Button>{' '}
                                                        <Button variant="warning" onClick={this._confirmaMudancaEstado.bind(this, n, 'concluidas')}><FaAngleDoubleRight /></Button>
                                                    </>
                                                ) : tipo === 'em_analise' ?
                                                    (
                                                        <>
                                                            <Button variant="info" onClick={this._confirmaMudancaEstado.bind(this, n, 'abertas')}><FaAngleLeft /></Button>{' '}
                                                            <Button variant="info" onClick={this._confirmaMudancaEstado.bind(this, n, 'concluidas')}><FaAngleRight /></Button>
                                                        </>
                                                    ) : (
                                                        <></>
                                                    )
                                        }

                                    </td>
                                </tr>
                            )
                            )
                    }
                </tbody>
            </Table>
        )
    }

    _ModalEstado() {

        const { nc, estado, visivel } = this.state.modalEstado;
        let novoEstado = null;
        let nomeEstado = '';

        if (estado === 'abertas') {
            novoEstado = 'aberta';
            nomeEstado = 'Aberta';
        } else if (estado === 'em_analise') {
            novoEstado = estado;
            nomeEstado = 'Análise';
        } else if (estado === 'concluidas') {
            novoEstado = 'concluida';
            nomeEstado = 'Concluída';
        }

        return (
            <div>
                {
                    nc ?
                        (
                            <Modal show={visivel} onHide={this._confirmaMudancaEstado.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Mudar Estado de NC?</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p>Deseja mover a Não Conformidade abaixo para o estado de <b>{nomeEstado}</b>?<br /><br />{nc.titulo} - {NOMES[nc.tipoNaoConformidade]}</p>{
                                                novoEstado === 'concluida' ?
                                                    (<Alert variant="danger">A conclusão de uma Não Conformidade não poderá ser desfeita.</Alert>) : (<></>)
                                            }
                                            </Col>

                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._confirmaMudancaEstado.bind(this, null)}>
                                        Fechar
                                    </Button>
                                    <Button variant="danger" onClick={this._mudarEstadoNC.bind(this, nc, novoEstado, nomeEstado)}>
                                        Sim
                                    </Button>
                                </Modal.Footer>
                            </Modal>
                        )
                        :
                        (<></>)
                }
            </div>
        )
    }


}

export default ListaNC;