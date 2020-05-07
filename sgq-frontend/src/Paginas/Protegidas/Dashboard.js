import React, { Component } from 'react';
import { Container, Row, Col, Jumbotron } from 'react-bootstrap';
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
