import org.apache.camel.builder.RouteBuilder;

public class hello extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period={{time:1000}}")
            .setBody()
                .simple("Hello Camel from {{you}}")
            .log("${body}");
    }
}
