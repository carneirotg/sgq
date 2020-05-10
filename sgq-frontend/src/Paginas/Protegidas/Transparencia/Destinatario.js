import React, { Component } from 'react';
import { Button, Container, Form, Pagination, Table, Row, Col, Modal } from 'react-bootstrap';
import './Destinatario.css';

import { FaUserEdit, FaTrashAlt, FaUserPlus } from 'react-icons/fa';

import { cliente } from '../../../Componentes/SGQClient';
import ToastManager from '../../../Componentes/ToastManager';

class Destinatario extends Component {

    state = {
        buscaDestinatario: '',
        destinatarios: [],
        modal: {
            visivel: false,
            destinatario: null
        },
        modalRemove: {
            visivel: false,
            destinatario: null
        },
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
        await this._consultar();
        this._configuraPaginacao();
    }

    async _consultar(params = null) {

        const resp = await cliente().destinatarios.listar();

        if (resp.sucesso) {
            const destinatarios = resp.retorno;

            this.setState({ destinatarios });
        } else {
            ToastManager.erro("Erro ao consultar destinatarios. Tente mais tarde.");
        }
    }

    async _paginar(pag) {
        if (parseInt(this.state.pagina.pagina) !== pag) {
            this.setState({
                pagina: {
                    ...this.state.pagina, pagina: pag
                }
            })
        }
    }

    _configuraPaginacao() {
        const { registros } = this.state.pagina;
        const paginas = Math.ceil(this.state.destinatarios.length / registros)
        const total = this.state.destinatarios.length;

        this.setState({
            pagina: {
                pagina: 1,
                paginas,
                registros,
                total
            }
        })
    }

    _valores(event) {
        const name = event.target.name;
        const value = event.target.value;

        this.setState({ ...this.state, [name]: value });

        if (this.state.buscaDestinatario.length > 3) {
            clearTimeout(this.timer);
            this.timer = setTimeout(() => this._consultar(), 300)
        } else if (this.state.buscaDestinatario.length === 0) {
            this._consultar();
        }
    }

