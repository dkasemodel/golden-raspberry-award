# CSV File to Import
Para iniciar a aplicação é necessário informar um arquivo CSV contendo os dados a serem carregados na aplicação na inicialização dela.

Estes dados devem seguir os seguintes requisitos que serão descritos abaixo.

## Cabeçalho do CSV
O arquivo devo conter na primeira linha o cabeçalho dos dados que serão carregados. Ele deve ser idêntico ao abaixo:
```csv
year;title;studios;producers;winner
```
> [!WARNING]
> A primeira linha do CSV sempre será ignorada, pois ela precisa conter o cabeçalho!

## Dados das colunas
Cada coluna deve seguir o padrão descrito abaixo.

### YEAR
Deve conter a informação de um ano com 4 dígitos. Números com quantidade de dígitos diferente de 4 será lançada exceção e a aplicação não irá iniciar.

### TITLE
Texto livre com até 255 caracteres contendo o Título do Filme.

O título não pode se repetir, não podem haver mais de um filme com o mesmo título.

### STUDIOS
Lista dos Estúdios nos quais participaram da criação do filme. Caso existe mais de um, os mesmos podem ser separados com `;` entre eles ou então ` and `.

> [!WARNING]
> Quando o ` and ` for utilizado, obrigatoriamente deve existir 1 (um) espaço antes e depois.
> > Ex.: MGM, United Artists and Cannon Films

### PRODUCERS
Lista dos produtores do filme. Caso existe mais de um, os mesmos podem ser separados com `;` entre eles ou então ` and `.

> [!WARNING]
> Quando o ` and ` for utilizado, obrigatoriamente deve existir 1 (um) espaço antes e depois.
> > Ex.: David Joseph, Yoram Globus and Menahem Golan

### WINNER
A coluna deve conter os valores `yes`, `no` ou _vazio_.

> [!WARNIN]
> Qualquer valor diferente de YES / NO / _VAZIO_ a aplicação irá lançar uma exceção e não será iniciada.

#### yes
Informa de que o filme é vencedor.

#### no / _vazio_
Informa de que o filme não é vencedor.

## Exemplo de arquivo

```
year;title;studios;producers;winner
1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes
1983;Hercules;MGM, United Artists, Cannon Films;Yoram Globus and Menahem Golan;
```
