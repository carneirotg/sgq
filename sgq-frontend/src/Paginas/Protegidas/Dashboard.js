import React, { Component } from 'react';
import logo from '../../images/logo_sgq_texto.png';
import { Container, Row, Col, Jumbotron, Button, Form } from 'react-bootstrap';
import './Dashboard.css';

class Dashboard extends Component {

  render() {

    return (
      <div className="App">
        <Container>
          <div className="App-jumbo">
            <Jumbotron>
              <Row>
                <Col md><h1>Dashboard</h1></Col>
              </Row>

            </Jumbotron>
          </div>
        </Container>
      </div>
    );
  }
}

export default Dashboard;
