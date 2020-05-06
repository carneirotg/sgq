import React, { Component } from 'react';
import { Button, Container, Col, Modal, Row, Tabs, Tab, Table } from 'react-bootstrap';

import { Link } from 'react-router-dom';

import logo from '../../images/logo_sgq_texto.png';

class Eventos extends Component {

    state = {
        modal: { visivel: false, incidente: null },
        incidentes: [
            {
                id: 34516,
                titulo: "Braço mecânico emperrado",
                descricao: "Braço mecânico da linha de montagem apresentou problemas no funcionamento, tendo emperrado durante as horas inicias do dia do evento. Foi necessário a lubrificação do mesmo, o suporte atuou de pronto.",
                conclusao: "Desgaste apresentado que causou o problema foi devido ao peso excessivo, causando maior atrito que o suportado pela especificação do componente.",
                classificacao: "Baixa",
                tipoIncidente: "SEM_DANO",
                criadoEm: "2020-01-09 10:45:00",
                concluidoEm: "2020-01-09 13:30:00",
                setor: "LINHA_MONTAGEM",
            },
            {
                id: 12312,
                titulo: "Braço mecânico emperrado",
                descricao: "Braço mecânico da linha de montagem apresentou problemas no funcionamento, tendo emperrado durante as horas inicias do dia do evento. Foi necessário a lubrificação do mesmo, o suporte atuou de pronto.",
                conclusao: "Desgaste apresentado que causou o problema foi devido ao peso excessivo, causando maior atrito que o suportado pela especificação do componente.",
                classificacao: "Baixa",
                tipoIncidente: "PARADA",
                criadoEm: "2020-01-09 10:45:00",
                concluidoEm: "2020-01-09 13:30:00",
                setor: "LINHA_MONTAGEM",
            }, {
                id: 2345,
                titulo: "Braço mecânico emperrado",
                descricao: "Braço mecânico da linha de montagem apresentou problemas no funcionamento, tendo emperrado durante as horas inicias do dia do evento. Foi necessário a lubrificação do mesmo, o suporte atuou de pronto.",
                conclusao: "Desgaste apresentado que causou o problema foi devido ao peso excessivo, causando maior atrito que o suportado pela especificação do componente.",
                classificacao: "Baixa",
                tipoIncidente: "PARADA",
                criadoEm: "2020-01-09 10:45:00",
                concluidoEm: "2020-01-09 13:30:00",
                setor: "LINHA_MONTAGEM",
            }, {
                id: 33156,
                titulo: "Braço mecânico emperrado",
                descricao: "Braço mecânico da linha de montagem apresentou problemas no funcionamento, tendo emperrado durante as horas inicias do dia do evento. Foi necessário a lubrificação do mesmo, o suporte atuou de pronto.",
                conclusao: "Desgaste apresentado que causou o problema foi devido ao peso excessivo, causando maior atrito que o suportado pela especificação do componente.",
                classificacao: "Baixa",
                tipoIncidente: "DANO_FINANCEIRO",
                criadoEm: "2020-01-09 10:45:00",
                concluidoEm: "2020-01-09 13:30:00",
                setor: "LINHA_MONTAGEM",
            }, {
                id: 9687,
                titulo: "Braço mecânico emperrado",
                descricao: "Braço mecânico da linha de montagem apresentou problemas no funcionamento, tendo emperrado durante as horas inicias do dia do evento. Foi necessário a lubrificação do mesmo, o suporte atuou de pronto.",
                conclusao: "Desgaste apresentado que causou o problema foi devido ao peso excessivo, causando maior atrito que o suportado pela especificação do componente.",
                classificacao: "Baixa",
                tipoIncidente: "SEM_DANO",
                criadoEm: "2020-01-09 10:45:00",
                concluidoEm: "2020-01-09 13:30:00",
                setor: "LINHA_MONTAGEM",
            }
        ]
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
                            <Tabs defaultActiveKey="sem_dano" id="tabIncidentes">
                                <Tab eventKey="sem_dano" title="Sem Danos Significativos">
                                    {this._SecaoEventos("SEM_DANO")}
                                </Tab>
                                <Tab eventKey="dano_pessoas" title="Danos a Pessoas">
                                    {this._SecaoEventos("DANO_PESSOA")}
                                </Tab>
                                <Tab eventKey="dano_financeiro" title="Danos Financeiros">
                                    {this._SecaoEventos("DANO_FINANCEIRO")}
                                </Tab>
                                <Tab eventKey="parada" title="Parada de Produção">
                                    {this._SecaoEventos("PARADA")}
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
                        this.state
                            .incidentes
                            .filter(i => i.tipoIncidente === tipo)
                            .map(i => (
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
        console.log(`Modal acionado com ${incidente}`);
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
                                            <Col md><p><b>Criado em</b><br />{incidente.criadoEm}</p></Col>
                                            <Col md><p><b>Concluído em</b><br />{incidente.concluidoEm}</p></Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            <Col><p><b>Descrição</b><br />{incidente.descricao}</p></Col>
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