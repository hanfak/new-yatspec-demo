# New Yatspec Demo 

With the change to Java 9, this has led original yatspec (link?) to be incompatible. I have a repo for demoing how to
 use The original yatspec here (link??)

Hence a fork has been created at (link?) by Nick McDowall (an ex colleague). The changes made, has improved yatspec and
 made it even easier to get started, customised and better integration with testing libraries(assertj, junit5).

Here is a demo of how to use it for different contexts. 

## Examples Contents

- Http request and response 
    - use of @Notes, to add comments for reader
    - Custom Assertj assertions, create fluent thens and readable assertions
    - Custom Renderers for html output (tabs are rendered correctly), for rendering request & response objects
    - Use of participents for sequence diagrams 
    - Use of testState and storing captured inputs(req and resp) in logs (a map) in the when and extracting during thens
    - Use of builder pattern to create fluent whens
    - Use of inheritence to hide common methods, fields etc
## Instructions 

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
 -- show schema
 \d+ <name of table>
 
 
********
 Access: 
 http://localhost:2222/usecase/Luke%20Skywalker
 
 Yatspec reports: 
 target/surefire-reports/yatspec
 
## Notes

- Run one test class at a time. All test run will take time, have not set hikari properly
- 