import jwt from 'jwt-decode';

class LoginManager {

    async login(username, password) {
        const response = await fetch('/token', { method: 'POST', body: `grant_type=password&username=${username}&password=${password}&scope=any`, headers: { 'Content-Type': 'application/x-www-form-urlencoded' } });
        const statusCode = response.status;

        if (statusCode === 200) {
            const jsonR = await response.json();

            localStorage.setItem('token', jsonR.access_token);
            localStorage.setItem('usuario', JSON.stringify(jwt(jsonR.access_token)));

            return true;
        } 
        
        console.error(`Erro ao tentar login: ${statusCode}`);
        
        return false;

    }

    user() {
        let u = JSON.parse(localStorage.getItem('usuario'));

        if (this._expirado(u)) {
            return null;
        }

        return u;
    }

    token() {

        let u = JSON.parse(localStorage.getItem('usuario'));

        if (this._expirado(u)) {
            return null;
        }

        return localStorage.getItem('token');
    }

    logoff(){
        return this._expirado(null);
    }

    _expirado(u) {

        if (u == null || u.exp <= (new Date().getTime()) / 1000) {
            localStorage.removeItem('token', null);
            localStorage.removeItem('usuario', null);

            console.log('Token do usuario expirou ou foi realizado o logoff');

            return true;
        }

        return false;
    }

}

export default LoginManager;