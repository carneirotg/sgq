import React, { Component } from 'react';
import { Button, Container, Form, Row, Col, Jumbotron } from 'react-bootstrap';
import './Artefato.css';

import { cliente } from '../../../Componentes/SGQClient';
import ToastManager from '../../../Componentes/ToastManager';

class ArtefatoNovo extends Component {

    state = {
        artefato: {
            nome: '',
            descricao: ''
        }
    }

    _valores(event) {
        const name = event.target.name;
        const value = event.target.value;
        this.setState({ artefato: { ...this.state.artefato, [name]: value } });
    }

    _reset() {
        this.setState({ artefato: { nome: '', descricao: '' } });
    }

    async _cadastrarArtefato(e) {
        e.preventDefault();

        let resp = await cliente().artefatos.salvar(this.state.artefato);

        if (resp.sucesso) {
            ToastManager.sucesso("Artefato cadastrado");
            this._reset();
        } else {
            ToastManager.erro("Erro ao cadastrar. Tente mais tarde.")
        }
    }

    render() {
        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">
                        <Jumbotron>
                            <Row>
                                <Col md><h1>Novo Artefato</h1></Col>
                            </Row>
                            <Form validated onSubmit={this._cadastrarArtefato.bind(this)}>
                                <Row>
                                    <Col md></Col>
                                    <Col md="4">
                                        <Form.Group controlId="gNomeArtefato">
                                            <Form.Label>Nome</Form.Label>
                                            <Form.Control type="text" name="nome" value={this.state.artefato.nome} onChange={this._valores.bind(this)} minLength="5" required />
                                            <Form.Control.Feedback type="invalid">
                                                Forneça um nome
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md></Col>
                                </Row>
                                <Row>
                                    <Col md></Col>
                                    <Col md="4">
                                        <Form.Group controlId="gDescrArtefato">
                                            <Form.Label>Descrição</Form.Label>
                                            <Form.Control as="textarea" name="descricao" value={this.state.artefato.descricao} onChange={this._valores.bind(this)} size="500" minLength="5" required />
                                            <Form.Control.Feedback type="invalid">
                                                Forneça a descrição
                                            </Form.Control.Feedback>
                                        </Form.Group>
                                    </Col>
                                    <Col md></Col>
                                </Row>
                                <Row className="Btns">
                                    <Col md></Col>
                                    <Col md><Button className="Btn" type="submit">Cadastrar</Button></Col>
                                    <Col md><Button className="Btn" variant="warning" onClick={this._reset.bind(this)}>Limpar</Button></Col>
                                    <Col md></Col>
                                </Row>
                            </Form>
                        </Jumbotron>
                    </div>
                </Container>
            </div>
        );
    }

}

export default ArtefatoNovo;