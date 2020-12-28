package adapters.incoming.webserver.servlets;

public interface Marshaller<S, T> {
  T marshall(S dataToMarshall);
}
