example 
- usecase send to queue
- listen to queue and call another usecase
- Create messageListenerfactory in wiring

TODO:
- [LOG_START] [LOG_TYPE=APP] 2020-12-09 18:00:12,082 [ActiveMQ Task-1] INFO (FailoverTransport: 1065) - Successfully connected to nio://localhost:61616 [LOG_END]
- This log is happenning twice. Need only connect once to broker

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
- usecase send to internal queue after requested state
- listen to queue and call another usecase
- When usecase two, has finished, send req or create file or update db to confirm
    - Some cron job to check event is complete
- Create messageListenerfactory in wiring

example 
- dead letter queue
- In usecase randomly throw exception, which then adds it to the DLQ, and resends to same queue which executes the
 same usecas

Other 

- add traceyid