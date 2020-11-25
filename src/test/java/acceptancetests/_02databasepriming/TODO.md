- ~~Rendering database contents in captured outputs~~
- Tidy up 3.5F
- Separate test, to with use of different givens to run insert database in build (isStoredInTheDatabase()) method
, instead of builder methods
- ~~Add interesting givens~~
- ~~Use intersting givens getType~~
- Asserting on database, via teststate using builder
- Asserting on database, via teststate using fluent assertj assertions (ThenTheResponseVersion2)
- new test UC3: Priming charcater, use random values **NEXT**
- new usecase for database insertions
- new usecase for database updates
    - new usecase for priming and asserting on multiple tables in one then, for an update with multiple childs
     include intersting givens
- new usecase for database deletions
- Use a stub for database
- Move SpeciesInfoId and SpeciesInfoRecord to source, and use in database code, but have mapping in DataProvider
 interface to create object for usecase without the id fields
- split the usecase from the servlets, have inject a httpResponseMarshaller class into servlet
- toSTring on SpeciesInfoRecord find better way
