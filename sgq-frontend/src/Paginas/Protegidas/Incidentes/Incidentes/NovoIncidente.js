import React, { Component } from 'react';
import { FaTrash, FaPlusCircle } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Card, Modal, Alert } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';

class NovoIncidente extends Component {

    state = {
        incidente: {
            id: null,
            titulo: '',
            descricao: '',
            conclusao: '',
            classificacao: '',
            tipoIncidente: '',
            situacao: 'ABERTA',
            setor: '',
            ncEnvolvidas: []
        },
        update: false,
        ncs: [],
        modalNCS: {
            visivel: false,
            termo: null
        }
    }

    _timerTermosNC = null;

    async componentDidMount() {
        this._carregaIncidentes();
    }

    async _carregaIncidentes() {
        if (this.props.match !== undefined && this.props.match.params['id'] !== undefined) {

            const id = parseInt(this.props.match.params['id']);
            const incCli = cliente().incidentes;

            const resp = await incCli.consultaId(id);

            if (resp.sucesso) {
                const incidente = resp.retorno;
                this.setState({ ...this.state, incidente, update: true });
            } else {
                ToastManager.erro("Erro ao carregar incidente. Tente mais tarde.");
            }
        }
    }

    async _cadastrarIncidente(event) {
        event.preventDefault();

        const incCli = cliente().incidentes;
        const resp = await incCli.salvar(this.state.incidente);

        if (resp.sucesso) {
            const estado = this.state.update ? 'atualizado' : 'registrado';
            ToastManager.sucesso(`Incidente ${estado} com sucesso.`)
            this._reset();
        } else {
            ToastManager.erro(`Erro: ${resp.erro}`);
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

    _valorIncidente(event) {
        const { name, value } = event.target;
        const { incidente } = this.state;

        this.setState({ incidente: { ...incidente, [name]: value } });
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

    _adicionaNC(nc) {
        let { incidente, ncs } = this.state;
        const { ncEnvolvidas } = incidente;

        ncEnvolvidas.push(nc);
        ncs = ncs.filter(n => n.id != nc.id);

        ToastManager.informativo("NC adicionada ao incidente em edição.");

        this.setState({
            ...this.state,
            incidente: { ...incidente, ncEnvolvidas },
            ncs
        });
    }

    _removeNC(nc) {
        const { incidente } = this.state;
        let { ncEnvolvidas } = incidente;
        ncEnvolvidas = ncEnvolvidas.filter(n => n.id != nc.id);

        ToastManager.atencao("NC removida do incidente em edição.");

        this.setState({ incidente: { ...incidente, ncEnvolvidas } })
    }

    _reset() {

        if (this.state.update) {
            this.props.history.push('/dashboard/incidentes')
        } else {
            this.setState({
                incidente: {
                    id: null,
                    titulo: '',
                    descricao: '',
                    conclusao: '',
                    classificacao: '',
                    tipoIncidente: '',
                    situacao: '',
                    setor: '',
                    ncEnvolvidas: []
                }
            });
        }
    }

    _toggleModalNCS() {
        this.setState({ ...this.state, ncs: [], modalNCS: { visivel: !this.state.modalNCS.visivel, termo: null } })
    }

    render() {

        const tipo = this.state.update ? 'Atualização de' : 'Novo';

        let btAcao = "Cadastrar";
        let btCancela = "Limpar";

        if (this.state.update) {
            btAcao = "Atualizar";
            btCancela = "Retornar";
        }

        return (
            <div className="App">
                <Container>
                    <div className="App-jumbo">
                        <Row>
                            <Col md><h1>{tipo} Incidente</h1></Col>
                        </Row>
                        <Form onSubmit={this._cadastrarIncidente.bind(this)}>
                            <Row>
                                <Col md></Col>
                                <Col md="4">
                                    <Form.Group controlId="gTituloNC">
                                        <Form.Label>Título</Form.Label>
                                        <Form.Control type="text" name="titulo" value={this.state.incidente.titulo} onChange={this._valorIncidente.bind(this)} minLength="5" required />
                                        <Form.Control.Feedback type="invalid">
                                            Forneça título
                                            </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Form.Group controlId="gClassificacao">
                                        <Form.Label>Classificação de Impacto</Form.Label>
                                        <Form.Control as="select" name="classificacao" value={this.state.incidente.classificacao} onChange={this._valorIncidente.bind(this)} required>
                                            <option placeholder="Selecione"></option>
                                            <option value="BAIXO">Baixo</option>
                                            <option value="MEDIO">Médio</option>
                                            <option value="ALTO">Alto</option>
                                            <option value="CRITICO">Crítico</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Form.Group controlId="gTipo">
                                        <Form.Label>Tipo</Form.Label>
                                        <Form.Control as="select" name="tipoIncidente" value={this.state.incidente.tipoIncidente} onChange={this._valorIncidente.bind(this)} required>
                                            <option placeholder="Selecione"></option>
                                            <option value="SEM_DANO">Sem Danos Aparentes</option>
                                            <option value="PARADA">Parada de Produção</option>
                                            <option value="DANO_FINANCEIRO">Dano Financeiro</option>
                                            <option value="DANO_PESSOA">Dano a Pessoa</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md></Col>
                            </Row>
                            <Row>
                                <Col md="1"></Col>
                                <Col md="8">
                                    <Form.Group controlId="gDescricao">
                                        <Form.Label>Descrição</Form.Label>
                                        <Form.Control as="textarea" name="descricao" value={this.state.incidente.descricao} onChange={this._valorIncidente.bind(this)} size="500" minLength="10" required />
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Form.Group controlId="gSetorNC">
                                        <Form.Label>Setor de Ocorrência</Form.Label>
                                        <Form.Control as="select" name="setor" value={this.state.incidente.setor} onChange={this._valorIncidente.bind(this)} required>
                                            <option placeholder="Selecione"></option>
                                            <option value="COMPRAS">Compras</option>
                                            <option value="LINHA">Linha de Montagem</option>
                                            <option value="LOGISTICA">Logística</option>
                                            <option value="INSPECAO">Inspeção de Produção</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                            </Row>
                            {
                                this.state.incidente.id !== null ?
                                    (<Row>
                                        <Col md="1"></Col>
                                        <Col md="10">
                                            <Form.Group controlId="gConclusao">
                                                <Form.Label>Conclusão do Incidente</Form.Label>
                                                <Form.Control as="textarea" name="conclusao" value={this.state.incidente.conclusao} onChange={this._valorIncidente.bind(this)} size="5000" minLength="10" />
                                            </Form.Group>
                                        </Col>
                                        <Col md></Col>
                                    </Row>) : (<></>)
                            }
                            <Row>
                                <Col md></Col>
                                <Col md="10">
                                    <Form.Group controlId="gncEnvolvidas">
                                        <Row>
                                            <Col style={{ textAlign: 'center' }}>
                                                <Button variant="info" onClick={this._toggleModalNCS.bind(this)}>Adicionar Não Conformidade</Button>
                                            </Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            {this.state.incidente.ncEnvolvidas.map(nc => (
                                                <Col md="4" sm="6">
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
                {this._ModalNCS()}
            </div>
        );
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
                                        <Form.Control type="text" name="termo" placeholder="Termos refente à uma NC..." name="termos" onChange={this._valorTermosNCS.bind(this)} />
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

export default NovoIncidente;