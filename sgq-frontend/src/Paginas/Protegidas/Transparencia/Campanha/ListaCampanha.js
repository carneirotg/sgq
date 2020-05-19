import React, { Component } from 'react';
import { FaEdit, FaAngleDoubleRight, FaEye } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Tabs, Tab, Table, Alert, Modal, Pagination, Card } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';
import { parseDateOnly } from '../../../../Componentes/ParseDate';

import NOMES from '../../../../Componentes/nomes.json';
import { Link } from 'react-router-dom';


class ListaCampanha extends Component {

    PAGINA_INICIAL = {
        habilitada: true,
        pagina: 0,
        paginas: 0,
        total: 0,
        registros: 10
    }

    state = {
        tipo: 'ativas',
        buscaCampanha: '',
        campanhas: [],
        pagina: {
            habilitada: true,
            pagina: 0,
            paginas: 0,
            total: 0,
            registros: 10
        },
        modalEstado: {
            campanha: null,
            estado: null,
            visivel: false
        },
        modalVisualizar: {
            campanha: null,
            visivel: false
        }
    }

    timerBusca = null;

    async componentDidMount() {
        this._carregaCampanhasEscopo(this.state.tipo);
    }

    async _carregaCampanhasEscopo(tipo, params = null) {
        if (this.state.tipo !== tipo) {
            this.setState({
                ...this.setState,
                tipo,
                pagina: this.PAGINA_INICIAL
            })
        }

        const paramsPaginacao = params != null ? params : this.state.pagina;
        const { buscaCampanha } = this.state;

        const camCli = cliente().campanhas;
        let resp = null;

        if (buscaCampanha !== '') {
            resp = await camCli.consultaEstadoTitulo(tipo, buscaCampanha, params);
        } else {
            resp = await camCli.consultaEstado(tipo, params);
        }

        if (resp.sucesso) {
            const { pagina } = paramsPaginacao;

            const campanhas = resp.retorno;

            const hPagina = parseInt(resp.headers['x-sgq-pagina']);
            const hPaginas = parseInt(resp.headers['x-sgq-paginas']);
            const hTotal = parseInt(resp.headers['x-sgq-total']);

            if (this._atualizarPaginas(pagina, paramsPaginacao, hPagina)) {
                this.setState(
                    {
                        campanhas,
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
                ToastManager.atencao("Não existem campanhas no estado desejado");
                this.setState({ ...this.state, campanhas: [] });
            }
        }
    }

    _atualizarPaginas(pagina, paramsPaginacao, hPagina) {
        return parseInt(pagina) === 0 || parseInt(paramsPaginacao.pagina) === parseInt(hPagina)
    }

    async _paginar(pag) {
        if (parseInt(this.state.pagina.pagina) !== pag) {
            const paramsPaginacao = { ...this.state.pagina, pagina: pag };
            const { tipo } = this.state;

            this._carregaCampanhasEscopo(tipo, paramsPaginacao);
        }
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

    _valoresCampanha(evt) {
        const { name, value } = evt.target;

        this.setState({ ...this.state, [name]: value });

        clearTimeout(this.timerBusca);
        if (value.length >= 3) {
            this.timerBusca = setTimeout(() => this._carregaCampanhasEscopo(this.state.tipo, null), 300);
        } else if (value === '' || value.length === 0) {
            this.timerBusca = setTimeout(() => this._carregaCampanhasEscopo(this.state.tipo, this.PAGINA_INICIAL), 300);
        }
    }

    _confirmaMudancaEstado(campanha, estado) {

        if (campanha !== null) {
            this.setState({
                modalEstado: {
                    campanha, estado,
                    visivel: true
                }
            });
        } else {
            this.setState({
                modalEstado: {
                    campanha: null,
                    estado: null,
                    visivel: false
                }
            });
        }
    }

    async _mudarEstadoCampanha(campanha, estado, nomeEstado) {
        const camCli = cliente().campanhas;
        const resp = await camCli.concluir(campanha.id);

        if (resp.sucesso) {
            ToastManager.informativo(`Campanha movida para fase de ${nomeEstado}`);
            this.setState({ ...this.state, campanhas: this.state.campanhas.filter(i => i.id !== campanha.id) });
        } else if (resp.status === 403) {
            ToastManager.atencao("Seu perfil não possui autorização para esta operação");
        } else {
            ToastManager.erro(resp.erroDetalhado);
        }

        this._confirmaMudancaEstado(null);
    }

    _visualizaCampanha(campanha) {

        if (campanha !== null) {
            this.setState({
                modalVisualizar: {
                    campanha,
                    visivel: true
                }
            });
        } else {
            this.setState({
                modalVisualizar: {
                    campanha: null,
                    visivel: false
                }
            });
        }
    }

    render() {
        return (
            <div className="App">
                <Container>
                    <Row>
                        <Col md><h1 style={{ textAlign: 'center' }}>Campanhas</h1></Col>
                    </Row>
                    <Row>
                        <Col md="3"></Col>
                        <Col md="6">
                            <Form.Group controlId="paginador">
                                <Col>{this.state.campanhas.length > 0 ? (this._paginador()) : (<></>)}</Col>
                            </Form.Group>
                        </Col>
                        <Col md="2">
                            <Form.Group controlId="buscaCampanhas">
                                <Form.Control placeholder="Termos campanhas" type="text" name="buscaCampanha" value={this.state.buscaCampanha} onChange={this._valoresCampanha.bind(this)} />
                            </Form.Group>
                        </Col>

                    </Row>
                    <Row>
                        <Col md></Col>
                        <Col md="10">
                            <Tabs defaultActiveKey="abertos" id="tabcampanhas" onSelect={(tab) => this._carregaCampanhasEscopo(tab)}>
                                <Tab eventKey="ativas" title="Ativas" />
                                <Tab eventKey="concluidas" title="Concluídas" />
                            </Tabs>
                            {this._SecaoEstados()}
                        </Col>
                        <Col md></Col>
                    </Row>
                </Container>
                {this._ModalEstado()}
                {this._ModalVisualizar()}
            </div>
        );
    }

    _SecaoEstados() {

        const { tipo } = this.state;

        return (
            <Table responsive striped hover size="lg">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Tipo de Risco</th>
                        <th>Início Campanha</th>
                        <th>Fim Campanha</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        this.state
                            .campanhas
                            .map(c => (
                                <tr key={c.id}>
                                    <td>{c.id}</td>
                                    <td>{c.titulo}</td>
                                    <td>{NOMES[c.tipoRisco].tipo}</td>
                                    <td>{parseDateOnly(c.inicio)}</td>
                                    <td>{parseDateOnly(c.fim)}</td>
                                    <td>
                                        <Button variant="success" onClick={this._visualizaCampanha.bind(this, c)}><FaEye /></Button>{' '}
                                        {
                                            tipo !== 'concluidas' ?
                                                (
                                                    <>
                                                        <Button variant="primary" as={Link} to={`/dashboard/campanha/${c.id}`}><FaEdit /></Button>{' '}
                                                    </>
                                                ) : (<></>)
                                        }
                                        {
                                            tipo === 'ativas' ?
                                                (
                                                    <>
                                                        <Button variant="warning" onClick={this._confirmaMudancaEstado.bind(this, c, 'concluidas')}><FaAngleDoubleRight /></Button>
                                                    </>
                                                ) : (<></>)
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

        const { campanha, estado, visivel } = this.state.modalEstado;
        let novoEstado = null;
        let nomeEstado = '';

        if (estado === 'abertas') {
            novoEstado = 'aberta';
            nomeEstado = 'Abertas';
        } else if (estado === 'concluidas') {
            novoEstado = 'concluida';
            nomeEstado = 'Concluída';
        }

        return (
            <div>
                {
                    campanha ?
                        (
                            <Modal show={visivel} onHide={this._confirmaMudancaEstado.bind(this, null)} animation={false} size="lg">
                                <Modal.Header closeButton>
                                    <Modal.Title>Mudar Estado de Incidente?</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p>Deseja mover o Incidente abaixo para o estado de <b>{nomeEstado}</b>?<br /><br />{campanha.titulo} - {NOMES[campanha.tipoRisco].tipo}</p>{
                                                novoEstado === 'concluida' ?
                                                    (<Alert variant="danger">A conclusão de uma Campanha não poderá ser desfeita.</Alert>) : (<></>)
                                            }
                                            </Col>

                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._confirmaMudancaEstado.bind(this, null)}>
                                        Fechar
                                    </Button>
                                    <Button variant="danger" onClick={this._mudarEstadoCampanha.bind(this, campanha, novoEstado, nomeEstado)}>
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

    _ModalVisualizar() {
        const { campanha, visivel } = this.state.modalVisualizar;

        return (
            <div>
                {
                    campanha ?
                        (
                            <Modal show={visivel} onHide={this._visualizaCampanha.bind(this, null)} animation={false} size="lg">
                                <Modal.Header closeButton>
                                    <Modal.Title>{campanha.titulo}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>

                                        <Row>
                                            <Col md></Col>
                                            <Col md="6">
                                                <p><b>Título: </b><br />{campanha.titulo}</p>
                                            </Col>
                                            <Col md="4">
                                                <p><b>Período da Campanha: </b><br />{parseDateOnly(campanha.inicio)} a {parseDateOnly(campanha.fim)}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md></Col>
                                            <Col md="4">
                                                <p><b>Defeito Apontado: </b><br />{campanha.defeito}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Constatado em: </b><br />{parseDateOnly(campanha.dataConstatacao)}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Tipo de Risco: </b><br />{NOMES[campanha.tipoRisco].tipo}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="8">
                                                <p><b>Texto Informativo: </b><br />{campanha.informativoCampanha}</p>
                                            </Col>
                                            <Col md="3">
                                                <br />
                                                <p><b>Incidentes Conhecidos? </b><br />{campanha.incidentesConhecidos ? 'Sim' : 'Não'}</p>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="8">
                                                <p><b>Medidas Corretivas: </b><br />{campanha.medidasCoretivas}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md></Col>
                                            <Col md="10">
                                                <Form.Group controlId="gncsEnvolvidas">
                                                    <br />
                                                    <Row>
                                                        {campanha.ncsEnvolvidas.map(nc => (
                                                            <Col md="4" sm="6" key={nc.id}>
                                                                <Card >
                                                                    <Card.Body>
                                                                        <Card.Title>{nc.titulo} <sup style={{ color: 'red' }}></sup></Card.Title>
                                                                        <Card.Subtitle className="mb-2 text-muted">[{nc.id}] {nc.resumo}</Card.Subtitle>
                                                                    </Card.Body>
                                                                </Card>
                                                            </Col>
                                                        ))}
                                                    </Row>
                                                </Form.Group>
                                            </Col>
                                            <Col md></Col>
                                        </Row>

                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._visualizaCampanha.bind(this, null)}>
                                        Fechar
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

export default ListaCampanha;