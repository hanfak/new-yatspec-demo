- split into separate provider per table/action
- delegate used by usecase, that calls multiple tables under a transaction
    - Used in usecase,  interact with database several times in different places, with different args
    - Some sort of transaction class that rolls back if calls to db/other when fails
    - unit of work pattern https://github.com/mattia-battiston/clean-architecture-example/issues/1#issuecomment-396603327
 