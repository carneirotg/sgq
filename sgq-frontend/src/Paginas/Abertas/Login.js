import React, { Component } from 'react';
import logo from '../../images/logo_sgq_texto.png';
import { Container, Row, Col, Jumbotron, Button, Form } from 'react-bootstrap';
import './Login.css';
import { Redirect } from 'react-router-dom';
import LoginClient from '../../Componentes/LoginManager';

import Toast from '../../Componentes/ToastManager';

class Login extends Component {

  _loginClient = new LoginClient();

  state = {
    redirectDashboard: false,
    loginForm: { username: '', password: '' }
  }

  _valores(event) {
    const name = event.target.name;
    const value = event.target.value;
    this.setState({ loginForm: { ...this.state.loginForm, [name]: value } });
  }

  _reset(){
    this.setState({loginForm: {username: '', password: ''}});
  }

  async _fazLogin(evt) {
    evt.preventDefault();

    let { username, password } = this.state.loginForm;
    let redir = await this._loginClient.login(username, password);

    if (!redir) {
      Toast.erro("Usuario e/ou senha inválido(s)");
    }

    this.setState({ redirectDashboard: redir });
  }

  _redirectDashboard() {
    if (this.state.redirectDashboard) {
      return (<Redirect to="/dashboard" />);
    }
  }

  render() {

    return (
      <div className="App">
        {this._redirectDashboard()}
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
              <Form onSubmit={this._fazLogin.bind(this)}>
                <Row>
                  <Col md></Col>
                  <Col md="4">
                    <Form.Group controlId="nomeUsuario">
                      <Form.Control type="text" placeholder="Nome de Usuário" name="username" value={this.state.loginForm.username} onChange={this._valores.bind(this)} required />
                    </Form.Group>
                  </Col>
                  <Col md></Col>
                </Row>
                <Row>
                  <Col md></Col>
                  <Col md="4">
                    <Form.Group controlId="senhaUsuario">
                      <Form.Control type="password" placeholder="Senha de Usuário" name="password" value={this.state.loginForm.password} onChange={this._valores.bind(this)} required/>
                      <Form.Text className="text-muted" style={{ textAlign: 'center' }}>
                        <i>gestor / g123</i> <b>ou</b> <i>analista / a123</i>
                      </Form.Text>
                    </Form.Group>
                  </Col>
                  <Col md></Col>
                </Row>
                <Row className="Btns">
                  <Col md></Col>
                  <Col md><Button className="Btn" type="submit">Entrar</Button></Col>
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

export default Login;
