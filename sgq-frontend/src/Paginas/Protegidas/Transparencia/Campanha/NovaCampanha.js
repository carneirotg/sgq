import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';

import React, { Component } from 'react';
import { FaTrash, FaLightbulb, FaPlusCircle } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Card, Modal, Alert, Accordion } from 'react-bootstrap';
import { DateRangePicker, SingleDatePicker } from 'react-dates';

import moment from 'moment';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';
import NOMES from '../../../../Componentes/nomes.json';

class NovaCampanha extends Component {

    state = {
        update: false,
        campanha: {
            id: null,
            titulo: '',
            inicio: null,
            fim: null,
            informativoCampanha: '',
            medidasCorretivas: '',
            incidentesConhecidos: false,
            dataConstatacao: null,
            defeito: '',
            tipoRisco: '',
            ncsEnvolvidas: [],
            estadoCampanha: 'ATIVA'
        },
        modalTipos: false,
        ncs: [],
        modalNCS: {
            visivel: false,
            termo: null
        },
        focused: {
            periodo: null,
            constatado: false
        }
    }

    _timerTermosNC = null;

    async componentDidMount() {
        this._carregaCampanha();
    }

    async _carregaCampanha() {
        if (this.props.match !== undefined && this.props.match.params['id'] !== undefined) {

            const id = parseInt(this.props.match.params['id']);
            const campCli = cliente().campanhas;

            const resp = await campCli.consultaId(id);

            if (resp.sucesso) {
                const campanha = resp.retorno;
                campanha.inicio = moment(campanha.inicio);
                campanha.fim = moment(campanha.fim);
                campanha.dataConstatacao = moment(campanha.dataConstatacao);
                console.log(campanha);
                this.setState({ ...this.state, campanha, update: true });
            } else {

                if (resp.status === 404) {
                    ToastManager.atencao("Campanha não encontrada.");
                    this.props.history.push("/dashboard/campanhas");

                } else {
                    ToastManager.erro("Erro ao carregar campanha. Tente mais tarde.");
                }
            }
        }
    }

    _valoresCampanha(event) {
        let { name, value, checked } = event.target;

        if (name.includes('incidentesConhecidos')) {
            value = checked;
        }

        this.setState({ campanha: { ...this.state.campanha, [name]: value } });

    }

    async _cadastrarCampanha(event) {
        event.preventDefault();

        if (this.state.campanha.ncsEnvolvidas.length === 0) {
            ToastManager.atencao("Ao menos uma não conformidade deve ser informada.");
            return;
        }

        const camCli = cliente().campanhas;
        const resp = await camCli.salvar(this.state.campanha);

        if (resp.sucesso) {
            const estado = this.state.update ? 'atualizada' : 'registrada';
            ToastManager.sucesso(`Campanha ${estado} com sucesso.`)
            this._reset();
        } else {
            ToastManager.erro(`Erro: ${resp.erro}`);
            console.log(resp)
        }

    }

    _toggleModalNCS() {
        this.setState({ ...this.state, ncs: [], modalNCS: { visivel: !this.state.modalNCS.visivel, termo: null } })
    }

    _removeNC(nc) {

        if (this.state.update) {
            ToastManager.atencao("Um campanha só pode ter suas NCS alteradas durante a sua criação.")
            return;
        }

        const { campanha } = this.state;
        let { ncsEnvolvidas } = campanha;
        ncsEnvolvidas = ncsEnvolvidas.filter(n => n.id !== nc.id);

        ToastManager.atencao("NC removida da campanha em edição.");

        this.setState({ campanha: { ...campanha, ncsEnvolvidas } })
    }

    _adicionaNC(nc) {
        let { campanha, ncs } = this.state;
        const { ncsEnvolvidas } = campanha;

        ncsEnvolvidas.push(nc);
        ncs = ncs.filter(n => n.id !== nc.id);

        ToastManager.informativo("NC adicionada à campanha em edição.");

        this.setState({
            ...this.state,
            campanha: { ...campanha, ncsEnvolvidas },
            ncs
        });
    }

    _valorTermosNCS(event) {
        const { name, value } = event.target;
        const modal = this.state.modalNCS;

        this.setState({ modalNCS: { ...modal, [name]: value } });

        clearTimeout(this.timer);
        if (value.length >= 3) {
            this.timer = setTimeout(() => this._carregaNCTermo(value, null), 300);
        } else if (value === '' || value.length === 0) {
            this.timer = setTimeout(() => this.setState({ ncs: [] }));
        }
    }

    async _carregaNCTermo(termo) {
        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.consultaConcluidaTitulo(termo);

        if (resp.sucesso) {
            this.setState({ ncs: resp.retorno });
        } else {
            const status = resp.status;
            if (status !== undefined && status === 404) {
                ToastManager.atencao("Sem resultados para termos utilizados.");
                this.setState({ ncs: [] });
            } else {
                ToastManager.erro(`Erro ao executar consulta de NCS: ${resp.erro}`);
            }
        }
    }

