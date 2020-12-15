# New Yatspec Demo 

With the change to Java 9, this has led original yatspec (link?) to be incompatible. I have a repo for demoing how to
 use The original yatspec here (link??)

Hence a fork has been created at (link?) by Nick McDowall (an ex colleague). The changes made, has improved yatspec and
 made it even easier to get started, customise and with better integration with testing libraries(assertj, junit5).

Here is a demo of how to use it for different contexts. 

Requirements 

- Java 11+
- Docker

## Examples Contents

- Source files 
    - Clean architecture
    - No framwork

- Http request and response 
    - use of @Notes, to add comments for reader
    - Custom Assertj assertions, create fluent thens and readable assertions
    - Used custom Assertj assertions, for fluent readable use in test 
    - Custom Renderers for html output (tabs are rendered correctly), for rendering request & response objects
    - Use of participents for sequence diagrams 
    - Use of testState and storing captured inputs(req and resp) in logs (a map) in the when and extracting during thens
    - Use of builder pattern to create fluent whens
    - Use of inheritence to hide common methods, fields etc
- Database sql
    - Flywaydb, to create schema
    - Postgresql
- Database interactions 
    - source 
        - Use JOOQ library to handle sql
        - Use hikariCP for database connections
    - Test
        - Render database contents before and after givens priming in html output under captured inputs
        - Render database contents after test execution in html output under captured inputs
        - Rendering to html table
        - Asserting on database directly via jooq
        - Primed one and multiple tables in db
        - Use of interesting givens, to grab data from it and use it elsewhere
- File service 
    - Source
        - Use of IO to read/write and replace words
        - Use of internal async processing using executor service to run a runnable 
    - Test
        - ??
- Use of Queues
    - Source - Using jms/activemq 
        - setup
            - Queue listener
            - Message sender to queue
            - wiring
        - Examples 
            - http req in, sends message to queue, listener grabs message from queue and process
    - Testing 
        - ???
    
## Instructions 

For app to work, will need the database and broker to be up 

For acceptance tests to work will need database to be up

### Docker Database setup

* mkdir -p $HOME/docker/volumes/postgres
* docker run --rm --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data  postgres:11
* docker exec -it postgres bash
* psql -h localhost -U postgres -d postgres 
* psql -h localhost -U postgres -d postgres -W
 
 create database "starwarslocal";
 -- conntect to db
 \c "starwarslocal"
 --show tables
 \dt
 -- show table with schema
 \dt records.<name of table>
 -- show schema
 \d+ <name of table>
 
 ### ActiveMQ Docker Setup
 
* docker run -d --name='activemq' -it --rm -e 'ACTIVEMQ_CONFIG_MINMEMORY=256' -e 'ACTIVEMQ_CONFIG_MAXMEMORY=512'  -v
 /data/activemq:/data  -v /var/log/activemq:/var/log/activemq -p 8161:8161 -p 61616:61616  --network=host webcenter/activemq:latest
* To view activemq console, to see queues, processed messages etc:
    * http://127.0.0.1:8161/admin/
    * admin:admin
* https://activemq.apache.org/getting-started
 
## Uses

 Access: 
 GET http://localhost:2222/usecase/Luke%20Skywalker
 
 Yatspec reports: 
 target/surefire-reports/yatspec
 
## Notes

- Run one test class at a time. All test run will take time, have not set hikari properly 
- 