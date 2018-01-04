# Issue Tracker

**Issue Tracker** is a student experimental project written in Scala Play Framework and Angular.

## Getting Started

To run this project locally you will have to install:

- PostgreSQL
- Angular CLI
- SBT
- Scala

After you have installed all of these you will have to setup database for backend project. First you need to start PostgreSQL service and make database with name issuetracker. Then, you will call ```sbt run 8080``` from backend folder in order to start Play server. Now, you have functional backend application.

To start your frontend application just run ```npm install``` from frontend folder, and after that ```ng serve -o``` to serve angular files on port 4200.

To run on docker, you will first have to build you backend project using ```sbt dist``` and turn off your local PostgreSQL server. Than run ```docker-compose build``` and ```docker-compose up -d``` and you are ready to go.

**Note**: Current urls are set for docker environment. To set them for local use, you will have to change **app.config.ts** file on frontend and ```slick.dbs.default.db.url``` parameter in **application.conf** file on backend.

## Contributors

* **Aleksandar Novaković** [Novke01](https://github.com/Novke01)
* **Milorad Vojnović** [milekuglas](https://github.com/milekuglas)
* **Arsenije Vladisavljev** [arsa94](https://github.com/arsa94)
* **Aleksandar Kahriman** [aleksandar94ns](https://github.com/aleksandar94ns)
