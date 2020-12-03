package fileservice;

@FunctionalInterface
public interface FileReader<S,T> {
    T readFile(S s);
}
