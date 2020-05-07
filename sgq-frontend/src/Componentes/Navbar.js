import React, { Component } from 'react';
import { Button, Form, FormControl, Nav, NavDropdown, Navbar } from 'react-bootstrap';

import { Link, Redirect } from 'react-router-dom';

import LoginManager from './LoginManager';

import logo from '../images/logo_sgq.png';


class NavbarSGQ extends Component {

    state = {
        logoffRealizado: false
    }

    _lm = new LoginManager();

    efetuarLogoff() {
        this.setState({ logoffRealizado: this._lm.logoff() });
    }

    _redirecionar() {
        if (this.state.logoffRealizado) {
            return (<Redirect to="/" />);
        }
    }

    render() {
        return (
            <Navbar bg="light" expand="lg">
                {this._redirecionar()}
                <Navbar.Brand as={Link} to="/dashboard">
                    <img
                        alt="SGQ Logo"
                        src={logo}
                        width="32"
                        height="32"
                        className="d-inline-block align-top"
                    />{' '}SGQ</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Nav.Link as={Link} to="/dashboard">Inicío</Nav.Link>
                        <NavDropdown title="Artefatos" id="basic-nav-dropdown">
                            <NavDropdown.Item as={Link} to="/dashboard/artefato">Cadastrar</NavDropdown.Item>
                            <NavDropdown.Item as={Link} to="/dashboard/artefatos">Buscar</NavDropdown.Item>                            
                        </NavDropdown>
                        <NavDropdown title="Não Conformidades" id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Cadastrar</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">Buscar</NavDropdown.Item>   
                            <NavDropdown.Divider />                         
                            <NavDropdown.Item href="#action/3.2">Normas</NavDropdown.Item>   
                        </NavDropdown>
                        <NavDropdown title="Incidentes" id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Cadastrar</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">Buscar</NavDropdown.Item>                            
                            <NavDropdown.Divider />
                            <NavDropdown.Item href="#action/3.2">Relatórios</NavDropdown.Item>                            
                        </NavDropdown>
                        <NavDropdown title="Campanhas" id="basic-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Cadastrar</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">Buscar</NavDropdown.Item>    
                            <NavDropdown.Divider />
                            <NavDropdown.Item href="#action/3.1">Destinatários</NavDropdown.Item>
                        </NavDropdown>

                    </Nav>
                    <Button variant="danger" onClick={this.efetuarLogoff.bind(this)}>Logoff</Button>
                </Navbar.Collapse>
            </Navbar>
        )
    }

}

export default NavbarSGQ;