    _reset() {
        if (this.state.update) {
            this.props.history.push('/dashboard/campanhas');
        } else {
            this.setState({
                campanha: {
                    id: null,
                    titulo: '',
                    inicio: null,
                    fim: null,
                    informativoCampanha: '',
                    medidasCorretivas: '',
                    incidentesConhecidos: false,
                    dataConstatacao: null,
                    defeito: '',
                    tipoRisco: '',
                    ncsEnvolvidas: [],
                    estadoCampanha: 'ATIVA'
                }
            })
        }
    }

    render() {

        const tipo = this.state.update ? 'Atualização de' : 'Nova';

        let btAcao = "Cadastrar";
        let btCancela = "Limpar";

        if (this.state.update) {
            btAcao = "Atualizar";
            btCancela = "Retornar";
        }

        const { focused: focusedState } = this.state;
        const { periodo: fPeriodo, constatado: fConstatado } = focusedState;

        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">
                        <Row>
                            <Col md><h1>{tipo} Campanha de Recall</h1></Col>
                        </Row>
                        <Form onSubmit={this._cadastrarCampanha.bind(this)}>
                            <Row>
                                <Col md></Col>
                                <Col md="6">
                                    <Form.Group controlId="gTituloNC">
                                        <Form.Label>Título</Form.Label>
                                        <Form.Control type="text" name="titulo" value={this.state.campanha.titulo} onChange={this._valoresCampanha.bind(this)} minLength="5" required disabled={this.state.update} />
                                    </Form.Group>
                                </Col>
                                <Col md="4">
                                    <Form.Group controlId="gInicio">
                                        <Form.Label>Período da Campanha</Form.Label>
                                        <br />
                                        <DateRangePicker
                                            startDatePlaceholderText="Início"
                                            startDate={this.state.campanha.inicio}
                                            startDateId="inicio"
                                            endDatePlaceholderText="Fim"
                                            endDate={this.state.campanha.fim}
                                            endDateId="fim"
                                            onDatesChange={({ startDate: inicio, endDate: fim }) => this.setState({ campanha: { ...this.state.campanha, inicio, fim } })}
                                            focusedInput={fPeriodo}
                                            onFocusChange={(focusedInput) => this.setState({ focused: { ...focusedState, periodo: focusedInput } })}
                                            disabled={this.state.update}
                                            required
                                        />
                                    </Form.Group>
                                </Col>
                                <Col md></Col>
                            </Row>
                            <Row>
                                <Col md></Col>
                                <Col md="4">
                                    <Form.Group controlId="gDefeito">
                                        <Form.Label>Defeito Apontado</Form.Label>
                                        <Form.Control type="text" name="defeito" value={this.state.campanha.defeito} onChange={this._valoresCampanha.bind(this)} required disabled={this.state.update} />
                                    </Form.Group>
                                </Col>
                                <Col md="2">
                                    <Form.Group controlId="gConstatado">
                                        <Form.Label>Constatado em</Form.Label>
                                        <br />
                                        <SingleDatePicker
                                            id="constatado"
                                            isOutsideRange={() => false}
                                            placeholder="Constatado"
                                            date={this.state.campanha.dataConstatacao}
                                            focused={fConstatado}
                                            onFocusChange={({ focused }) => this.setState({ focused: { ...focusedState, constatado: focused } })}
                                            onDateChange={(dt) => this.setState({ campanha: { ...this.state.campanha, dataConstatacao: dt } })}
                                            disabled={this.state.update}
                                            required
                                        />                                     </Form.Group>
                                </Col>
                                <Col md="4">
                                    <Form.Group controlId="gTipo">
                                        <Form.Label>Tipo <sup> <FaLightbulb style={{ color: '#2d9c21', cursor: 'pointer' }} onClick={() => this.setState({ modalTipos: true })} /></sup>  </Form.Label>
                                        <Form.Control as="select" name="tipoRisco" value={this.state.campanha.tipoRisco} onChange={this._valoresCampanha.bind(this)} required disabled={this.state.update}>
                                            <option placeholder="Selecione"></option>
                                            <option value="FOGO">{NOMES['FOGO'].tipo}</option>
                                            <option value="LESAO">{NOMES['LESAO'].tipo}</option>
                                            <option value="QUEDA">{NOMES['QUEDA'].tipo}</option>
                                            <option value="VARIOS">{NOMES['VARIOS'].tipo}</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md></Col>
                            </Row>
                            <Row>
                                <Col md="1"></Col>
                                <Col md="8">
                                    <Form.Group controlId="gInformativo">
                                        <Form.Label>Texto Informativo</Form.Label>
                                        <Form.Control as="textarea" name="informativoCampanha" value={this.state.campanha.informativoCampanha} onChange={this._valoresCampanha.bind(this)} size="5000" minLength="10" required />
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <br />
                                    <Form.Group controlId="gIncidenteOcorrido">
                                        <Form.Check type="checkbox" label="Incidentes Externos Conhecidos?" name="incidentesConhecidos" value={this.state.campanha.incidentesConhecidos} onChange={this._valoresCampanha.bind(this)} disabled={this.state.update} />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row>
                                <Col md="1"></Col>
                                <Col md="8">
                                    <Form.Group controlId="gMedidas">
                                        <Form.Label>Medidas Corretivas a Serem Adotadas</Form.Label>
                                        <Form.Control as="textarea" name="medidasCorretivas" value={this.state.campanha.medidasCorretivas} onChange={this._valoresCampanha.bind(this)} size="5000" minLength="10" required />
                                    </Form.Group>
                                </Col>
                                <Col md></Col>
                            </Row>
                            <Row>
                                <Col md></Col>
                                <Col md="10">
                                    <Form.Group controlId="gncsEnvolvidas">
                                        <Row>
                                            <Col style={{ textAlign: 'center' }}>
                                                <Button variant="info" onClick={this._toggleModalNCS.bind(this)} disabled={this.state.update}>Adicionar Não Conformidade</Button>
                                            </Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            {this.state.campanha.ncsEnvolvidas.map(nc => (
                                                <Col md="4" sm="6" key={nc.id}>
                                                    <Card >
                                                        <Card.Body onClick={this._removeNC.bind(this, nc)}>
                                                            <Card.Title>{nc.titulo} <sup style={{ color: 'red' }}><FaTrash /></sup></Card.Title>
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
                            <Row className="Btns">
                                <Col md></Col>
                                <Col md><Button className="Btn" type="submit">{btAcao}</Button></Col>
                                <Col md><Button className="Btn" variant="warning" onClick={this._reset.bind(this)}>{btCancela}</Button></Col>
                                <Col md></Col>
                            </Row>
                        </Form>
                    </div>
                </Container>
                <br />
                {this._ModalTipos()}
                {this._ModalNCS()}
            </div>
        );
    }

    _ModalTipos() {

        return (

            <Modal show={this.state.modalTipos} onHide={() => this.setState({ modalTipos: false })} animation={false}>
                <Modal.Header closeButton>
                    <Modal.Title>Tipos de Não Conformidades</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Container>
                        <Row>
                            <Col md>
                                <Accordion>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="0">{NOMES['FOGO'].tipo}</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="0">
                                            <Card.Body>
                                                <p><i>{NOMES['FOGO'].descricao}</i></p>
                                                <p><b>Implicações:</b><br />{NOMES['FOGO'].implicacoes} </p>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="1">{NOMES['LESAO'].tipo}</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="1">
                                            <Card.Body>
                                                <p><i>{NOMES['LESAO'].descricao}</i></p>
                                                <p><b>Implicações:</b><br />{NOMES['LESAO'].implicacoes} </p>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="2">{NOMES['QUEDA'].tipo}</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="2">
                                            <Card.Body>
                                                <p><i>{NOMES['QUEDA'].descricao}</i></p>
                                                <p><b>Implicações:</b><br />{NOMES['QUEDA'].implicacoes} </p>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="3">{NOMES['VARIOS'].tipo}</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="3">
                                            <Card.Body>
                                                <p><i>{NOMES['VARIOS'].descricao}</i></p>
                                                <p><b>Implicações:</b><br />{NOMES['VARIOS'].implicacoes} </p>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                </Accordion>
                            </Col>

                        </Row>
                    </Container>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => this.setState({ modalTipos: false })}>Fechar</Button>
                </Modal.Footer>
            </Modal>
        )
    }

    _ModalNCS() {

        const { visivel } = this.state.modalNCS;

        return (
            (
                <Modal show={visivel} onHide={this._toggleModalNCS.bind(this)} animation={false} size="lg">
                    <Modal.Header closeButton>
                        <Modal.Title>Selecione Não Conformidades</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Container>
                            <Container>
                                <Row>
                                    <Col md></Col>
                                    <Col md="10">
                                        <Form.Control type="text" name="termo" placeholder="Termos refente à uma NC..." onChange={this._valorTermosNCS.bind(this)} />
                                    </Col>
                                    <Col md></Col>
                                </Row>
                                <br />
                                <Row>

                                    {this.state.ncs.map(nc => (
                                        <Col md="4" sm="10">
                                            <Card onClick={this._adicionaNC.bind(this, nc)}>
                                                <Card.Body>
                                                    <Card.Title>{nc.titulo} <sup style={{ color: 'green' }}><FaPlusCircle /></sup></Card.Title>
                                                    <Card.Subtitle className="mb-2 text-muted">[{nc.id}] {nc.resumo}</Card.Subtitle>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    ))}

                                </Row>
                                <Row>
                                    <Col><Alert variant="info" style={{ fontSize: '0.7em' }}>Apenas as dez primeiras ocorrências serão exibidas para cada termo.</Alert></Col>
                                </Row>
                            </Container>
                        </Container>
                    </Modal.Body >
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this._toggleModalNCS.bind(this)}>
                            Fechar
                        </Button>
                    </Modal.Footer>
                </Modal>
            )
        );
    }

}

export default NovaCampanha;