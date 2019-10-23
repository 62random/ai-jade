# FirePrevention-AI1920

# Evolução Temporal do Ambiente e Estratégia Aplicada- 1ª Fase


Nota: Definimos o raio de ação como o número de células a que um fogo pode distar de um combatente para que este o consiga combater (i.e. ter combustível suficiente para lá chegar e reabastecer).

1. Incendiário comunica à Central que ateou um fogo
2. Central considera agentes disponíveis (e cujo raio de ação envolva o incêndio) para apagar o fogo 
  2.1. Se o fogo for pouco extenso:
    2.1.1. Envia-se o combatente mais rápido no seu raio de ação 
  2.2. Se o fogo for extenso: 
    2.2.1. Dar prioridade à capacidade de água do combatente a enviar, no sentido de tentar apagar o máximo de células
3. Central comunica com o agente que escolheu e destaca-o para apagar o fogo 
4. Agente cumpre a ordem e decide o que fazer a seguir:
  4.1. Enquanto houver um incêndio numa célula adjacente, e caso o agente disponha dos recursos mínimos (água para o apagar e combustível para chegar ao posto de abastecimento mais próximo), o agente desloca-se para a célula em questão e apaga o incêndio;
  4.2. Caso não haja nenhum incêndio numa célula adjacente, o agente comunica com a central enviando a sua posição atual e a sua quantidade de recursos disponíveis e esta comunicação serve para determinar a sua próxima ação.
5. Enquanto houver incêndios ativos, próxima ação a realizar após agente apagar incêndio:
  5.1. se há incêndios que o agente possa apagar (verificação de recursos), volta ao passo 4;
  5.2. se não houver, manda o agente abastecer os recursos em falta.
6. Se não houver incêndios ativos, central manda combatentes que estão fora da central reabastecer os recursos que têm em falta.
 
=> Política de Reabastecimento:

1. Encontrar postos de abastecimento de combustível ao seu alcance:
  1.1. Caso ele precise de água:
    1.1.1. verificar se há algum caminho para algum deles que passe por um posto de abastecimento de água:
      => se houver, opta pelo caminho mais curto;
      => se não houver, vai para o posto de abastecimento de combustível mais próximo de um posto de abastecimento de água;
  1.2. Caso ele não precise de água:
    1.2.1. escolher o posto de abastecimento de combustível a que possa chegar apagando mais incêndios pelo caminho.
