import org.apache.camel.builder.RouteBuilder;

public class route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:data/inbox?noop=true")
            .to("file:data/outbox");
    }
}
