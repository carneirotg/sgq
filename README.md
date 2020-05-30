![Construção e Testes SGQ](https://github.com/dblsilverio/sgq/workflows/Constru%C3%A7%C3%A3o%20e%20Testes%20SGQ/badge.svg)

# SGQ - Sistema de Gestão de Qualidade

Projeto desenvolvido como parte do TCC da especialização em Arquitetura de Software Distribuído 2018 - Grupo 2.

## Tecnologias e Versões

O projeto faz uso das seguintes tecnologias:

* Java 11
* Maven 3.6+
* Spring Boot 2.2.6
* Spring Cloud Hoxton
* React 16.13
* Bootstrap 4
* PostgreSQL 12
* Docker 19+
* Docker Compose 1.25

## Construção - Backend

Acesse o projeto `sgq-parent` e execute o *goal* **install** do Maven:

```shell
user@host:~/sgq-parent$ mvn install
```

Serão executados todos os testes aplicáveis, construções de pacotes e, ao longo do processo, imagens docker também serão criadas para cara projeto, podendo ser evidenciado ao fim do processo:

```shell
user@host:~/sgq-parent$ docker images | grep 1.0.4
sgq-relatorios           1.0.4               f6d08ea515cb        50 years ago        529MB
sgq-transparencia        1.0.4               3a33316002f5        50 years ago        522MB
sgq-gateway-normas       1.0.4               f4c74c2b429f        50 years ago        503MB
sgq-mock-gestao-normas   1.0.4               343218a32c07        50 years ago        487MB
sgq-auth-server          1.0.4               d762e2461b8e        50 years ago        508MB
sgq-gestao-incidentes    1.0.4               c5cc809ac00e        50 years ago        517MB
sgq-service-discovery    1.0.4               5405cc857ae2        50 years ago        491MB
sgq-api-gateway          1.0.4               04ec23dcbb79        50 years ago        488MB
```

Também podem ser apontadas as referências **latest** para as versões atuais geradas.

## Construção - Frontend

Acesse o projeto `sgq-frontend` e execute o script `docker-build.sh`, um facilitado para a geração de build do *Webapp* e imagem docker:

```
user@host:~/sgq-frontend$ ./docker-build.sh

Rodando Yarn Build~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
yarn run v1.22.4
$ react-scripts build
Creating an optimized production build...
...
Construindo imagem Docker 1.0.4~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Successfully built 2280d211655a
Successfully tagged sgq-frontend:latest
```

## Execução Local

### Subindo o NGinx

Com as imagens devidamente construídas, acesse o diretório `sgq-utilitarios/NGinx` e rode o comando `docker-compose up`:

```shell
user@host:~/sgq-frontend$ docker-compose up
Starting sgq-front-proxy ... done
```

Este *docker-compose.yml* é responsável por subir um proxy local para a aplicação. Pode ser necessário o download da imagem `nginx:1.17-alpine`, de pouco menos de 20Mb.

### Subindo o SGQ Local

Inicie o modo Swam do docker local com `docker swarm init`:

```shell
docker swarm init --advertise-addr IP_LOCAL

Swarm initialized: current node (7r1dpofpnwjomjvfk2t02y3eg) is now a manager.
```
Um único nó é o suficiente para rodar o projeto. Ao todo é planejado 5gb de ram necessário para todos todos os módulos, incluindo o NGinx e PostgreSQL.

Siga para a pasta `sgq-utilitarios/SGQCompose` e inicie um deploy do stack SGQ:

```shell
docker stack  deploy --compose-file docker-compose.yml sgq

Creating network sgq_default
Creating service sgq_transparencia
Creating service sgq_gestao-incidentes
Creating service sgq_frontend
Creating service sgq_service-discovery
Creating service sgq_postgres-sgq
Creating service sgq_auth-server
Creating service sgq_mock-normas
Creating service sgq_api-gateway
Creating service sgq_gateway-normas
Creating service sgq_relatorios
```

*É possível utilizar apenas o docker compose para subir o stack. Basta fornecer o parâmetro --compatibility para fazer uso das configurações adequadas de recursos.*

Crie um host local com o nome `sgq.pucminas.local` apontando para o próprio ip do Host em questão:

```shell
x.x.x.x        sgq.pucminas.local
```

Acesse, via browser, o site em http://sgq.pucminas.local/

#### Parâmetros de Memória

Caso necessário, é possível reduzir ou aumentar a memória no docker-compose do stack:

```yaml
gateway-normas:
  image: sgq-gateway-normas
  deploy:
    resources:
      limits:
        memory: 500M -> 800M   
  restart: unless-stopped
```

Em seguida, basta atualizar o serviço ou container via modo de implantação selecionado.
