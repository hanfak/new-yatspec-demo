package webserver.factories;

import logging.LoggingCategory;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;
import webserver.JettyWebServer;
import webserver.handlers.UncaughtErrorHandler;

import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;
import static logging.LoggingCategory.ACCESS;
import static org.eclipse.jetty.server.CustomRequestLog.EXTENDED_NCSA_FORMAT;
import static org.slf4j.LoggerFactory.getLogger;
import static org.zalando.logbook.DefaultHttpLogWriter.Level.INFO;

public class JettyWebServerFactory {

  private final JettyWebServer jettyWebServer;

  private JettyWebServerFactory(JettyWebServer jettyWebServer) {
    this.jettyWebServer = jettyWebServer;
  }

  public static JettyWebServerFactory jettyWebServer(JettyWebServer jettyWebServer) {
    return new JettyWebServerFactory(jettyWebServer);
  }

  public JettyWebServer create(ServletContextHandler servletContextHandler) {
    jettyWebServer.withRequestLog(createRequestLog());
    jettyWebServer.withBean(new UncaughtErrorHandler()); // TODO monkey patch zalendo
    addLoggingFilter(servletContextHandler);
    jettyWebServer.withHandler(servletContextHandler);
    return jettyWebServer;
  }

  public static void addLoggingFilter(ServletContextHandler servletContextHandler) {
    Logbook logbook = Logbook.builder()
        .formatter(new DefaultHttpLogFormatter())
        .writer(new DefaultHttpLogWriter(getLogger(LoggingCategory.AUDIT.name()), INFO))
        .build();
    FilterHolder filterHolder = new FilterHolder(new LogbookFilter(logbook));
    servletContextHandler.addFilter(filterHolder, "/*", EnumSet.of(REQUEST, ASYNC, ERROR));
  }

  public static CustomRequestLog createRequestLog() {
    Slf4jRequestLogWriter slf4jRequestLogWriter = new Slf4jRequestLogWriter();
    slf4jRequestLogWriter.setLoggerName(ACCESS.name());
    return new CustomRequestLog(slf4jRequestLogWriter, EXTENDED_NCSA_FORMAT);
  }
}
