import React from 'react';
import logo from '../../images/logo_sgq_texto.png';
import { Container, Row, Col, Jumbotron, Button, Form } from 'react-bootstrap';
import './Login.css';

function Login() {
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
        <div className="App-jumbo">
          <Jumbotron>
            <Row>
              <Col md><h1>Login</h1></Col>
            </Row>
            <Row>
              <Col md></Col>
              <Col md="4">
                <Form.Group controlId="nomeUsuario">
                  <Form.Control type="text" placeholder="Nome de Usuário" />
                </Form.Group>
              </Col>
              <Col md></Col>
            </Row>
            <Row>
              <Col md></Col>
              <Col md="4">
                <Form.Group controlId="senhaUsuario">
                  <Form.Control type="password" placeholder="Senha de Usuário" />
                  <Form.Text className="text-muted" style={{textAlign: 'center'}}>
                    <i>gestor / g123</i> <b>ou</b> <i>analista / a123</i>
                  </Form.Text>
                </Form.Group>
              </Col>
              <Col md></Col>
            </Row>
            <Row className="Btns">
              <Col md></Col>
              <Col md><Button className="Btn">Entrar</Button></Col>
              <Col md><Button className="Btn" variant="warning">Limpar</Button></Col>
              <Col md></Col>
            </Row>
          </Jumbotron>
        </div>
      </Container>
    </div>
  );
}

export default Login;
