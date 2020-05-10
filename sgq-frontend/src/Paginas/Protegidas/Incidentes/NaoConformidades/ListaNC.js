import React, { Component } from 'react';
import { FaEdit, FaAngleRight, FaAngleLeft, FaAngleDoubleRight, FaEye } from 'react-icons/fa';
import { Row, Col, Container, Form, Button, Tabs, Tab, Table } from 'react-bootstrap';

import ToastManager from '../../../../Componentes/ToastManager';

import { cliente } from '../../../../Componentes/SGQClient';

class ListaNC extends Component {

    state = {
        tipo: 'abertas',
        ncs: [],
        pagina: {
            habilitada: true,
            pagina: 0,
            paginas: 0,
            total: 0,
            registros: 10
        }

    }

    async componentDidMount() {
        this._carregaNCEscopo(this.state.tipo);
    }

    async _carregaNCEscopo(tipo) {
        const ncCli = cliente().naoConformidades;
        const resp = await ncCli.consultaEstado(tipo);

        if (resp.sucesso) {
            const ncs = resp.retorno;
            this.setState({ ...this.state, tipo, ncs });
        } else {
            if (resp.status === 404) {
                ToastManager.atencao("Não existem não conformidades no estado desejado");
            }
        }
    }

    render() {
        return (
            <div className="App">
                <Container>
                    <Row>
                        <Col md><h1 style={{ textAlign: 'center' }}>Não Conformidades</h1></Col>
                    </Row>
                    <Row>
                        <Col md></Col>
                        <Col md="10">
                            <Tabs defaultActiveKey="abertas" id="tabNCS" onSelect={this._carregaNCEscopo.bind(this)}>
                                <Tab eventKey="abertas" title="Abertas" />
                                <Tab eventKey="em_analise" title="Em Análise" />
                                <Tab eventKey="concluidas" title="Concluídas" />
                            </Tabs>
                            {this._SecaoEstados()}
                        </Col>
                        <Col md></Col>
                    </Row>
                </Container>
            </div>
        );
    }

    _SecaoEstados() {

        const { tipo } = this.state;

        return (
            <Table responsive striped hover>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Título</th>
                        <th>Setor Afetado</th>
                        <th>Tipo</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        this.state
                            .ncs
                            .map(n => (
                                <tr key={n.id}>
                                    <td>{n.id}</td>
                                    <td>{n.titulo}</td>
                                    <td>{n.setor}</td>
                                    <td>{n.tipoNaoConformidade}</td>
                                    <td>
                                        <Button variant="success" onClick={() => false}><FaEye /></Button>{' '}
                                        {
                                            tipo !== 'concluidas' ?
                                                (
                                                    <>
                                                        <Button variant="primary" onClick={() => false}><FaEdit /></Button>{' '}
                                                    </>
                                                ) : (<></>)
                                        }
                                        {
                                            tipo === 'abertas' ?
                                                (
                                                    <>
                                                        <Button variant="info" onClick={() => false}><FaAngleRight /></Button>{' '}
                                                        <Button variant="warning" onClick={() => false}><FaAngleDoubleRight /></Button>
                                                    </>
                                                ) : tipo === 'em_analise' ?
                                                    (
                                                        <>
                                                            <Button variant="info" onClick={() => false}><FaAngleLeft /></Button>{' '}
                                                            <Button variant="info" onClick={() => false}><FaAngleRight /></Button>
                                                        </>
                                                    ) : (
                                                        <></>
                                                    )
                                        }

                                    </td>
                                </tr>
                            )
                            )
                    }
                </tbody>
            </Table>
        )
    }


}

export default ListaNC;