# What is it?

A library to trace execution of methods per each [Spring](https://spring.io/) Servlet's HTTP request.


# Why would I want to use it?

If you need a convenient way to trace which methods were executed per HTTP request - in what order, 
what were the method arguments and execution results.

You can filter the methods you are interested in by specifying 
a root package - for example, to log only application-specific methods and ignore framework ones.

One use case could be debugging and code analysis. Another big one - monitoring purposes.

# Quickstart
### Add library to your Spring Boot app

Gradle (Groovy)
```groovy
runtimeOnly "com.beautrace:beautrace-spring-servlet:1.0.2"
```

Gradle (Kotlin)
```kotlin
runtimeOnly("com.beautrace:beautrace-spring-servlet:1.0.2")
```

Maven
```xml
<dependency>
    <groupId>com.beautrace</groupId>
    <artifactId>beautrace-spring-servlet</artifactId>
    <version>1.0.2</version>
    <scope>runtime</scope>
</dependency>
```

### Add to your `application.properties`:
```properties
# Root package for tracing. Execution of methods from this package will be traced
# Setting this property is required to enable Beautrace
beautrace.root-package=com.beautrace.spring.servlet.app
# Path to a file to store traces. Ensure that the app has writing access to this directory
beautrace.output-file=${user.home}/beautrace.json
```

### After your app did some request processing, go check the output file

File contents look like the following:
```jsonc
...
{"request":"GET /api/exception?argOne=1234&argTwo=wow%20string","methodCalls":[...]}
{"request":"GET /api/exception?argOne=1234&argTwo=wow%20string","methodCalls":[...]}
...
```

Each line represents tracing for a different request. Each line is a json object. You can now pick your favorite json parser
and explore tracing for a request of your interest.

# Example projects

You can check the library in action by exploring demo projects:
- Spring Boot app on Kotlin / Gradle: https://github.com/beautrace/beautrace-spring-servlet-app

# How does the library work?

The library intercepts execution of methods from the package you specify and logs them. Once the http 
request is handled, this data is written to a file at a location you specify.

Here is how example output looks like for some http request:
```jsonc
{
  // Http request details 
  "request": "GET /api/mock?argOne=1234&argTwo=wow%20string",
  // List of method calls for that request from com.beautrace.spring.servlet.app package 
  "methodCalls": [
    // 
      {
        "name":"public boolean com.beautrace.spring.servlet.app.api.MockApiService.doApiWork(java.lang.Integer,java.lang.String)",
        // List of method arguments as pairs: 
        // first value in pair represents argument name, second - argument value
        "arguments":[
          {
            "first":"java.lang.Integer argOne", 
            "second":1234
          }, 
          {
            "first":"java.lang.String argTwo", 
            "second":"wow string"
          }
        ],
        // If compiler did not retain method argument names, then argument values will be logged
        // In our case compiler knew argument names, and therefore we logged them in 'arguments' node above
        "rawArguments":[],
        // What method returned
        "result":true,
        // Were there any unhandled exceptions during method execution?
        "exception":null
      },
    {
      "name":"public java.lang.String com.beautrace.spring.servlet.app.service.MockService.doWork()",
      "arguments":[],
      "rawArguments":[],
      "result":"work", 
      "exception":null
    }]
}
```

Here is a snippet of how unhandled exceptions are logged:
```jsonc
{
  ...
  
  "result": null,
  // Unhandled exception was thrown during the method execution
  // Beautrace will log entire stack trace of the exception
  "exception": {
    "localizedMessage": "exception!",
    "cause": null,
    "stackTrace": [
      {
        "classLoaderName": "app",
        "moduleName": null,
        "moduleVersion": null,
        "methodName": "doWorkWithException",
        "fileName": "MockApiService.kt",
        "lineNumber": 30,
        "className": "com.beautrace.spring.servlet.app.api.MockApiService",
        "nativeMethod": false
      },
      {
        "classLoaderName": "app",
        "moduleName": null,
        "moduleVersion": null,
        "methodName": "invoke",
        "fileName": "<generated>",
        "lineNumber": -1,
        "className": "com.beautrace.spring.servlet.app.api.MockApiService$$FastClassBySpringCGLIB$$defc0d23",
        "nativeMethod": false
      },
      ... // rest of the stack trace omitted for brevity here
      {
        "classLoaderName": null,
        "moduleName": "java.base",
        "moduleVersion": "11.0.15",
        "methodName": "run",
        "fileName": "Thread.java",
        "lineNumber": 829,
        "className": "java.lang.Thread",
        "nativeMethod": false
      }
    ]
  },
  
  ...
}
```

# FAQ

### What projects this library is compatible with?

It supports Spring Boot apps on Java/ Kotlin @ Java 8 and higher.

### The library only prints method argument values. How do I make it print argument names too?

You should configure your compiler to retain method argument names. Here is how.

Java compiler: add `-parameters` flag

Kotlin compiler: add `-java-parameters`flag
