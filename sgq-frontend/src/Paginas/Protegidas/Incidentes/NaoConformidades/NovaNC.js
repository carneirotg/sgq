import React, { Component } from 'react';
import { FaLightbulb } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Accordion, Card, Modal } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';

class NovaNC extends Component {

    state = {
        nc: {
            id: null,
            titulo: '',
            setor: '',
            resumo: '',
            tipoNaoConformidade: '',
            detalhamentoNaoConformidade: '',
            artefato: { id: null },
            detalhamentoArtefato: '',
            prejuizoApurado: false,
            normaNaoConformidade: {}
        },
        update: false,
        normaBusca: null,
        artefatos: [],
        normas: [],
        modalTipos: false
    }

    async componentDidMount() {
        this._carregaArtefatos();
        this._carregaNormas();

        this._carregaNC();
    }

    async _carregaNC() {
        if (this.props.match !== undefined && this.props.match.params['id'] !== undefined) {

            const id = parseInt(this.props.match.params['id']);
            const ncCli = cliente().naoConformidades;

            const resp = await ncCli.consultaId(id);

            if (resp.sucesso) {
                const nc = resp.retorno;
                this.setState({ ...this.state, nc, normaBusca: nc.normaNaoConformidade.normaId, update: true });
            } else {
                ToastManager.erro("Erro ao carregar não conformidade. Tente mais tarde.");
            }
        }
    }

    async _carregaArtefatos() {
        const artefatosCli = cliente().artefatos;

        const resp = await artefatosCli.listar({ habilitada: true, pagina: 0, registros: 100000 });

        if (resp.sucesso) {
            let arts = resp.retorno;

            if (arts !== null) {
                arts.sort((a, b) => a.nome.localeCompare(b.nome));
            }

            this.setState({ artefatos: arts.filter(a => !a.depreciado) });
        } else {
            ToastManager.erro("Erro ao carregar lista de artefatos. Tente mais tarde.");
        }
    }

    async _carregaNormas() {
        const normasCli = cliente().normas;

        const resp = await normasCli.listar({ habilitada: true, pagina: 0, registros: 100000 });

        if (resp.sucesso) {
            const normas = resp.retorno;

            this.setState({ normas });
        } else {
            ToastManager.erro("Erro ao carregar lista de normas. Tente mais tarde.");
        }
    }

    _valoresNC(event) {
        let { name, value, checked } = event.target;


        if (name.includes('prejuizoApurado')) {
            value = checked;
        } else if (name.includes('artefatoId')) {
            name = "artefato";
            value = { id: value };
        } else if (name.includes('normaNaoConformidade')) {
            this.setState({ normaBusca: value })
            value = this.state.normas.find(n => n.normaId === parseInt(value));
        }

        this.setState({ nc: { ...this.state.nc, [name]: value } });
    }

    async _cadastrarNC(event) {
        event.preventDefault();

        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.salvar(this.state.nc);

        if (resp.sucesso) {
            const estado = this.state.update ? 'atualizada' : 'registrada';
            ToastManager.sucesso(`Não conformidade ${estado} com sucesso!`);
            this._reset();
        } else {
            ToastManager.erro(`Erro ao cadastrar NC: ${resp.erro}`);
        }
    }

    _reset() {
        this.setState({
            ...this.state,
            normaBusca: '',
            nc: {
                id: null,
                titulo: '',
                setor: '',
                resumo: '',
                tipoNaoConformidade: '',
                detalhamentoNaoConformidade: '',
                artefato: { id: '' },
                detalhamentoArtefato: '',
                prejuizoApurado: false
            }
        })
    }

