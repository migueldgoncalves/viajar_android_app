## Actualizar a base de dados da aplicação

Com a actualização 1.7, a base de dados deixou de ser gerada durante cada execução normal da aplicação. Em vez disso, durante o desenvolvimento a base de dados passou a ser gerada uma única vez e a ser colocada depois nos assets, para ser importada durante a execução normal da aplicação.

Este tutorial descreve como criar a base de dados para ser colocada nos assets, a partir dos ficheiros `.csv` do projecto Python *Viajar*.  

### Requisitos

- Android Studio
- Android Virtual Device (AVD) com Android 6+
- DB Browser for SQLite (disponível [aqui](https://sqlitebrowser.org/))
- Projecto Python *Viajar* (ficheiros `.csv` disponíveis [aqui](https://github.com/migueldgoncalves/Viajar/tree/master/src/viajar/base_dados))

Não é necessário ter um dispositivo rooted.

### Passos

1. No Android Studio, em `DBInterface.java`, alterar a variável `developerMode` para `true`;
2. Copiar os ficheiros `.csv` para a pasta `app\src\main\res\raw` do projecto Android (em Windows, disponível por omissão em `C:\Users\<nome_de_utilizador>\AndroidStudioProjects\Viajar`). Se já lá estiverem ficheiros `.csv`, substituir pelos actuais;
3. Arrancar a aplicação no AVD;
4. Carregar em `Iniciar`, depois em `Viagem`. Isto irá iniciar o popular da base de dados;
5. O progresso do popular da base de dados pode ser acompanhado no separador `Run`, num total de cerca de 20 segundos. A aplicação não irá entrar na Travel Activity;
6. Com o AVD ligado, aceder ao Device File Explorer do Android Studio e seleccionar o AVD;
7. Ir a `data/data/com.viajar.viajar/databases` e copiar os ficheiros `Travel`, `Travel-shm` e `Travel-wal` para uma localização à escolha, como o Ambiente de Trabalho;
8. Com o DB Browser for SQLite, abrir o ficheiro `Travel`, e fechar de seguida. Os ficheiros `Travel-shm` e `Travel-wal` irão desaparecer;
9. Copiar ou mover o ficheiro `Travel` para a pasta `app\src\main\assets` do projecto Android, e substituir pelo ficheiro que lá estiver;
10. Em `DBInterface.java`, alterar a flag `developerMode` para `false`;
11. (Opcional) Apagar os ficheiros `.csv` do projecto Android;
12. (Opcional) Alterar a versão da aplicação no Translations Editor e no ficheiro `build.gradle` associado ao módulo `Viajar.app`.

A aplicação está pronta para ser executada normalmente.