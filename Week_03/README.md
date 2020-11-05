- #### netty-gateway
启动参数：
```
java -jar -Xmx256m -Xms256m xxx.jar
```

- #### netty-httpserver
基于Netty实现的简单httpserver, 启动时自动注册到netty-gateway, 启动参数：
```
java -jar -Dhttpserver.port=8801 -Xmx256m -Xms256m xxx.jar
```
通过修改-Dhttpserver.port=xxxx可开启多个http服务

- #### 压测
  - ##### 直接请求netty-httpserver
  ```
  ab -c 10 -n 50000 -k http://127.0.0.1:8801/
  This is ApacheBench, Version 2.3 <$Revision: 1879490 $>
  Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
  Licensed to The Apache Software Foundation, http://www.apache.org/

  Benchmarking 127.0.0.1 (be patient)
  Completed 5000 requests
  Completed 10000 requests
  Completed 15000 requests
  Completed 20000 requests
  Completed 25000 requests
  Completed 30000 requests
  Completed 35000 requests
  Completed 40000 requests
  Completed 45000 requests
  Completed 50000 requests
  Finished 50000 requests


  Server Software:
  Server Hostname:        127.0.0.1
  Server Port:            8801

  Document Path:          /
  Document Length:        11 bytes

  Concurrency Level:      10
  Time taken for tests:   0.395 seconds
  Complete requests:      50000
  Failed requests:        0
  Keep-Alive requests:    50000
  Total transferred:      5000000 bytes
  HTML transferred:       550000 bytes
  Requests per second:    126552.80 [#/sec] (mean)
  Time per request:       0.079 [ms] (mean)
  Time per request:       0.008 [ms] (mean, across all concurrent requests)
  Transfer rate:          12358.67 [Kbytes/sec] received

  Connection Times (ms)
                min  mean[+/-sd] median   max
  Connect:        0    0   0.0      0       1
  Processing:     0    0   0.3      0       5
  Waiting:        0    0   0.3      0       5
  Total:          0    0   0.3      0       5

  Percentage of the requests served within a certain time (ms)
    50%      0
    66%      0
    75%      0
    80%      0
    90%      0
    95%      1
    98%      1
    99%      1
   100%      5 (longest request)
  ```
  - ##### 通过gateway请求netty-httpserver
  ```
  ab -c 10 -n 50000 -k http://127.0.0.1:8888/
  This is ApacheBench, Version 2.3 <$Revision: 1879490 $>
  Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
  Licensed to The Apache Software Foundation, http://www.apache.org/

  Benchmarking 127.0.0.1 (be patient)
  Completed 5000 requests
  Completed 10000 requests
  Completed 15000 requests
  Completed 20000 requests
  Completed 25000 requests
  Completed 30000 requests
  Completed 35000 requests
  Completed 40000 requests
  Completed 45000 requests
  Completed 50000 requests
  Finished 50000 requests


  Server Software:
  Server Hostname:        127.0.0.1
  Server Port:            8888

  Document Path:          /
  Document Length:        11 bytes

  Concurrency Level:      10
  Time taken for tests:   2.019 seconds
  Complete requests:      50000
  Failed requests:        0
  Keep-Alive requests:    50000
  Total transferred:      5000000 bytes
  HTML transferred:       550000 bytes
  Requests per second:    24763.35 [#/sec] (mean)
  Time per request:       0.404 [ms] (mean)
  Time per request:       0.040 [ms] (mean, across all concurrent requests)
  Transfer rate:          2418.30 [Kbytes/sec] received

  Connection Times (ms)
                min  mean[+/-sd] median   max
  Connect:        0    0   0.0      0       1
  Processing:     0    0   0.6      0       9
  Waiting:        0    0   0.5      0       9
  Total:          0    0   0.6      0       9

  Percentage of the requests served within a certain time (ms)
    50%      0
    66%      1
    75%      1
    80%      1
    90%      1
    95%      1
    98%      2
    99%      2
   100%      9 (longest request)
  ```
