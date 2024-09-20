# Progetto Java - Implementazione personalizzata del comando Unix `wc`

Questo progetto è una versione personale del comando Unix `wc` (word count) basato sulla sfida proposta dal sito [codingchallenges.fyi](https://codingchallenges.fyi/challenges/challenge-wc).

## Descrizione del progetto

Il progetto ha l'obiettivo di realizzare un'applicazione Java che replica il comportamento del comando `wc`, utilizzato per contare linee, parole e caratteri in un file o nello standard input.

### Versioni

Sono state implementate due versioni del progetto:

- **Branch `main`**:
  - In questa versione, ho utilizzato la libreria **Java NIO** per l'accesso ai file e la classe **BufferedReader** per la lettura dallo standard input.
  
- **Branch `CR design pattern`**:
  - In questo branch ho implementato il **design pattern Strategy** per una maggiore flessibilità e modularità nella gestione delle diverse strategie di conteggio.

## Utilizzo

### Prerequisiti

- Oracle Open JDK 17.0.0
- Junit 5.9.1
- Mockito core ed inline 5.00

### Compilazione

Esegui il seguente comando per compilare il progetto:

```bash
javac -d bin src/*.java
