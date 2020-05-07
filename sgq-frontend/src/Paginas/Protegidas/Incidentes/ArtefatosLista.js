import React, { Component } from 'react';
import { Button, Container, Form, Pagination, Table, Row, Col, Jumbotron, Toast } from 'react-bootstrap';
import './Artefato.css';

import { cliente } from '../../../Componentes/SGQClient';
import ToastManager from '../../../Componentes/ToastManager';

class ArtefatosLista extends Component {

    state = {
        buscaArtefato: '',
        artefatos: [],
        pagina: {
            habilitada: true,
            pagina: 1,
            paginas: 0,
            total: 0,
            registros: 3
        }
    }

    async componentDidMount() {
        const resp = await cliente().artefatos.listar(this.state.pagina);

        if (resp.sucesso) {
            const artefatos = resp.retorno;
            
            this.setState(
                {
                    artefatos: artefatos,
                    pagina: {
                        ...this.state.pagina,
                        pagina: resp.headers['x-sgq-pagina'],
                        paginas: resp.headers['x-sgq-paginas'],
                        total: resp.headers['x-sgq-total'],
                    }
                });
        }
    }

    _valores(event) {
        const name = event.target.name;
        const value = event.target.value;
        this.setState({ ...this.state, [name]: value });
    }

    _simNao(val) {
        return val ? "Sim" : "Não";
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

    render() {

        let arts = this.state.artefatos;
        const busca = this.state.buscaArtefato;

        if (this.state.buscaArtefato != '') {
            arts = this.state.artefatos.filter(a => a.nome.toLowerCase().includes(busca.toLowerCase()));
        }

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
                                                <tr>
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

                    </div>
                </Container>
            </div>
        );
    }

}

export default ArtefatosLista;