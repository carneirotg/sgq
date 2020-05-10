import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';

import React, { Component } from 'react';
import { Button, Container, Col, Row, Tabs, Tab, Spinner } from 'react-bootstrap';
import { DateRangePicker } from 'react-dates';

import moment from 'moment';

import { cliente } from '../../../Componentes/SGQClient';
import ToastManager from '../../../Componentes/ToastManager';


class RelIncidente extends Component {

    state = {
        carregando: {
            mesAtual: false,
            anoAtual: false,
            seisMeses: false,
            dozeMeses: false,
            periodo: false
        },
        periodo: {
            inicio: null,
            fim: null
        },
        focusedInput: null
    }

    async _geraRelatorio(tipo) {
        this.setState({ carregando: { ...this.state.carregando, [tipo]: true } });

        if (tipo === 'periodo') {
            const { inicio, fim } = this.state.periodo;

            if (inicio == null || fim == null) {
                return ToastManager.atencao("Data(s) inválida(s) informada(s).");
            }
        }

        const resp = await cliente().relatorios.gerar(tipo, this.state.periodo);

        if (resp.sucesso) {
            ToastManager.sucesso("Relatório gerado com sucesso.");
        }

        this.setState({
            carregando: {
                mesAtual: false,
                anoAtual: false,
                seisMeses: false,
                dozeMeses: false,
                periodo: false
            }
        });
    }

    _botaoHabilitado() {

        const emGeracao = Object.values(this.state.carregando).some(c => c == true);
        return emGeracao;
    }

    render() {
        moment.locale('pt-br');

        return (
            <div className="App">
                <Container>
                    <Row>
                        <Col md><h1 style={{ textAlign: 'center' }}>Relatórios - Incidentes</h1></Col>
                    </Row>
                    <Row>
                        <Col md></Col>
                        <Col md="10">
                            <Tabs defaultActiveKey="fixo" id="tabRelIncidente">
                                <Tab eventKey="fixo" title="Períodos Fixos">
                                    <Container>
                                        <br />
                                        <Row>
                                            <Col md="6">
                                                <Button variant="primary" block onClick={this._geraRelatorio.bind(this, 'mesAtual')} disabled={this._botaoHabilitado()}>
                                                    Mês atual
                                                {
                                                        this.state.carregando.mesAtual ? (
                                                            <Spinner
                                                                as="span"
                                                                animation="grow"
                                                                size="sm"
                                                                role="status"
                                                                aria-hidden="true"
                                                            />
                                                        ) : (<></>)
                                                    }
                                                </Button>
                                            </Col>
                                            <Col md="6">
                                                <Button variant="primary" block onClick={this._geraRelatorio.bind(this, 'anoAtual')} disabled={this._botaoHabilitado()} >
                                                    Ano atual
                                                    {
                                                        this.state.carregando.anoAtual ? (
                                                            <Spinner
                                                                as="span"
                                                                animation="grow"
                                                                size="sm"
                                                                role="status"
                                                                aria-hidden="true"
                                                            />
                                                        ) : (<></>)
                                                    }
                                                </Button>
                                            </Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            <Col md="6">
                                                <Button variant="primary" block onClick={this._geraRelatorio.bind(this, 'seisMeses')} disabled={this._botaoHabilitado()}>
                                                    Últimos seis meses
                                                    {
                                                        this.state.carregando.seisMeses ? (
                                                            <Spinner
                                                                as="span"
                                                                animation="grow"
                                                                size="sm"
                                                                role="status"
                                                                aria-hidden="true"
                                                            />
                                                        ) : (<></>)
                                                    }
                                                </Button>
                                            </Col>
                                            <Col md="6">
                                                <Button variant="primary" block onClick={this._geraRelatorio.bind(this, 'dozeMeses')} disabled={this._botaoHabilitado()}>
                                                    Últimos doze meses
                                                    {
                                                        this.state.carregando.dozeMeses ? (
                                                            <Spinner
                                                                as="span"
                                                                animation="grow"
                                                                size="sm"
                                                                role="status"
                                                                aria-hidden="true"
                                                            />
                                                        ) : (<></>)
                                                    }
                                                </Button>
                                            </Col>
                                        </Row>
                                    </Container>
                                </Tab>
                                <Tab eventKey="variavel" title="Períodos Arbitrários">
                                    <Container>
                                        <br />
                                        <Row>
                                            <Col md="2"></Col>
                                            <Col md="8" style={{ textAlign: 'center' }}>Selecione um período de até 12 meses</Col>
                                            <Col md="2"></Col>
                                        </Row>
                                        <Row>
                                            <Col md="2"></Col>
                                            <Col md="8" style={{ textAlign: 'center' }}>
                                                <DateRangePicker
                                                    isOutsideRange={() => false}
                                                    startDate={this.state.periodo.inicio}
                                                    startDateId="inicio"
                                                    endDate={this.state.periodo.fim}
                                                    endDateId="fim"
                                                    onDatesChange={({ startDate, endDate }) => this.setState({ periodo: { inicio: startDate, fim: endDate } })}
                                                    focusedInput={this.state.focusedInput}
                                                    onFocusChange={focusedInput => this.setState({ focusedInput })}
                                                />
                                            </Col>
                                            <Col md="2"></Col>
                                        </Row>
                                        <br />
                                        <Row>
                                            <Col md="2"></Col>
                                            <Col md="8" style={{ textAlign: 'center' }}>
                                                <Button variant="primary" onClick={this._geraRelatorio.bind(this, 'periodo')}>
                                                    Gerar Relatório
                                                {
                                                        this.state.carregando.mesAtual ? (
                                                            <Spinner
                                                                as="span"
                                                                animation="grow"
                                                                size="sm"
                                                                role="status"
                                                                aria-hidden="true"
                                                            />
                                                        ) : (<></>)
                                                    }
                                                </Button>
                                            </Col>
                                            <Col md="2"></Col>
                                        </Row>
                                        <br />
                                    </Container>
                                </Tab>
                            </Tabs>
                        </Col>
                        <Col md></Col>
                    </Row>
                </Container>
            </div>
        );
    }


}

export default RelIncidente;