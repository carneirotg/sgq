import React, { Component } from 'react';
import { FaEdit, FaAngleRight, FaAngleLeft, FaAngleDoubleRight, FaEye, FaTasks } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Tabs, Tab, Table, Alert, Modal, Pagination } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';

import NOMES from '../../../../Componentes/nomes.json';
import { Link } from 'react-router-dom';


class ListaNC extends Component {

    PAGINA_INICIAL = {
        habilitada: true,
        pagina: 0,
        paginas: 0,
        total: 0,
        registros: 10
    }

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
        },
        modalVisualizar: {
            nc: null,
            visivel: false
        },
        modalCheckList: {
            nc: null,
            visivel: false,
            novaCheckList: {}
        }
    }

    timer = null;

    async componentDidMount() {
        this._carregaNCEscopo(this.state.tipo);
    }

    _valoresNC(evt) {
        const { name, value } = evt.target;

        this.setState({ ...this.state, [name]: value });

        clearTimeout(this.timer);
        if (value.length >= 3) {
            this.timer = setTimeout(() => this._carregaNCEscopo(this.state.tipo, null), 300);
        } else if (value === '' || value.length === 0) {
            this.timer = setTimeout(() => this._carregaNCEscopo(this.state.tipo, this.PAGINA_INICIAL), 300);
        }
    }

    _valoresCheckList(evt) {
        const { name, checked } = evt.target;
        const { modalCheckList } = this.state;

        this.setState({
            modalCheckList: {
                ...modalCheckList,
                novaCheckList: {
                    ...modalCheckList.novaCheckList,
                    [name]: checked
                }
            }
        });

    }

    async _carregaNCEscopo(tipo, params = null) {
        if (this.state.tipo !== tipo) {
            this.setState({
                ...this.setState,
                tipo,
                pagina: this.PAGINA_INICIAL
            })
        }

        const paramsPaginacao = params != null ? params : this.state.pagina;
        const { buscaNC } = this.state;

        const ncCli = cliente().naoConformidades;
        let resp = null;
        console.log(buscaNC);
        if (buscaNC !== '') {
            resp = await ncCli.consultaEstadoTitulo(tipo, buscaNC, params);
        } else {
            resp = await ncCli.consultaEstado(tipo, params);
        }

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

    _visualizaNC(nc) {

        if (nc !== null) {
            this.setState({
                modalVisualizar: {
                    nc,
                    visivel: true
                }
            });
        } else {
            this.setState({
                modalVisualizar: {
                    nc: null,
                    visivel: false
                }
            });
        }
    }

    _visualizaChecklist(nc) {

        if (nc !== null) {
            this.setState({
                modalCheckList: {
                    nc,
                    visivel: true,
                    novaCheckList: nc.normaNaoConformidade.checkList
                }
            });
        } else {
            this.setState({
                modalCheckList: {
                    nc: null,
                    visivel: false,
                    novaCheckList: {}
                }
            });
        }
    }

    async _atualizarChecklist() {

        const { nc, novaCheckList } = this.state.modalCheckList;

        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.atualizarChecklist(nc.id, novaCheckList);

        if (resp.sucesso) {
            ToastManager.sucesso("Checklist atualizada");
            nc.normaNaoConformidade.checkList = novaCheckList;
            this.setState({
                modalCheckList: {
                    ...this.state.modalCheckList,
                    nc
                }
            })
        } else {
            ToastManager.erro(resp.erro);
            console.log(resp);
        }

        this._visualizaChecklist(null);
    }

    async _mudarEstadoNC(nc, estado, nomeEstado) {
        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.mudarEstado(nc.id, estado);

        if (resp.sucesso) {
            ToastManager.informativo(`Não conformidade movida para fase de ${nomeEstado}`);
            this.setState({ ...this.state, ncs: this.state.ncs.filter(n => n.id !== nc.id) });
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
                        <Col md="2">
                            <Form.Group controlId="buscaNC">
                                <Form.Control placeholder="Não conformidade x" type="text" name="buscaNC" value={this.state.buscaNC} onChange={this._valoresNC.bind(this)} />
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
                {this._ModalVisualizar()}
                {this._ModalChecklist()}
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
                                        <Button variant="success" onClick={this._visualizaNC.bind(this, n)}><FaEye /></Button>{' '}
                                        {
                                            tipo !== 'concluidas' ?
                                                (
                                                    <>
                                                        <Button variant="primary" as={Link} to={`/dashboard/nc/${n.id}`}><FaEdit /></Button>{' '}
                                                    </>
                                                ) : (<></>)
                                        }
                                        {
                                            n.normaNaoConformidade.normaId !== null && tipo === 'em_analise' ? (
                                                <>
                                                    <Button variant="primary" onClick={this._visualizaChecklist.bind(this, n)}><FaTasks /></Button>{' '}
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

    _ModalVisualizar() {
        const { nc, visivel } = this.state.modalVisualizar;

        return (
            <div>
                {
                    nc ?
                        (
                            <Modal show={visivel} onHide={this._visualizaNC.bind(this, null)} animation={false} size="lg" >
                                <Modal.Header closeButton>
                                    <Modal.Title>{nc.titulo}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>

                                        <Row>
                                            <Col md></Col>
                                            <Col md="4">
                                                <p><b>Título da NC</b><br />{nc.titulo}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Tipo da Não Conformidade</b><br />{NOMES[nc.tipoNaoConformidade]}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Setor</b><br />{NOMES[nc.setor]}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="8">
                                                <p><b>Resumo</b><br />{nc.resumo}</p>
                                            </Col>
                                            <Col md="3">
                                                <Row>
                                                    <Col md>&nbsp;</Col>
                                                </Row>
                                                <Row>
                                                    <Col>
                                                        <p><b>Prejuízo Apurado: </b><br />{nc.prejuizoApurado ? 'Sim' : 'Não'}</p>
                                                    </Col>
                                                </Row>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="7">
                                                <p><b>Detalhamento da NC: </b><br />{nc.detalhamentoNaoConformidade}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Norma Associada: </b><br />{nc.normaNaoConformidade.norma}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="4">
                                                <p><b>Artefato Envolvido da NC: </b><br />{nc.artefato.id + ' - ' + nc.artefato.nome}</p>
                                            </Col>
                                            <Col md="6">
                                                <p><b>Detalhamento do Artefato: </b><br />{nc.detalhamentoArtefato}</p>
                                            </Col>
                                        </Row>
                                        {
                                            nc.normaNaoConformidade.normaId !== null ? (
                                                <>
                                                    <Row>
                                                        <Col md="1"></Col>
                                                        <Col md><div style={{ textAlign: 'center' }}><b>Checklist {nc.normaNaoConformidade.norma}</b></div></Col>
                                                        <Col md="1"></Col>
                                                    </Row>
                                                    <Row>
                                                        <Col md="1"></Col>
                                                        <Col md>
                                                            <ul>
                                                                {
                                                                    Object.entries(nc.normaNaoConformidade.checkList).map(e =>
                                                                    (<><li>{e[0]} - {e[1] ? 'Verificado' : 'Não Verificado'}</li></>))
                                                                }
                                                            </ul>
                                                        </Col>
                                                        <Col md="1"></Col>
                                                    </Row>
                                                </>
                                            ) : (<></>)
                                        }
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._visualizaNC.bind(this, null)}>
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

    _ModalChecklist() {
        const { nc, visivel, novaCheckList } = this.state.modalCheckList;

        let checkListEntries = null;

        if (nc !== null) {
            checkListEntries = Object.entries(novaCheckList);
        }

        return (
            <div>
                {
                    nc ?
                        (
                            <Modal show={visivel} onHide={this._visualizaChecklist.bind(this, null)} animation={false} size="lg" >
                                <Modal.Header closeButton>
                                    <Modal.Title>{nc.titulo}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>

                                        <Row>
                                            <Col md></Col>
                                            <Col md="4">
                                                <p><b>Título da NC</b><br />{nc.titulo}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Tipo da Não Conformidade</b><br />{NOMES[nc.tipoNaoConformidade]}</p>
                                            </Col>
                                            <Col md="3">
                                                <p><b>Setor</b><br />{NOMES[nc.setor]}</p>
                                            </Col>
                                            <Col md></Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md="8">
                                                <p><b>Resumo</b><br />{nc.resumo}</p>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md><div style={{ textAlign: 'center' }}><b>Checklist {nc.normaNaoConformidade.norma}</b></div></Col>
                                            <Col md="1"></Col>
                                        </Row>
                                        <Row>
                                            <Col md="1"></Col>
                                            <Col md>
                                                <ul>
                                                    {
                                                        checkListEntries.map(e =>
                                                            (<><li>{e[0]} - <input type="checkbox" key={e[0]} name={e[0]} checked={e[1]} onChange={this._valoresCheckList.bind(this)} /></li></>))
                                                    }
                                                </ul>
                                            </Col>
                                            <Col md="1"></Col>
                                        </Row>

                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="primary" onClick={this._atualizarChecklist.bind(this, null)}>
                                        Atualizar Checklist
                                    </Button>
                                    <Button variant="secondary" onClick={this._visualizaChecklist.bind(this, null)}>
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

export default ListaNC;