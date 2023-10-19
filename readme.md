Once upon a time a Japanese Legend said **DatteBayo** (Believe It) 

So Welcome....

Basically this project is a template of spring security, I have several plans to develop it so wait for it :wink 

How to execute :-
1. Do mvn clean install
2. docker-compose up --build 
3. There is no flyway as of now so run the scripts in mysql console
4. Then start the application => Happy Hacking as basic React app says !!

Api's and their behaviour :-

1. register api is not authenticated so simply enter details and register your user
2. login api is a get call with basic auth as headers on successful response you will receive header as part of response headers.
3. info api provides the information of user so pass the jwt token you receive as part of login api response as a header with name as Authorization (you cannot see the other users details using your jwt it is method level secured application :wink).