Adidas Challenge Subscription

Fidel C. Garbajosa
30 March 2021

This Java project has been developed in Java 14, MySQL, Maven and Swagger

It's a maven multiproject so three services (Public, Service and Email) are 
created from a root pom project. Public service is unprotected and the other two 
(Subscription and Email) are proteced using Oauth2 with JSon Web Token which it's 
the most used protection system used on professional environments.

There are four operations on the Public service: creation of subscription, read my subscription, 
cancel my subscription and show all subscriptions. Being the Public service unprotected, an email 
and password has to be sent when reading the subscription to identify the user. 

In the first one (creation) the user has to include a password to read his subscription in the future. 
No user is allowed to read subscriptions from someone else except his/hers
 
The Subscription service is the one which does the Database operations and Email is in
charge of sending emails.

The admin user name and password is required to read all subscriptions. It's stored in the database and
the application.properties file

Due to be running out of time it's not done proper testing or docker configuration

Included is a collection data exported from Postman to test the app