    render() {
        return (

            <div className="App">
                <Container>
                    <div className="App-jumbo">
                        <Row>
                            <Col md><h1>Nova Não Conformidade</h1></Col>
                        </Row>
                        <Form onSubmit={this._cadastrarNC.bind(this)}>
                            <Row>
                                <Col md></Col>
                                <Col md="4">
                                    <Form.Group controlId="gTituloNC">
                                        <Form.Label>Título da NC</Form.Label>
                                        <Form.Control type="text" name="titulo" value={this.state.nc.titulo} onChange={this._valoresNC.bind(this)} minLength="5" required />
                                        <Form.Control.Feedback type="invalid">
                                            Forneça título
                                            </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Form.Group controlId="gTipoNC">
                                        <Form.Label>Tipo Constatado <sup><FaLightbulb style={{ color: '#2d9c21', cursor: 'pointer' }} onClick={() => this.setState({ modalTipos: true })} /></sup></Form.Label>
                                        <Form.Control as="select" name="tipoNaoConformidade" value={this.state.nc.tipoNaoConformidade} onChange={this._valoresNC.bind(this)} required>
                                            <option placeholder="Selecione"></option>
                                            <option value="ANOMALIA">Anomalia Diversa</option>
                                            <option value="AUSENCIA_MEDIDA">Ausência de Medida</option>
                                            <option value="ESPECIFICACAO_DIVERGENTE">Especificação Divergente</option>
                                            <option value="QUANTIDADE_DIVERGENTE">Quantidade Divergente</option>
                                            <option value="INSTRUCAO_DIVERGENTE">Instrução Divergente</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Form.Group controlId="gSetorNC">
                                        <Form.Label>Setor de Ocorrência</Form.Label>
                                        <Form.Control as="select" name="setor" value={this.state.nc.setor} onChange={this._valoresNC.bind(this)} required>
                                            <option placeholder="Selecione"></option>
                                            <option value="COMPRAS">Compras</option>
                                            <option value="LINHA">Linha de Montagem</option>
                                            <option value="LOGISTICA">Logística</option>
                                            <option value="INSPECAO">Inspeção de Produção</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md></Col>
                            </Row>
                            <Row>
                                <Col md="1"></Col>
                                <Col md="8">
                                    <Form.Group controlId="gResumoNC">
                                        <Form.Label>Resumo</Form.Label>
                                        <Form.Control type="text" name="resumo" value={this.state.nc.resumo} onChange={this._valoresNC.bind(this)} minLength="10" required />
                                        <Form.Control.Feedback type="invalid">
                                            Forneça um resumo da NC
                                            </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Row>
                                        <Col md>&nbsp;</Col>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group controlId="gPrejuizoNC">
                                                <Form.Check type="checkbox" id="prejuizoApurado" name="prejuizoApurado" label="Prejuízo Apurado" checked={this.state.nc.prejuizoApurado} onChange={this._valoresNC.bind(this)} />
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                </Col>
                            </Row>
                            <Row>
                                <Col md="1"></Col>
                                <Col md="7">
                                    <Form.Group controlId="gDetalheNC">
                                        <Form.Label>Detalhamento</Form.Label>
                                        <Form.Control as="textarea" name="detalhamentoNaoConformidade" value={this.state.nc.detalhamentoNaoConformidade} onChange={this._valoresNC.bind(this)} size="500" minLength="10" required />
                                        <Form.Control.Feedback type="invalid">
                                            Forneça o detalhamento da NC
                                            </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                                <Col md="3">
                                    <Form.Group controlId="gNormaApoio">
                                        <Form.Label>Norma de Apoio</Form.Label>
                                        <Form.Control as="select" name="normaNaoConformidade" value={this.state.normaBusca} onChange={this._valoresNC.bind(this)}>
                                            <option placeholder="Selecione"></option>
                                            {
                                                this.state.normas.map(n => (
                                                    <option key={n.normaId} value={n.normaId}>{n.norma}</option>
                                                ))
                                            }
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md></Col>
                            </Row>
                            <Row>
                                <Col md="1"></Col>
                                <Col md="4">
                                    <Form.Group controlId="gArtefatoEnvolvido">
                                        <Form.Label>Artefato Envolvido</Form.Label>
                                        <Form.Control as="select" name="artefatoId" value={this.state.nc.artefato.id} onChange={this._valoresNC.bind(this)} required>
                                            <option placeholder="Selecione"></option>
                                            {
                                                this.state.artefatos.map(a => (
                                                    <option key={a.id} value={a.id}>{a.nome}</option>
                                                ))
                                            }
                                        </Form.Control>
                                    </Form.Group>
                                </Col>
                                <Col md="6">
                                    <Form.Group controlId="gDetalheArtefatoEnvolvido">
                                        <Form.Label>Informações Adicionais</Form.Label>
                                        <Form.Control type="text" placeholder="Número de série, modelo, etcs" name="detalhamentoArtefato" value={this.state.nc.detalhamentoArtefato} onChange={this._valoresNC.bind(this)} required />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row className="Btns">
                                <Col md></Col>
                                <Col md><Button className="Btn" type="submit">Cadastrar</Button></Col>
                                <Col md><Button className="Btn" variant="warning" onClick={this._reset.bind(this)}>Limpar</Button></Col>
                                <Col md></Col>
                            </Row>
                        </Form>
                    </div>
                </Container>
                <br />
                {this._ModalTipos()}
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
                                        <Accordion.Toggle as={Card.Header} eventKey="0">Anomalia Diversa</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="0">
                                            <Card.Body>
                                                Anomalia, irregularidade ou defeito aparente constatado no artefato.
                                                <ul>
                                                    <li><i>Rachaduras aparentes</i></li>
                                                    <li><i>Material apresenta deformidade</i></li>
                                                </ul>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="1">Ausência de Medida</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="1">
                                            <Card.Body>
                                                Não foi realizada a devida medição de um recurso, propriedade ou situação.
                                                <ul>
                                                    <li><i>Tempteratura não está sendo medida regularmente</i></li>
                                                    <li><i>Tempo de processamento de artefato apresenta lacunas</i></li>
                                                </ul>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="2">Especificação Divergente</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="2">
                                            <Card.Body>
                                                Artefato consumido ou produzido está em estado perfeito, contudo não está em acordo com as especificações do mesmo.
                                                <ul>
                                                    <li><i>Artefato não é de aço 1020.</i></li>
                                                    <li><i>Chapa de aço apresenta bitola 0,5cm maior.</i></li>
                                                </ul>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="3">Quantidade Divergente</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="3">
                                            <Card.Body>
                                                Artefato produzido ou consumido em quantidade que difere do esperado.
                                                <ul>
                                                    <li><i>Bombas de combustível apresentam -30% de produtividade por dia.</i></li>
                                                    <li><i>Lotes consumidos de pastilhas de freio apresentam 5 unidades a menos.</i></li>
                                                </ul>
                                            </Card.Body>
                                        </Accordion.Collapse>
                                    </Card>
                                    <Card>
                                        <Accordion.Toggle as={Card.Header} eventKey="4">Instrução Divergente</Accordion.Toggle>
                                        <Accordion.Collapse eventKey="4">
                                            <Card.Body>
                                                Instruções recebidas, interna ou externamente, divergem daquelas que são determinadas pelo padrão local.
                                                <ul>
                                                    <li><i>Instalação de nova solda necessita de adaptações na estrutura elétrica.</i></li>
                                                    <li><i>Temperatura requerida por fornecedor para operação diverge de instruções anteriores.</i></li>
                                                </ul>
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
}

export default NovaNC;