import React, { Component } from 'react';
import { Button, Card, Container, Col, Modal, Row, Tabs, Tab, Table } from 'react-bootstrap';

import { Link } from 'react-router-dom';

import { cliente } from '../../Componentes/SGQClient';
import ToastManager from '../../Componentes/ToastManager';
import { parseDateOnly } from '../../Componentes/ParseDate';
import { parseDate } from '../../Componentes/ParseDate';
import NOMES from '../../Componentes/nomes.json';

import logo from '../../images/logo_sgq_texto.png';

class Eventos extends Component {

    state = {
        modalIncidente: { visivel: false, incidente: null },
        modalCampanha: { visivel: false, campanha: null },
        incidentes: [],
        campanhas: []
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
        } else {
            if (resp.status !== 404) {
                ToastManager.atencao("Erro ao carregar últimos incidentes. Tente mais tarde.")
            }
        }

    }

    async _carregarCampanhas() {
        const evCli = cliente().eventos;
        const resp = await evCli.consultaUltimasCampanhas();

        if (resp.sucesso) {
            this.setState({ campanhas: resp.retorno });
        } else {
            if (resp.status !== 404) {
                ToastManager.atencao("Erro ao carregar últimos campanhas. Tente mais tarde.");
            }
        }
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
                {this._ModalIncidentes()}
                {this._ModalCampanhas()}
            </div>
        );
    }

    _SecaoEventos(tipo) {

        let eventos = [];
        let tipoDescricao = '';

        if (tipo === 'INCIDENTES') {
            eventos = this.state.incidentes;
            tipoDescricao = 'Incidentes';

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
                            eventos.length === 0 ? (
                                <tr>
                                    <th colSpan="5" style={{ textAlign: 'center' }}>{tipoDescricao} não disponíveis.</th>
                                </tr>
                            )
                                :
                                eventos.map(i => (
                                    <tr key={i.id}>
                                        <td>{i.id}</td>
                                        <td>{i.titulo}</td>
                                        <td>{NOMES[i.setor]}</td>
                                        <td>{NOMES[i.classificacao]}</td>
                                        <td><Button variant="info" onClick={this._modalIncidente.bind(this, i)}>Detalhes</Button></td>
                                    </tr>

                                ))
                        }
                    </tbody>
                </Table>
            );

        } else if (tipo === 'CAMPANHAS') {
            eventos = this.state.campanhas;
            tipoDescricao = 'Campanhas de Recall';

            return (
                <Table responsive striped hover>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Título</th>
                            <th>Início Recall</th>
                            <th>Fim Recall</th>
                            <th>Detalhes</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            eventos.length === 0 ? (
                                <tr>
                                    <th colSpan="5" style={{ textAlign: 'center' }}>{tipoDescricao} não disponíveis.</th>
                                </tr>
                            )
                                :
                                eventos.map(c => (
                                    <tr key={c.id}>
                                        <td>{c.id}</td>
                                        <td>{c.titulo}</td>
                                        <td>{parseDateOnly(c.inicio)}</td>
                                        <td>{parseDateOnly(c.fim)}</td>
                                        <td><Button variant="info" onClick={this._modalCampanha.bind(this, c)}>Detalhes</Button></td>
                                    </tr>

                                ))
                        }
                    </tbody>
                </Table>
            )
        }


    }


    _modalIncidente(incidente) {
        if (incidente == null) {
            this.setState({ modalIncidente: { visivel: false, incidente: null } });
        } else {
            this.setState({ modalIncidente: { visivel: true, incidente } });
        }
    }

    _modalCampanha(campanha) {
        if (campanha == null) {
            this.setState({ modalCampanha: { visivel: false, campanha: null } });
        } else {
            this.setState({ modalCampanha: { visivel: true, campanha } });
        }
    }

    _ModalIncidentes() {

        let { incidente, visivel } = this.state.modalIncidente;

        return (
            <div>
                {
                    incidente ?
                        (
                            <Modal show={visivel} onHide={this._modalIncidente.bind(this, null)} animation={false}>
                                <Modal.Header closeButton>
                                    <Modal.Title>#{incidente.id} - {incidente.titulo}</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container>
                                        <Row>
                                            <Col md><p><b>Tipo</b><br />{NOMES[incidente.tipoIncidente]}</p></Col>
                                            <Col md><p><b>Classificação</b><br />{NOMES[incidente.classificacao]}</p></Col>
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

    _ModalCampanhas() {

        const { campanha, visivel } = this.state.modalCampanha;

        return (
            <div>
                {
                    campanha ?
                        (
                            <Modal show={visivel} onHide={this._modalCampanha.bind(this, null)} animation={false} size="lg">
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
                                                <p><b>Medidas Corretivas: </b><br />{campanha.medidasCorretivas}</p>
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
                                            </Col>
                                            <Col md></Col>
                                        </Row>

                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={this._modalCampanha.bind(this, null)}>
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