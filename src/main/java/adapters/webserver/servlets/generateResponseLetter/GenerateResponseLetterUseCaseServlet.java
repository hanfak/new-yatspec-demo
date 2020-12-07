package adapters.webserver.servlets.generateResponseLetter;

import adapters.async.AsyncProcessor;
import adapters.webserver.servlets.Unmarshaller;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort.GenerateResponseLetterCommand;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static core.usecases.ports.incoming.GenerateResponseLetterUseCasePort.GenerateResponseLetterCommand.userLetterData;


public class GenerateResponseLetterUseCaseServlet extends HttpServlet {

  private final Unmarshaller<ServletInputStream, ReadFileUpdaterDTO> unmarshaller;
  private final GenerateResponseLetterUseCasePort generateResponseLetterUseCasePort;
  private final AsyncProcessor asyncProcessor;

  public GenerateResponseLetterUseCaseServlet(Unmarshaller<ServletInputStream, ReadFileUpdaterDTO> unmarshaller,
                                              GenerateResponseLetterUseCasePort generateResponseLetterUseCasePort,
                                              AsyncProcessor asyncProcessor) {
    this.unmarshaller = unmarshaller;
    this.generateResponseLetterUseCasePort = generateResponseLetterUseCasePort;
    this.asyncProcessor = asyncProcessor;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Extreme for this example to change object used in this layer to object used in inner layer, when they are equal, lot of duplication
    // But I dont want the jackson logic to be passed to inner layer, it should stay in outer layer. And helps with SRP
    // its a trade off
    ReadFileUpdaterDTO dto = unmarshaller.unmarshall(req.getInputStream());
    GenerateResponseLetterCommand command = userLetterData(dto.getName(), dto.getQueryDetails(), dto.getDate());

    // Debatable whether the async processing should be in usecase or here.
    // In prod, this will send to a external queue for processing with reliability
    asyncProcessor.process(() -> generateResponseLetterUseCasePort.createLetter(command));

    // This could be extracted to delegate which takes care of creating http response, ie creating json,set status
    resp.getWriter().print("generating file");
  }
}
