package core.usecases.ports.outgoing;

@FunctionalInterface
public interface FileReader<S,T> {
    T readFile(S s);
}
