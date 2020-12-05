package webserver.servlets.generateResponseLetter;

import async.AsyncProcessor;
import usecases.generateresponseletter.LetterCreator;
import usecases.generateresponseletter.LetterCreator.UserLetterData;
import webserver.servlets.Unmarshaller;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static usecases.generateresponseletter.LetterCreator.UserLetterData.userLetterData;

public class GenerateResponseLetterUseCaseServlet extends HttpServlet {

  private final Unmarshaller<ServletInputStream, ReadFileUpdaterDTO> unmarshaller;
  private final LetterCreator letterCreator;
  private final AsyncProcessor asyncProcessor;

  public GenerateResponseLetterUseCaseServlet(Unmarshaller<ServletInputStream, ReadFileUpdaterDTO> unmarshaller,
                                              LetterCreator letterCreator,
                                              AsyncProcessor asyncProcessor) {
    this.unmarshaller = unmarshaller;
    this.letterCreator = letterCreator;
    this.asyncProcessor = asyncProcessor;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Extreme for this example to change object used in this layer to object used in inner layer, when they are equal, lot of duplication
    // But I dont want the jackson logic to be passed to inner layer, it should stay in outer layer. And helps with SRP
    // its a trade off
    ReadFileUpdaterDTO dto = unmarshaller.unmarshall(req.getInputStream());
    UserLetterData userLetterData = userLetterData(dto.getName(), dto.getQueryDetails(), dto.getDate());

    asyncProcessor.process(() -> letterCreator.createLetter(userLetterData));

    // This could be extracted to delegate which takes care of creating http response, ie creating json,set status
    resp.getWriter().print("generating file");
  }
}
