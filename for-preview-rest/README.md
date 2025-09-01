# for-preview-rest

### keep-alive 확인
연달아 호출하는 경우에는 connection을 항상 새로 맺는다.
``` terminal
curl -v -w "\n" -X 'GET' \
  'http://localhost:8080/api/ping/1' \
  -H 'accept: */*'
Note: Unnecessary use of -X or --request, GET is already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/ping/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 8
< Date: Mon, 01 Sep 2025 13:55:15 GMT
< 
* Connection #0 to host localhost left intact
pong : 2

curl -v -w "\n" -X 'GET' \
  'http://localhost:8080/api/ping/1' \
  -H 'accept: */*'
Note: Unnecessary use of -X or --request, GET is already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/ping/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 8
< Date: Mon, 01 Sep 2025 13:55:17 GMT
< 
* Connection #0 to host localhost left intact
pong : 2
```

한번에 호출하는 경우 connection을 재사용한다.
``` terminal
curl -v -w "\n" -X 'GET' \
  'http://localhost:8080/api/ping/1' 'http://localhost:8080/api/ping/2' \
  -H 'accept: */*'
Note: Unnecessary use of -X or --request, GET is already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/ping/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 8
< Date: Mon, 01 Sep 2025 13:57:34 GMT
< 
* Connection #0 to host localhost left intact
pong : 2
Note: Unnecessary use of -X or --request, GET is already inferred.
* Found bundle for host: 0x600001ac42a0 [serially]
* Can not multiplex, even if we wanted to
* Re-using existing connection with host localhost
> GET /api/ping/2 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 8
< Date: Mon, 01 Sep 2025 13:57:34 GMT
< 
* Connection #0 to host localhost left intact
pong : 3
```

톰캣이 재사용할 수 있는 최대 카운트를 1로 제한하면 1개의 요청이 들어왔을 때 톰캣이 1개의 요청을 처리한 후 연결을 끊는다.
`server.tomcat.max-keep-alive-requests=1`
``` terminal
curl -v -w "\n" -X 'GET' \
  'http://localhost:8080/api/ping/1' 'http://localhost:8080/api/ping/2' \
  -H 'accept: */*'
Note: Unnecessary use of -X or --request, GET is already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/ping/1 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 8
< Date: Mon, 01 Sep 2025 14:01:20 GMT
< Connection: close
< 
* Closing connection
pong : 2
Note: Unnecessary use of -X or --request, GET is already inferred.
* Hostname localhost was found in DNS cache
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
> GET /api/ping/2 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.7.1
> accept: */*
> 
* Request completely sent off
< HTTP/1.1 200 
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 8
< Date: Mon, 01 Sep 2025 14:01:20 GMT
< Connection: close
< 
* Closing connection
pong : 3
```
