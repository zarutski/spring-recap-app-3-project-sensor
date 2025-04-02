spring-course-recap-measurement-task
==================

Task consists of 2 pars:
1. sensor/measurements-storing systems (https://github.com/zarutski/spring-recap-app-3-project-measurement)
2. client sending data (current repository)

client (interaction wtih measurements system):
--------------------------
After application startup, app will: 

1. register new sensor at the measurments system via REST API
1. send 1000 requests with random data to remote system [POST /measurements/add] using RestTemplate
2. retrive previously saved measurments and print results to the console output [GET /measurements]
