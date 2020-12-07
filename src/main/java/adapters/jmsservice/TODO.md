example 
- usecase send to queue
- listen to queue and call another usecase
- Create messageListenerfactory in wiring

example 
- usecase send to queue, with different instructions
- listen to queue, and depending on instruction sends to different usecases

example 
- Store data in database as events(
    - requested UC1
    - Sent for processing UC1
    - received for processing UC2
    - processed UC2
    - Complete UC2
- usecase send to queue
- listen to queue and call another usecase
- When usecase two, has finished, send req or create file or update db to confirm
    - Some cron job to check event is complete
- Create messageListenerfactory in wiring

example 
- dead letter queue

Other 

- add traceyid