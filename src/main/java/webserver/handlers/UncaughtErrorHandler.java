package webserver.handlers;

import logging.LoggingCategory;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class UncaughtErrorHandler extends ErrorHandler {
  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());


  @Override
  protected void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
    Throwable uncaught = (Throwable) request.getAttribute("javax.servlet.error.exception");
    if (uncaught == null) {
      APPLICATION_LOGGER.error("Fatal error. Uncaught exception was not found.");
    } else {
      APPLICATION_LOGGER.error(format("Uncaught exception: '%s'", uncaught.getMessage()), uncaught);
    }
    writer.append("Technical Failure. Please contact the system administrator.");
  }
}
