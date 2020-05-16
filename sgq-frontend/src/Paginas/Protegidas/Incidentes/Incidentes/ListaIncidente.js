import React, { Component } from 'react';
import { FaEdit, FaAngleRight, FaAngleLeft, FaAngleDoubleRight, FaEye, FaTasks } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Tabs, Tab, Table, Alert, Modal, Pagination, Card } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';

import NOMES from '../../../../Componentes/nomes.json';
import { Link } from 'react-router-dom';

class ListaIncidente extends Component {

    PAGINA_INICIAL = {
        habilitada: true,
        pagina: 0,
        paginas: 0,
        total: 0,
        registros: 10
    }

    state = {
        tipo: 'abertos',
        buscaIncidente: '',
        incidentes: [],
        pagina: {
            habilitada: true,
            pagina: 0,
            paginas: 0,
            total: 0,
            registros: 10
        },
        modalEstado: {
            incidente: null,
            estado: null,
            visivel: false
        },
        modalVisualizar: {
            incidente: null,
            visivel: false
        }
    }

    timer = null;

    async componentDidMount() {
        this._carregaIncidenteEscopo(this.state.tipo);
    }

    async _carregaIncidenteEscopo(tipo, params = null) {
        if (this.state.tipo !== tipo) {
            this.setState({
                ...this.setState,
                tipo,
                pagina: this.PAGINA_INICIAL
            })
        }

        const paramsPaginacao = params != null ? params : this.state.pagina;
        const { buscaIncidente } = this.state;

        const incCli = cliente().incidentes;
        let resp = null;

        if (buscaIncidente !== '') {
            resp = await incCli.consultaEstadoTitulo(tipo, buscaIncidente, params);
        } else {
            resp = await incCli.consultaEstado(tipo, params);
        }

        if (resp.sucesso) {
            const { pagina } = paramsPaginacao;

            const incidentes = resp.retorno;

            const hPagina = parseInt(resp.headers['x-sgq-pagina']);
            const hPaginas = parseInt(resp.headers['x-sgq-paginas']);
            const hTotal = parseInt(resp.headers['x-sgq-total']);

            if (this._atualizarPaginas(pagina, paramsPaginacao, hPagina)) {
                this.setState(
                    {
                        incidentes,
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
                ToastManager.atencao("Não existem não incidentes no estado desejado");
                this.setState({ ...this.state, ncs: [] });
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

            this._carregaIncidenteEscopo(tipo, paramsPaginacao);
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

    _valoresIncidente(evt) {
        const { name, value } = evt.target;

        this.setState({ ...this.state, [name]: value });

        clearTimeout(this.timer);
        if (value.length >= 3) {
            this.timer = setTimeout(() => this._carregaNCEscopo(this.state.tipo, null), 300);
        } else if (value === '' || value.length === 0) {
            this.timer = setTimeout(() => this._carregaNCEscopo(this.state.tipo, this.PAGINA_INICIAL), 300);
        }
    }

    _confirmaMudancaEstado(incidente, estado) {

        if (incidente !== null) {
            this.setState({
                modalEstado: {
                    incidente, estado,
                    visivel: true
                }
            });
        } else {
            this.setState({
                modalEstado: {
                    incidente: null,
                    estado: null,
                    visivel: false
                }
            });
        }
    }

    async _mudarEstadoIncidente(incidente, estado, nomeEstado) {
        const incCli = cliente().incidentes;
        const resp = await incCli.mudarEstado(incidente.id, estado);

        if (resp.sucesso) {
            ToastManager.informativo(`Incidente movida para fase de ${nomeEstado}`);
            this.setState({ ...this.state, incidentes: this.state.incidentes.filter(i => i.id !== incidente.id) });
        } else if (resp.status === 403) {
            ToastManager.atencao("Seu perfil não possui autorização para esta operação");
        } else {
            ToastManager.erro(resp.erroDetalhado);
        }

        this._confirmaMudancaEstado(null);
    }

    _visualizaIncidente(incidente) {

        if (incidente !== null) {
            this.setState({
                modalVisualizar: {
                    incidente,
                    visivel: true
                }
            });
        } else {
            this.setState({
                modalVisualizar: {
                    incidente: null,
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
                        <Col md><h1 style={{ textAlign: 'center' }}>Incidentes</h1></Col>
                    </Row>
                    <Row>
                        <Col md="3"></Col>
                        <Col md="6">
                            <Form.Group controlId="paginador">
                                <Col>{this.state.incidentes.length > 0 ? (this._paginador()) : (<></>)}</Col>
                            </Form.Group>
                        </Col>
                        <Col md="2">
                            <Form.Group controlId="buscaNC">
                                <Form.Control placeholder="Termos incidentes" type="text" name="buscaIncidente" value={this.state.buscaIncidente} onChange={this._valoresIncidente.bind(this)} />
                            </Form.Group>
                        </Col>

                    </Row>
                    <Row>
                        <Col md></Col>
                        <Col md="10">
                            <Tabs defaultActiveKey="abertos" id="tabincidentes" onSelect={(tab) => this._carregaIncidenteEscopo(tab)}>
                                <Tab eventKey="abertos" title="Abertos" />
                                <Tab eventKey="em_analise" title="Em Análise" />
                                <Tab eventKey="concluidos" title="Concluídos" />
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
                        <th>Setor Afetado</th>
                        <th>Tipo</th>
                        <th>Classificação</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        this.state
                            .incidentes
                            .map(i => (
                                <tr key={i.id}>
                                    <td>{i.id}</td>
                                    <td>{i.titulo}</td>
                                    <td>{NOMES[i.setor]}</td>
                                    <td>{NOMES[i.tipoIncidente]}</td>
                                    <td>{NOMES[i.classificacao]}</td>
                                    <td>
                                        <Button variant="success" onClick={this._visualizaIncidente.bind(this, i)}><FaEye /></Button>{' '}
                                        {
                                            tipo !== 'concluidos' ?
                                                (
                                                    <>
                                                        <Button variant="primary" as={Link} to={`/dashboard/incidente/${i.id}`}><FaEdit /></Button>{' '}
                                                    </>
                                                ) : (<></>)
                                        }
                                        {
                                            tipo === 'abertos' ?
                                                (
                                                    <>
                                                        <Button variant="info" onClick={this._confirmaMudancaEstado.bind(this, i, 'em_analise')}><FaAngleRight /></Button>{' '}
                                                        <Button variant="warning" onClick={this._confirmaMudancaEstado.bind(this, i, 'concluidos')}><FaAngleDoubleRight /></Button>
                                                    </>
                                                ) : tipo === 'em_analise' ?
                                                    (
                                                        <>
                                                            <Button variant="info" onClick={this._confirmaMudancaEstado.bind(this, i, 'abertos')}><FaAngleLeft /></Button>{' '}
                                                            <Button variant="info" onClick={this._confirmaMudancaEstado.bind(this, i, 'concluidos')}><FaAngleRight /></Button>
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

        const { incidente, estado, visivel } = this.state.modalEstado;
        let novoEstado = null;
        let nomeEstado = '';

        if (estado === 'abertos') {
            novoEstado = 'aberto';
            nomeEstado = 'Aberto';
        } else if (estado === 'em_analise') {
            novoEstado = estado;
            nomeEstado = 'Análise';
        } else if (estado === 'concluidos') {
            novoEstado = 'concluido';
            nomeEstado = 'Concluído';
        }

        return (
            <div>
                {
                    incidente ?
                        (
                            <Modal show={visivel} onHide={this._confirmaMudancaEstado.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Mudar Estado de Incidente?</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p>Deseja mover o Incidente abaixo para o estado de <b>{nomeEstado}</b>?<br /><br />{incidente.titulo} - {NOMES[incidente.tipoIncidente]}</p>{
                                                novoEstado === 'concluido' ?
                                                    (<Alert variant="danger">A conclusão de um Incidente não poderá ser desfeita.</Alert>) : (<></>)
                                            }
                                            </Col>

                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._confirmaMudancaEstado.bind(this, null)}>
                                        Fechar
                                    </Button>
                                    <Button variant="danger" onClick={this._mudarEstadoIncidente.bind(this, incidente, novoEstado, nomeEstado)}>
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
        const { incidente, visivel } = this.state.modalVisualizar;

        return (
            <div>
                {
                    incidente ?
                        (
                            <Modal show={visivel} onHide={this._visualizaIncidente.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>{incidente.titulo}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>

                                        <Row>
                                            <Col md></Col>
                                            <Col md="4">
                                                <p><b>Título: </b><br />{incidente.titulo}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Classif do Impacto: </b><br />{NOMES[incidente.classificacao]}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Tipo de Incidente: </b><br />{NOMES[incidente.tipoIncidente]}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="8">
                                                <p><b>Descrição: </b><br />{incidente.descricao}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Setor: </b><br />{NOMES[incidente.setor]}</p>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="10">
                                                <p><b>Conclusão / Análise do Incidente: </b><br />{incidente.conclusao !== '' ? incidente.conclusao : (<i>Não fornecida.</i>)}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md></Col>
                                            <Col md="10">
                                                <Row>
                                                    <Col style={{ textAlign: 'center' }}>
                                                        <b>Não Conformidade Envolvidas</b>
                                                    </Col>
                                                </Row>
                                                <br />
                                                <Row>
                                                    {incidente.ncEnvolvidas.map(nc => (
                                                        <Col md="4" sm="6">
                                                            <Card>
                                                                <Card.Body>
                                                                    <Card.Title>{nc.titulo} <sup style={{ color: 'red' }}></sup></Card.Title>
                                                                    <Card.Subtitle className="mb-2 text-muted">[{nc.id}] {nc.resumo}</Card.Subtitle>
                                                                </Card.Body>
                                                            </Card>
                                                        </Col>
                                                    ))}
                                                </Row>
                                            </Col>
                                            <Col md></Col>
                                        </Row>

                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._visualizaIncidente.bind(this, null)}>
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

export default ListaIncidente;