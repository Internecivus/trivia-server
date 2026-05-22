# TriviaServer

## TL;DR
* **Web sučelje se može vidjeti [ovdje](http://18.220.145.50:8080/public/login) (testno korisničko ime i lozinka su `trivia` i `trivia`).**

* **Gotova klijentska aplikacija (Windows, Mac, JAR) može se preuzeti [ovdje](https://drive.google.com/open?id=1T8gWPx-VExwQIaaZ3CYgZUo1xAIyfBu7).**

* **API sučelje se može vidjeti [ovdje](http://18.220.145.50:8080/api) (testno korisničko ime i lozinka su `trivia` i `trivia`).**

    Neki primjeri upita su:
    * /api/questions
    * /api/questions;category=History?random=true&size=5
    * /api/questions?page=1&size=10&sortField=id&sortOrder=desc&search=Java
    * /api/categories

## Opis
Ovo je prvi dio projekta za Objektno Programiranje 2 i Uvod u XML programiranje.
Sadrži server Trivia platforme s kojim komunicira klijentska aplikacija. Organiziran je u .EAR u kojem se nalaze 3 modula i jedna zajednička biblioteka.

Server je namijenjen pokretanju na Wildfly 12.0.0.Final aplikacijskom serveru u Java EE 8 preview modu.

Klijentska aplikacija dostupna je na [GitHubu](https://github.com/Internecivus/TriviaClient).

## Upute
1. Downloadajte i instalirajte [Java JDK 10](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)
(pazite da možda IDE koji koristite nema manju bundelanu verziju, u suprotnom morate promijeniti verziju ili Maven zastavice).

1. Downloadajte i instalirajte [Wildfly 12.0.0.Final](http://wildfly.org).

2. Downloadajte i "instalirajte" [Maven](https://maven.apache.org) (opcionalno možete koristiti IDE koji ima integraciju s Mavenom kao što je IntelliJ).

3. Downloadajte i intalirajte [MySQL](https://www.mysql.com).

4. U WILDFLY_FOLDER/standalone/configuration/standalone.xml:
    
    * dodajte `<default-security-domain value="jaspitest"/>` pod `<subsystem xmlns="urn:jboss:domain:ejb3:5.0">` da omogućite EJBContext sigurnosne provjere.
    
    * dodajte `<location name="/images" handler="image-content"/>`
    unutar `<host name="default-host" alias="localhost">` pod `<subsystem xmlns="urn:jboss:domain:undertow:5.0">`
    i
    * dodajte `<file name="image-content" path="${jboss.home.dir}/standalone/data/images"/>`
    unutar `<handlers>` pod `<subsystem xmlns="urn:jboss:domain:undertow:5.0">`
    da omogućite pristupanju slikama putem `/images`. Stvorite direktorij `images` u `WILDFLY_FOLDER/standalone/data`. *Napomena: u produkciji se datoteke ne bi trebale spašavati u folder aplikacijskog servera!*.
    
    * dodajte vlastiti datasource po sljedećem principu:
        ```xml
      <datasource jndi-name="java:jboss/datasources/trivia_db_remote" pool-name="trivia_db_remote">
            <connection-url>jdbc:mysql://localhost:3306/BAZA_IME</connection-url>
            <driver>mysql</driver>
            <pool>
                <min-pool-size>10</min-pool-size>
                <max-pool-size>20</max-pool-size>
                <prefill>true</prefill>
            </pool>
            <security>
                <user-name>USERNAME</user-name>
                <password>PASSWORD</password>
            </security>
      </datasource>
        ```
    pod `<datasources>` pod `<subsystem xmlns="urn:jboss:domain:datasources:5.0">`, s time da morate zamijeniti `BAZA_IME`, `USERNAME` i `PASSWORD` s vašim podacima. 
    *Napomena: U `persistence.xml` datasource je referenciran sa `trivia_db_remote` jer se na live serveru koristi Amazon RDS!*
    
    * dodajte
    ```xml
    <driver name="mysql" module="com.mysql.jdbc">
        <driver-class>com.mysql.jdbc.Driver</driver-class>
    </driver>
    ```
    pod `<drivers>` pod `<subsystem xmlns="urn:jboss:domain:datasources:5.0">`.
    Također morate dodati i sam [driver](https://dev.mysql.com/downloads/connector/j/5.1.html) pod gore naveden path u Wildfly direktorij modula (ne zaboravite i module.xml!).
                                    

5. S obzirom na to da na vašem lokalnom računalu nema slika koje su referencirane u `TriviaPersistence/src/main/resources/META-INF/sql/data.sql`, morate onemogućiti dodavanje tih SQL podataka (ručnim brisanjem ili putem `persistence.xml`).

5. Pokrenite Wildfly sa `-Dee8.preview.mode=true`.