    _valoresDestinatario(event) {
        const name = event.target.name;
        let value = event.target.value;

        if (name.includes("assinante")) {
            value = event.target.checked;
        }

        const { modal } = this.state;
        const { destinatario } = modal;

        this.setState(
            {
                modal:
                {
                    ...modal,
                    destinatario:
                        { ...destinatario, [name]: value }
                }
            });

        if (this.state.buscaDestinatario.length > 3) {
            clearTimeout(this.timer);
            this.timer = setTimeout(() => this._consultar(), 300)
        } else if (this.state.buscaDestinatario.length === 0) {
            this._consultar();
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

    _modalDestinatario(destinatario, novo = false) {
        console.log(`_modalDestinatario(${destinatario}, ${novo} = false)`)
        if (destinatario == null && !novo) {
            this.setState({ modal: { visivel: false, destinatario: null } });

        } else if (destinatario == null && novo) {
            let vazio = {
                id: null,
                descricao: '',
                tipoDestinatario: 'WEBSERVICE',
                endpoint: '',
                assinanteRecall: false,
                assinanteEventos: false
            };

            this.setState({ modal: { visivel: true, destinatario: vazio } });

        } else {
            this.setState({ modal: { visivel: true, destinatario } });
        }

    }

    async _removeDestinatario(destinatario) {
        this._modalRemoveDestinatario(null);
        const resp = await cliente().destinatarios.remover(destinatario.id);

        if (resp.sucesso) {
            ToastManager.sucesso("Destinatário removido com sucesso")
            this.setState({ destinatarios: this.state.destinatarios.filter(d => d.id !== destinatario.id) });
        } else {
            ToastManager.erro("Erro ao remover destinatário. Tente mais tarde.");
        }
    }

    async _cadastrarDestinatario(evt) {
        evt.preventDefault();

        const { destinatario } = this.state.modal;
        const acao = destinatario.id != null ? 'atualizado' : 'cadastrado';

        const resp = await cliente().destinatarios.salvar(destinatario);

        if (resp.sucesso) {
            ToastManager.sucesso(`Destinatário ${acao} com sucesso.`);

            if (acao === 'cadastrado') {
                const { destinatarios } = this.state;
                destinatario.id = resp.resourceId;
                
                destinatarios.push(destinatario);

                this.setState({ destinatarios })
            } else {
                this.setState(
                    {
                        destinatarios: [
                            ...this.state.destinatarios.map(d => {
                                if (d.id !== destinatario.id) {
                                    return d;
                                } else {
                                    return destinatario
                                }
                            })
                        ]
                    });
            }

            this._modalDestinatario(null, false);
        } else {
            ToastManager.erro(`Destinatario não foi ${acao}. Tente mais tarde.`);
        }
    }

    _modalRemoveDestinatario(destinatario) {
        if (destinatario == null) {
            this.setState({ modalRemove: { visivel: false, destinatario: null } });
        } else {
            this.setState({ modalRemove: { visivel: true, destinatario } });
        }
    }

    _simNao(valor) {
        return valor ? 'Sim' : 'Não';
    }

    render() {

        const { pagina, registros } = this.state.pagina;

        const inicio = (pagina - 1) * registros;
        const fim = (pagina * registros) - 1;
        const { destinatarios } = this.state;
        const { buscaDestinatario } = this.state;

        let destinatariosListads = [];

        if (buscaDestinatario !== '') {
            destinatariosListads = destinatarios.filter(d => d.descricao.toLowerCase().includes(buscaDestinatario))
        } else {
            destinatariosListads = destinatarios.slice(inicio, fim + 1);
        }

        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">

                        <Row>
                            <Col md><h1>Consulta Destinatários</h1></Col>
                        </Row>
                        <Row>
                            <Col md="3"></Col>
                            <Col md="6">
                                <Form.Group controlId="paginador">
                                    <Col>{this._paginador()}</Col>
                                </Form.Group>
                            </Col>
                            <Col md>
                                <Button variant="primary" onClick={this._modalDestinatario.bind(this, null, true)}><FaUserPlus /></Button>
                            </Col>
                            <Col md="2">
                                <Form.Group controlId="buscaDestinatario">
                                    <Form.Control placeholder="DETRAN" type="text" name="buscaDestinatario" value={this.state.buscaDestinatario} onChange={this._valores.bind(this)} />
                                </Form.Group>
                            </Col>

                        </Row>
                        <Row>
                            <Col md>
                                <Table striped bordered hover>
                                    <thead>
                                        <tr>
                                            <th style={{ width: '10%' }}>#</th>
                                            <th style={{ width: '15%' }}>Descrição</th>
                                            <th style={{ width: '10%', wordWrap: 'break-word' }}>Tipo Endpoint</th>
                                            <th style={{ width: '30%' }} className="d-none d-lg-table-cell">Endpoint</th>
                                            <th style={{ textAlign: 'center', width: '10%' }}>Recall</th>
                                            <th style={{ textAlign: 'center', width: '10%' }}>Incidentes</th>
                                            <th style={{ textAlign: 'center', width: '15%' }}>Ações</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            destinatariosListads.map(d => (
                                                <tr key={d.id}>
                                                    <td>{d.id}</td>
                                                    <td>{d.descricao}</td>
                                                    <td style={{ wordWrap: 'break-word' }}>{d.tipoDestinatario}</td>
                                                    <td className="d-none d-lg-table-cell">{d.endpoint}</td>
                                                    <td style={{ textAlign: 'center' }}>{this._simNao(d.assinanteRecall)}</td>
                                                    <td style={{ textAlign: 'center' }}>{this._simNao(d.assinanteEventos)}</td>
                                                    <td style={{ textAlign: 'center' }}>
                                                        <Button variant="info" onClick={this._modalDestinatario.bind(this, d, false)}><FaUserEdit /> </Button>{' '}
                                                        <Button variant="danger" onClick={this._modalRemoveDestinatario.bind(this, d)}><FaTrashAlt /> </Button>
                                                    </td>
                                                </tr>
                                            ))
                                        }
                                    </tbody>
                                </Table>
                            </Col>
                        </Row>

                    </div>
                </Container>
                {this._ModalEdicao()}
                {this._ModalRemove()}
            </div>
        );
    }

