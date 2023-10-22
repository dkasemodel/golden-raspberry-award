# Golden Raspberry Award API
API para gerenciar os dados e ganhadores do Premio Golden Raspberry Award (piores filmes do ano).

## Requisitos
- Java 17 ([Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html));
- [Maven 3.9.x](https://maven.apache.org/download.cgi)

## Makefile (Build, Test and Run)
O projeto disponibliza um arquivo Makefile para rodar comandos básicos como:

### Build
Compilar a aplicação rodando todos os testes.
```shell
make build
```

### Test
Rodar apenas os testes.
```shell
make test
```

### Start
Iniciar a aplicação localmente.
```shell
make start
```

### Default
O comando padrão do Makefile é rodar o `build` e o `start`.
```shell
make
```

## Arquivo inicial de Dados
Para que a aplicação inicie é necessário passar um arquivo (CSV) com dados inciais. Este arquivo deve seguir as seguintes orientações: [csv-file](docs/CSV_FILE_IMPORT.md).

Por padrão, a aplicação irá utilizar o arquivo [initial-data.csv](initial-data.csv), caso seja necessário iniciar a aplicação com outra carga inicial de dados, deve-se rodar o comando `make start` passando o parâmetro `EXTRA_PARAMS=-Daward.initial-data.file` com o caminho (completo ou relativo) do aquivo no qual se deseja utilizar para carregar na aplicaçao.

```shell
make start EXTRA_PARAMS="-Daward.initial-data.file=/home/user/award/list.csv"
```
O Parâmetro `EXTRA_PARAMS` pode ser utilizado para qualquer outro parâmetro necessário para inicar a aplicação, como por exemplo especificação do `HEAD`.
```shell
make start EXTRA_PARAMS="-Xms512mb -Xmx1024mb"
```
É possível também iniciar a aplicação sem dados, mas para isso é necessário informar um arquivo CSV contendo apenas o cabeçalho, e setar o valor da configuração `award.initial-data.ignore-empty-data` para `TRUE`.

Exemplo (arquivo):
```
year;title;studios;producers;winner
```
Exemplo (configuração):
```shell
make start EXTRA_PARAMS="-Daward.initial-data.file=/home/user/award/empty.csv -Daward.initial-data.ignore-empty-data=true"
```

# Rodando a aplicação
Após a aplicação ser iniciada, ela estará disponível na porta 8080. Para testar os endpoints, é possível utulizar o swagger, disponível no endereço [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html). A documentaçãp do OpenAPI estão disponíveis aqui: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).
