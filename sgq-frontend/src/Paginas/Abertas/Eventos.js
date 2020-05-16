import React, { Component } from 'react';
import { Button, Container, Col, Modal, Row, Tabs, Tab, Table } from 'react-bootstrap';

import { Link } from 'react-router-dom';

import { cliente } from '../../Componentes/SGQClient';

import logo from '../../images/logo_sgq_texto.png';
import { parseDate } from '../../Componentes/ParseDate';

class Eventos extends Component {

    state = {
        modal: { visivel: false, incidente: null },
        incidentes: []
    }

    componentDidMount() {
        this._carregarIncidentes();
        this._carregarCampanhas();
    }

    async _carregarIncidentes() {
        const evCli = cliente().eventos;
        const resp = await evCli.consultaUltimosIncidentes();

        if (resp.sucesso) {
            this.setState({ incidentes: resp.retorno });
        }

    }

    async _carregarCampanhas() {

    }

    render() {
        return this.eventos();
    }

    eventos() {

        return (
            <div className="App">
                <Container>
                    <div className="App-header">
                        <Row>
                            <Col md></Col>
                            <Col md>
                                <img src={logo} className="App-logo" alt="logo" />
                            </Col>
                            <Col md></Col>
                        </Row>

                    </div>
                </Container>
                <Container>
                    <Row>
                        <Col md><h1 style={{ textAlign: 'center' }}>Eventos</h1></Col>
                    </Row>
                    <Row>
                        <Col md></Col>
                        <Col md="10">
                            <Tabs defaultActiveKey="incidentes" id="tabEventos">
                                <Tab eventKey="incidentes" title="Últimos Incidentes Ocorridos">
                                    {this._SecaoEventos("INCIDENTES")}
                                </Tab>
                                <Tab eventKey="campanhas" title="Últimas Campanhas de Recall">
                                    {this._SecaoEventos("CAMPANHAS")}
                                </Tab>

                            </Tabs>
                        </Col>
                        <Col md></Col>
                    </Row>
                </Container>
                <Container>
                    <Row>
                        <Col md="5"></Col>
                        <Col md="2">
                            <Button variant="info" as={Link} to="/">Voltar</Button>
                        </Col>
                        <Col md="5"></Col>
                    </Row>
                </Container>
                {this._Modal()}
            </div>
        );
    }

    _SecaoEventos(tipo) {

        let eventos = [];

        if(tipo === 'INCIDENTES'){
            eventos = this.state.incidentes;
        }

        return (
            <Table responsive striped hover>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Setor Afetado</th>
                        <th>Classificação</th>
                        <th>Detalhes</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        eventos.map(i => (
                            <tr key={i.id}>
                                <td>{i.id}</td>
                                <td>{i.titulo}</td>
                                <td>{i.setor}</td>
                                <td>{i.classificacao}</td>
                                <td><Button variant="info" onClick={this._modalIncidente.bind(this, i)}>Detalhes</Button></td>
                            </tr>
                        )
                        )
                    }
                </tbody>
            </Table>
        )
    }

    _modalIncidente(incidente) {
        if (incidente == null) {
            this.setState({ modal: { visivel: false, incidente: null } });
        } else {
            this.setState({ modal: { visivel: true, incidente } });
        }
    }

    _Modal() {

        let incidente = this.state.modal.incidente;

        return (
            <div>
                {
                    incidente ?
                        (
                            <Modal show={this.state.modal.visivel} onHide={this._modalIncidente.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>#{incidente.id} - {incidente.titulo}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p><b>Tipo</b><br />{incidente.tipoIncidente}</p></Col>
                                            <Col md><p><b>Classificação</b><br />{incidente.classificacao}</p></Col>
                                        </Row>
                                        <Row>
                                            <Col md><p><b>Criado em</b><br />{parseDate(incidente.criadoEm)}</p></Col>
                                            <Col md><p><b>Concluído em</b><br />{parseDate(incidente.concluidoEm)}</p></Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            <Col><p><b>Descrição</b><br />{incidente.descricao}</p></Col>
                                        </Row>
                                        <Row>
                                            <Col><p><b>Conclusão</b><br />{incidente.conclusao}</p></Col>
                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._modalIncidente.bind(this, null)}>
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

export default Eventos;