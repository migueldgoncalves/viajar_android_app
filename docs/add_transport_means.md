## Adicionar meios de transporte à viagem

Este tutorial descreve como adicionar suporte para novos meios de transporte presentes na base de dados deste projecto.

### Requisitos

O [Android Studio](https://developer.android.com/studio) é recomendado para criar um novo meio de transporte, e foi o IDE usado para criar este tutorial. Conhecimentos gerais de programação são também recomendados.

### Passos

0. Abrir este projecto com o Android Studio.
1. Obter um ícone que represente o meio de transporte. Devem ser imagens com cerca de 50x50 píxeis.
   * Uma possibilidade é pesquisar por [android icons](https://www.google.com/search?q=android+icons&rlz=1C1GCEA_enPT854PT854&oq=android+icons&aqs=chrome.0.69i59.1634j0j7&sourceid=chrome&ie=UTF-8) no Google.
   * Outra possibilidade é adicionar imagens [directamente pelo Android Studio](https://developer.android.com/studio/write/vector-asset-studio#running).
2. Se o ícone não foi obtido directamente com o Android Studio, copiar ou mover o ficheiro do ícone para esta [pasta](https://github.com/migueldgoncalves/viajar_android_app/tree/master/app/src/main/res/mipmap-mdpi). Caso tenha sido, saltar para o passo 3.
3. Abrir o [Translations Editor](https://developer.android.com/studio/write/translations-editor).
4. Acrescentar uma string para o novo meio de transporte e para as respectivas traduções. Esta vai ser a string que irá ser mostrada ao se abrir a aplicação.
5. Ir para o [ficheiro](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/res/menu/bottom_navigation_menu_gps.xml) das opções do BottomNavigationMenu do separador do GPS.
6. Acrescentar um item para o novo meio de transporte. A ordem importa: o elemento mais acima aparecerá mais à esquerda quando estiver visível, e vice-versa.
   * O ícone do item estará na pasta `drawable` se no passo 1 foi obtido directamente com o Android Studio, caso contrário estará na pasta `mipmap`.
7. Ir para a [Travel Activity](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/java/com/viajar/viajar/TravelActivity.java).
8. Nas constantes relacionadas com meios de transporte, acrescentar uma constante para o novo meio de transporte. A constante deve ser igual ao nome do meio de transporte como estiver guardado na base de dados.
    * Por exemplo, a string para o comboio de alta velocidade é `Comboio de Alta Velocidade`, igual ao que surge na base de dados.
9. No método `onCreate`, em `locationButtons.setOnNavigationItemSelectedListener`, acrescentar um else if para o novo meio de transporte.
10. No método `updateUI`, no código destinado a mostrar apenas botões de meios de transporte acessíveis a partir do local actual, acrescentar as linhas correspondentes ao novo meio de transporte.
11. (Opcional) Na classe `MapPageFragment`, no método `desenharMapa`, no if destinado a meios de transporte a evitar desenhar no mapa, acrescentar o novo meio de transporte se desejado.
12. (Opcional) Na classe `DestinationsCustomView`, acrescentar código correspondente à cor ou cores do novo meio de transporte tanto ao se traçarem as ligações no mapa geral como nos destinos ao mostrar. Por omissão as ligações serão desenhadas a negro e os destinos serão mostrados com fundo cinzento. As partes do código de interesse são:
    * As constantes relacionadas com cores;
    * O método `addDestinationsInfo`;
    * O método `getColorByRouteName`;
    * Pode ser de interesse criar um método para indicar se uma ligação corresponde ao novo meio de transporte.

A aplicação suporta agora o novo meio de transporte.

Boa viagem!