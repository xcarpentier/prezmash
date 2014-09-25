==============================================
PrezMash : France 2012 presidential with Play!
==============================================

Vote for your favorites candidats
---------------------------------

A sample presidential FaceMash.

Technology : Play 2 (Java), jQuery, MySql and Twitter4j

More information : http://xavier-carpentier.fr/developments/prezmash/

Why it does it this way, benefits and drawbacks
-----------------------------------------------

Using Play! because it is more concise.

Architecture and design
-----------------------

MVC paradigm via Play!, force very clean developments.

Using Ajax for data exchanges.

Compile, deployed and used 
--------------------------

This project was deployed on Amazon EC2.

To test it you need to install Play! and configure a MySql database. (or other)

To configure database change on conf/application.conf file :

db.default.url="jdbc:mysql://localhost:8889/prezmash?autoReconnect=true"

autoReconnect=true because it's deployed on Amazon and some weird things happened, like disconnection.

Licence
-------

You can copy, fork and what ever you want with my code without any feedback.
But please go to my website : http://xavier-carpentier.fr
And if you want to follow me on twitter : @xcapetir !
