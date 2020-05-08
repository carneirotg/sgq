import React, { Component } from 'react';
import { Button, Container, Form, Pagination, Table, Row, Col, Modal } from 'react-bootstrap';
import './ConsultaNormas.css';

import { FaTrashAlt, FaArrowAltCircleDown, FaTasks } from 'react-icons/fa';

import { cliente } from '../../../Componentes/SGQClient';
import ToastManager from '../../../Componentes/ToastManager';

class ConsultaNorma extends Component {

    state = {
        buscaNorma: '',
        normas: [],
        modal: {
            visivel: false,
            norma: null
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

        const resp = await cliente().normas.listar();

        if (resp.sucesso) {
            const normas = resp.retorno;

            this.setState({ normas });
        } else {
            ToastManager.erro("Erro ao consultar normas. Tente mais tarde.");
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
        const paginas = Math.ceil(this.state.normas.length / registros)
        const total = this.state.normas.length;

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

        if (this.state.buscaNorma.length > 3) {
            clearTimeout(this.timer);
            this.timer = setTimeout(() => this._consultar(), 300)
        } else if (this.state.buscaNorma.length == 0) {
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

    _modalNorma(norma) {
        if (norma == null) {
            this.setState({ modal: { visivel: false, norma: null } });
        } else {
            this.setState({ modal: { visivel: true, norma } });
        }
    }

    _avisoDownload(norma) {
        ToastManager.informativo(`Download de '${norma.norma}' simulado.`)
    }

    render() {

        const { pagina, registros } = this.state.pagina;

        const inicio = (pagina - 1) * registros;
        const fim = (pagina * registros) - 1;
        const { normas } = this.state;
        const { buscaNorma } = this.state;

        let normasListadas = [];

        if (buscaNorma !== '') {
            normasListadas = normas.filter(n => n.norma.toLowerCase().concat(n.resumo.toLowerCase()).includes(buscaNorma))
        } else {
            normasListadas = normas.slice(inicio, fim + 1);
        }

        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">

                        <Row>
                            <Col md><h1>Consulta Normas</h1></Col>
                        </Row>
                        <Row>
                            <Col md="4"></Col>
                            <Col md="6">
                                <Form.Group controlId="paginador">
                                    <Col>{this._paginador()}</Col>
                                </Form.Group>
                            </Col>
                            <Col md="2">
                                <Form.Group controlId="buscaNorma">
                                    <Form.Control placeholder="ISO 9000" type="text" name="buscaNorma" value={this.state.buscaNorma} onChange={this._valores.bind(this)} />
                                </Form.Group>
                            </Col>

                        </Row>
                        <Row>
                            <Col md>
                                <Table striped bordered hover>
                                    <thead>
                                        <tr>
                                            <th style={{ width: '10%' }}>#</th>
                                            <th style={{ width: '15%' }}>Norma</th>
                                            <th style={{ width: '10%', wordWrap: 'break-word' }}>Área</th>
                                            <th style={{ width: '30%' }} className="d-none d-lg-table-cell">Resumo</th>
                                            <th style={{ textAlign: 'center', width: '20%' }}>Ação</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            normasListadas.map(n => (
                                                <tr key={n.normaId}>
                                                    <td>{n.normaId}</td>
                                                    <td>{n.norma}</td>
                                                    <td style={{ wordWrap: 'break-word' }}>{n.area}</td>
                                                    <td className="d-none d-lg-table-cell">{n.resumo}</td>
                                                    <td style={{ textAlign: 'center' }}>
                                                        <Button variant="success" onClick={this._avisoDownload.bind(this, n)}><FaArrowAltCircleDown /> </Button> {' '}
                                                        <Button variant="info" onClick={this._modalNorma.bind(this, n)}><FaTasks /> </Button>
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
                {this._Modal()}
            </div>
        );
    }

    _Modal() {

        let norma = this.state.modal.norma;

        return (
            <div>
                {
                    norma ?
                        (
                            <Modal show={this.state.modal.visivel} onHide={this._modalNorma.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>{norma.nome}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p><b>Norma</b><br />{norma.area}</p></Col>
                                            <Col md><p><b>Área Industrial</b><br />{norma.area}</p></Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            <Col md><p><b>Resumo</b><br />{norma.resumo}</p></Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            <Col md><p><b>Checklist Sugerido</b><br /></p>
                                                <ul>
                                                    {Object.keys(norma.checkList).map(c => (<li key={c.length}>{c}</li>))}
                                                </ul>
                                            </Col>
                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._modalNorma.bind(this, null)}>
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

export default ConsultaNorma;