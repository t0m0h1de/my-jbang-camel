from("timer:java?period={{time:1000}}")
    .setBody()
        .simple("Hello Camel from quick")
    .log("${body}");
