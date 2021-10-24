## Adicionar separadores à Travel Activity

A [Travel Activity](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/java/com/viajar/viajar/TravelActivity.java) é a actividade mais complexa da aplicação. Conta com um conjunto de separadores, que podem ser acedidos tanto por botões no topo do ecrã como deslizando para os lados.

Este tutorial descreve como acrescentar separadores à Travel Activity.

### Requisitos

O [Android Studio](https://developer.android.com/studio) é recomendado para criar um novo separador, e foi o IDE usado para criar este tutorial. Conhecimentos gerais de programação são também recomendados.

Conhecimentos de programação em Java e Android são necessários para posteriormente implementar funcionalidades no novo separador.

### Passos

0. Abrir este projecto com o Android Studio.
1. Obter um ícone que represente o separador. Devem ser imagens com cerca de 50x50 píxeis.
   * Uma possibilidade é pesquisar por [android icons](https://www.google.com/search?q=android+icons&rlz=1C1GCEA_enPT854PT854&oq=android+icons&aqs=chrome.0.69i59.1634j0j7&sourceid=chrome&ie=UTF-8) no Google.
   * Outra possibilidade é adicionar imagens [directamente pelo Android Studio](https://developer.android.com/studio/write/vector-asset-studio#running).
2. Se o ícone não foi obtido directamente com o Android Studio, copiar ou mover o ficheiro do ícone para esta [pasta](https://github.com/migueldgoncalves/viajar_android_app/tree/master/app/src/main/res/mipmap-mdpi). Caso tenha sido, saltar para o passo 3.
3. Ir para a [Travel Activity](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/java/com/viajar/viajar/TravelActivity.java).
4. Incrementar a variável `TAB_NUMBER` em 1.
5. Abaixo do comentário `Fragment classes`, existe um conjunto de classes dedicadas aos separadores. Copiar e colar a classe mais adequada. 
   * Caso o separador tenha um mapa Google Maps integrado, usar como exemplo a classe `GPSPageFragment`.
   * Caso não tenha, usar como exemplo a classe `InfoPageFragment`.
6. Mudar o nome da classe para o nome desejado.
7. Ir para a pasta dos [ficheiros de layout](https://github.com/migueldgoncalves/viajar_android_app/tree/master/app/src/main/res/layout).
8. Duplicar o ficheiro de layout correspondente à classe copiada, mudando o nome do ficheiro para corresponder à nova classe da Travel Activity. Por exemplo, o ficheiro `fragment_travel_gps.xml` corresponde à classe `GPSPageFragment`.
9. No novo ficheiro de layout, alterar o campo `android:id` para um novo ID. Por exemplo, o ID `travelGPSLayout` (onde `travel` corresponde à Travel Activity) corresponde à classe `GPSPageFragment`.
10. Abrir o [ficheiro de layout](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/res/layout/activity_travel.xml) da Travel Activity.
11. Dentro da `TabLayout`, acrescentar um novo `TabItem`. A ordem importa: o elemento mais acima aparecerá mais à esquerda, e vice-versa.
12. Alterar o campo `android:id` para um novo ID à escolha.
13. Alterar o campo `android:icon` para o ícone do novo separador.
14. Regressar à [Travel Activity](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/java/com/viajar/viajar/TravelActivity.java), à nova classe criada.
15. No método `onCreateView`, haverá uma variável `R.layout` que fará referência ao layout da classe que serviu de exemplo. Alterar essa variável para apontar para o novo ficheiro de layout.
16. Na classe `TravelPagerAdapter`, no método `createFragment`, acrescentar um case no switch que corresponda ao novo separador. O número presente no case irá determinar a ordem do separador, sendo que 0 corresponde ao separador mais à esquerda. A ordem deve coincidir com a ordem da `TabLayout` do ficheiro de layout da Travel Activity.

A aplicação deve agora poder ser compilada e executada com sucesso. O novo separador terá o aspecto e pelo menos alguma da funcionalidade do separador que serviu de exemplo, e está pronto para nele se desenvolver a funcionalidade pretendida.