import React from 'react';
import { Link } from 'react-router-dom';
import logo from '../../images/logo_sgq_texto.png';
import { Container, Row, Col, Jumbotron, Button } from 'react-bootstrap';
import './Login.css';

function Abertura() {
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
              <Col md><h1>Seja Bem-Vindo ao SGQ!</h1></Col>
            </Row>
            <Row>
              <Col md>
                <Row>
                  <Col md></Col>
                  <Col md="6"><h3>Visitante</h3></Col>
                  <Col md></Col>
                </Row>
                <Row>
                  <Col md>
                    <p>Verifique nosso boletins de eventos e fique atualizado com os últimos acontecimentos.</p>
                    <p>Nossos boletins incluem:</p>

                    <ul>
                      <li>Últimos Incidentes</li>
                      <li>Últimos Recalls Realizados</li>
                    </ul>

                  </Col>
                </Row>
              </Col>
              <Col md>
                <Row>
                  <Col md></Col>
                  <Col md="6"><h3>Colaborador</h3></Col>
                  <Col md></Col>
                </Row>
                <Row>
                  <Col md>
                    <p>Acesse o SGQ ao realizar o login com suas credenciais.</p>
                  </Col>
                </Row>
              </Col>
            </Row>
            <Row>
              <Col md>
                <Col md><Link to="/eventos"><Button className="Btn" variant="info">Acessar Boletim de Eventos</Button></Link></Col>
              </Col>
              <Col md>
                <Col md><Link to="/login"><Button className="Btn">Acessar o SGQ</Button></Link></Col>
              </Col>
            </Row>
          </Jumbotron>
        </div>
      </Container>
    </div>
  );
}

export default Abertura;
