- Move SpeciesInfoId and SpeciesInfoRecord to source, and use in database code, but have mapping in DataProvider
 interface to create object for usecase without the id fields
- toSTring on SpeciesInfoRecord find better way
- For other objects for tables 
- Maybe use jooq create objects

- ~~Rendering database contents in captured outputs~~
- Tidy up 3.5F
- ~~Separate test, to with use of different givens to run insert database in build (isStoredInTheDatabase()) method
, instead of builder methods~~
- ~~Add interesting givens~~
- ~~Use intersting givens getType~~
- ~~Asserting on database, via teststate using builder~~
- ~~new test UC3: Priming charcater~~
- new test UC3: test all db tables in one then **NEXT**
- new test UC3: prime db with random vlaues, and use interesting given to get them out
- new usecase for database insertions (use jooq returning result, to get row from db on insertion)
- new usecase for database updates
    - new usecase for priming and asserting on multiple tables in one then, for an update with multiple childs
     include intersting givens, and assertion has one then to test both tables in same then builder
- new usecase for database deletions