    _ModalRemove() {

        let destinatario = this.state.modalRemove.destinatario;

        return (
            <div>
                {
                    destinatario ?
                        (
                            <Modal show={this.state.modalRemove.visivel} onHide={this._modalRemoveDestinatario.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Remover Destinatário?</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p><b>Deseja remover o destinário abaixo?</b><br />{destinatario.descricao}</p></Col>
                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._modalRemoveDestinatario.bind(this, null)}>
                                        Fechar
                                    </Button>
                                    <Button variant="danger" onClick={this._removeDestinatario.bind(this, destinatario)}>
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

    _ModalEdicao() {

        if (!this.state.modal.visivel) {
            return;
        }

        let destinatario = this.state.modal.destinatario;
        let tituloTipo = 'Novo';

        if (destinatario != null) {
            tituloTipo = 'Atualização de'
        } else {

        }

        return (

            <Modal show={this.state.modal.visivel} onHide={this._modalDestinatario.bind(this, null, false)} animation={false}>
                <Modal.Header closeButton>
                    <Modal.Title>{tituloTipo} Destinatário</Modal.Title>
                </Modal.Header>
                <Form onSubmit={this._cadastrarDestinatario.bind(this)}>
                    <Modal.Body>
                        <Container>
                            <Row>
                                <Col md>
                                    <p><b>Descrição</b><br /></p>
                                    <Form.Group controlId="descricaoGroup">
                                        <Form.Control type="text" name="descricao" value={destinatario.descricao} onChange={this._valoresDestinatario.bind(this)} minLength="5" required />
                                        <Form.Control.Feedback type="invalid">
                                            Forneça um nome / descrição
                                            </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                                <Col md>
                                    <p><b>Tipo Endpoint</b><br /></p>
                                    <Form.Group controlId="tipoEndGroup">
                                        <Form.Control as="select" name="tipoDestinatario" value={destinatario.tipoDestinatario} onChange={this._valoresDestinatario.bind(this)}>
                                            <option value="WEBSERVICE">Webservices / API</option>
                                            <option value="EMAIL">E-mail</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                            </Row>
                            <br />
                            <Row>
                                <Col md>
                                    <p><b>Endpoint</b><br /></p>
                                    <Form.Group controlId="endpointGroup">
                                        <Form.Control type="text" name="endpoint" value={destinatario.endpoint} onChange={this._valoresDestinatario.bind(this)} minLength="7" required />
                                        <Form.Control.Feedback type="invalid">
                                            Forneça um endpoint
                                            </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                            </Row>
                            <br />
                            <Row>
                                <Col md>
                                    <p><b>Notificar Campanhas?</b><br /></p>
                                    <Form.Check type="checkbox" id="assinanteRecall" name="assinanteRecall" label="Assinado" checked={destinatario.assinanteRecall} onChange={this._valoresDestinatario.bind(this)} />
                                </Col>
                                <Col md>
                                    <p><b>Notificar Incidentes?</b><br /></p>
                                    <Form.Check type="checkbox" id="assinanteEventos" name="assinanteEventos" label="Assinado" checked={destinatario.assinanteEventos} onChange={this._valoresDestinatario.bind(this)} />
                                </Col>
                            </Row>
                        </Container>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="info" type="submit">
                            Salvar
                            </Button>
                        <Button variant="secondary" onClick={this._modalDestinatario.bind(this, null, false)}>
                            Fechar
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        )
    }

}

export default Destinatario;