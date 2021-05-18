import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;

public class ReactorSinkTest {

    @Test
    void test1() {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        sink.asFlux().subscribe(s -> System.out.println("1: " + s));
        sink.emitNext("q1", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.asFlux().subscribe(s -> System.out.println("2: " + s));
        sink.emitNext("q2", Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    void test2() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        sink.asFlux().subscribe(s -> System.out.println("1: " + s));
        sink.emitNext("q1", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.asFlux().subscribe(s -> System.out.println("2: " + s));
        sink.emitNext("q2", Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    void test3() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        Disposable s1 = sink.asFlux().subscribe(s -> System.out.println("1: " + s));
        sink.emitNext("q1", Sinks.EmitFailureHandler.FAIL_FAST);
        Disposable s2 = sink.asFlux().subscribe(s -> System.out.println("2: " + s));
        sink.emitNext("q2", Sinks.EmitFailureHandler.FAIL_FAST);
        s1.dispose();
        s2.dispose();
        Disposable s3 = sink.asFlux().subscribe(s -> System.out.println("3: " + s));
        sink.emitNext("q3", Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    void test4() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer(1, false);
        Disposable s1 = sink.asFlux().subscribe(s -> System.out.println("1: " + s));
        sink.emitNext("q1", Sinks.EmitFailureHandler.FAIL_FAST);
        Disposable s2 = sink.asFlux().subscribe(s -> System.out.println("2: " + s));
        sink.emitNext("q2", Sinks.EmitFailureHandler.FAIL_FAST);
        s1.dispose();
        s2.dispose();
        Disposable s3 = sink.asFlux().subscribe(s -> System.out.println("3: " + s));
        sink.emitNext("q3", Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    void test5() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer(1, false);
        Disposable s1 = sink.asFlux().subscribe(s -> System.out.println("1: " + s));
        sink.emitNext("q1", Sinks.EmitFailureHandler.FAIL_FAST);
        Disposable s2 = sink.asFlux().subscribe(s -> System.out.println("2: " + s));
        sink.emitNext("q2", Sinks.EmitFailureHandler.FAIL_FAST);
        s1.dispose();
        s2.dispose();
        Disposable s3 = sink.asFlux().subscribe(s -> System.out.println("3: " + s));
        sink.emitNext("q3", Sinks.EmitFailureHandler.FAIL_FAST);
        s3.dispose();
        sink.emitNext("q4", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q5", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q6", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q7", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q8", Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Test
    void test6() {
        Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();
        Disposable s1 = sink.asFlux().subscribe(s -> System.out.println("1: " + s));
        sink.emitNext("q1", Sinks.EmitFailureHandler.FAIL_FAST);
        Disposable s2 = sink.asFlux().subscribe(s -> System.out.println("2: " + s));
        sink.emitNext("q2", Sinks.EmitFailureHandler.FAIL_FAST);
        s1.dispose();
        s2.dispose();
        sink.emitNext("q22", Sinks.EmitFailureHandler.FAIL_FAST);
        Disposable s3 = sink.asFlux().subscribe(s -> System.out.println("3: " + s));
        sink.emitNext("q3", Sinks.EmitFailureHandler.FAIL_FAST);
        s3.dispose();
        sink.emitNext("q4", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q5", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q6", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q7", Sinks.EmitFailureHandler.FAIL_FAST);
        sink.emitNext("q8", Sinks.EmitFailureHandler.FAIL_FAST);
    }

}
