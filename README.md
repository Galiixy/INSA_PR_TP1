# PR_TP1

## Principes architecturaux et protocole de communication
### Architecture


### Communication
1. Lancer le serveur avec un port
2. Client se connecte avec l'IP du serveur + le port + son nom -> lancement du Thread 
3. Client a accès à plusieurs fonctionnalités 
   

### Use Case
1. Ajouter un nom au client
2. Client peut créer un channel 
3. Client peut rejoindre un channel
4. Client peut quitter un channel 
5. Supprimer un channel quand y'a personne 
6. Client peut se déconnecter

### Usage in CLI

#### Globally usage

- Run Server
- Run Client :
  - Setup client name
  - Create a channel : `create`
    - Setup channel name
  - Join a channel : `join`
    - Choose a channel into list
    - Send messages in channel
  - Left a channel : `exit`
    - Send `exit` in channel
  - Left server : `exit`
    - Send `exit` in channel choice pannel

#### Exemple

```
Veuillez saisir votre pseudo pour la session : Mathis
Rejoindre ou créer un channel ? Quitter : exit
create
Nom du channel ?
Channel1
Channel Channel1 crée avec succès
Rejoindre ou créer un channel ? Quitter : exit
create
Nom du channel ?
Channel2
Channel Channel2 crée avec succès
Rejoindre ou créer un channel ? Quitter : exit
join
Liste des channels : 
[Channel1, Channel2]
Choix du channel ?
Channel1
some message here...
exit
Rejoindre ou créer un channel ? Quitter : exit
exit

Process finished with exit code 0
```

### IHM

