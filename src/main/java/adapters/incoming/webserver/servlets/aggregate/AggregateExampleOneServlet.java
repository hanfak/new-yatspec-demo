package adapters.incoming.webserver.servlets.aggregate;

import adapters.incoming.webserver.servlets.Unmarshaller;
import core.usecases.ports.incoming.AggregateExample1Step1Service;
import core.usecases.ports.incoming.AggregateExample1Step1Service.AggregateCommand;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AggregateExampleOneServlet extends HttpServlet  {
  private final AggregateExample1Step1Service service;
  private final Unmarshaller<ServletInputStream, IncomingAggregateDTO> unmarshaller;

  public AggregateExampleOneServlet(AggregateExample1Step1Service service, Unmarshaller<ServletInputStream, IncomingAggregateDTO> unmarshaller) {
    this.service = service;
    this.unmarshaller = unmarshaller;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    IncomingAggregateDTO incomingAggregateDTO = unmarshaller.unmarshall(req.getInputStream());

    AggregateCommand aggregateCommand = new AggregateCommand(
        incomingAggregateDTO.getAggregateReference(),
        incomingAggregateDTO.getAggregateData());

    service.execute(aggregateCommand);

    // TODO delegate for marshalling json
    resp.getWriter().print("In progress");
  }
}
