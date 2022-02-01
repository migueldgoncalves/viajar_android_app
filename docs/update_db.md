## Actualizar a base de dados da aplicação

A base de dados da aplicação é gerida a partir do projecto Python [Viajar](https://github.com/migueldgoncalves/Viajar). É lá que se encontram tanto os ficheiros `.csv` que contêm a informação, como o script que permite criar a partir deles uma base de dados SQLite para uso nesta aplicação.

Este tutorial descreve como actualizar a base de dados desta aplicação.

### Requisitos

- Android Studio
- Projecto Python *Viajar* (disponível [aqui](https://github.com/migueldgoncalves/Viajar))
- Python 3.9

**Nota**: É recomendável colocar o projecto Android e o projecto Python fora da drive C: (por exemplo, colocando-os na drive D:) de modo a não serem precisos privilégios de administrador.

Tutorial testado em Windows.

### Passos

1. Dentro do projecto Python, entrar com o explorador de ficheiros na pasta `src\viajar`;
2. Abrir o ficheiro `sqlite_interface` com um editor de texto simples como o Notepad;
3. Se os paths das pastas dos projectos estão correctos, saltar para o passo 6.
4. Alterar o path da pasta do projecto Android para o correcto na variável `PASTA_DESTINO`, deixando intacta a parte a partir de `\\Viajar`.
   1. Ao se escrever este tutorial, o projecto Android estava em `D:\\AndroidStudioProjects`
   2. Caracteres \ devem ser escritos como \\\
5. Alterar o path da pasta do projecto Python para o correcto na variável `PASTA_CSV`, deixando intacta a parte a partir de `\\Viajar`.
   1. Ao se escrever este tutorial, o projecto Python estava em `D:\\PycharmProjects`
   2. Caracteres \ devem ser escritos como \\\
6. Dentro do projecto Python, entrar na pasta `src`;
7. Correr o script `main` usando o Python. Pode ser preciso clicar com o botão direito do rato e depois clicar em *Abrir com*;
8. Inserir o número correspondente à opção *Criador de bases de dados SQLite* (actualmente é o 6) e carregar em ENTER;
9. A execução do script deverá ser imediata. Caso a criação da base de dados tenha sido bem-sucedida, clicar em qualquer tecla irá sair do script.

A base de dados está agora actualizada e a aplicação pode ser instalada num telemóvel ou Android Virtual Device.