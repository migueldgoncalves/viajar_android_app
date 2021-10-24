## Número da versão da aplicação

Os números das versões deste projecto seguem o modelo definido pelo [Semantic Versioning](https://semver.org/), com adaptações. Assim, as versões incluídas no código Android têm o formato `X.Y.Z`.

* `X` corresponde à major version 

  * `0` foi usado durante o desenvolvimento inicial
  * `1` é usado desde que se conseguiu uma aplicação funcional
  * Actualmente não há planos para se desenvolver uma nova major version

* `Y` corresponde à minor version
  * É incrementada sempre que se acrescenta ou se altera funcionalidade visível pelo utilizador, tal como acrescentar um ecrã ou elementos a um ecrã já existente

* `Z` corresponde à patch version. É colocada a 0 sempre que a minor version é incrementada. A patch version é incrementada nas seguintes situações:
  * Correcção de bugs
  * Criação de novos meios de transporte
  * Actualização dos dados da base de dados
  * Mudança na estrutura da base de dados

### Alterar o número da versão do projecto

Dentro deste projecto, o número da versão está presente em 2 sítios:

* No ficheiro [strings.xml](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/src/main/res/values/strings.xml). Uma forma de editar o ficheiro é através do Translations Editor do Android Studio
  * Cada componente do número da versão aparece numa linha diferente
* No ficheiro [build.gradle](https://github.com/migueldgoncalves/viajar_android_app/blob/master/app/build.gradle) do módulo Viajar.app
  * O número da versão deve ser escrito por extenso no campo `versionName`, com o formato `X.Y.Z` (sem `v`). Ex: `1.10.0`
  * O campo `versionCode` deve ser incrementado em 1 cada vez que se alterar o número da versão. Ex: A versão `1.10.0` corresponde à version code 28.

Um commit que introduza uma nova versão deve receber uma tag no GitHub, com o formato `vX.Y.Z` (desta vez com um `v` inicial). Ex: `v1.10.0`. A tag deve ter uma mensagem descritiva.