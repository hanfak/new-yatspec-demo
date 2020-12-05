package webserver.servlets;

public interface Unmarshaller<S, T> {
  T unmarshall(S dataToUnmarshall);
}