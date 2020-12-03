package webserver.servlets;

import async.AsyncProcessor;
import usecases.LetterCreator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GenerateResponseLetterUseCaseServlet extends HttpServlet {
  private final GenerateResponseLetterUnmarshaller unmarshaller;
  private final LetterCreator letterCreator;
  private final AsyncProcessor asyncProcessor;

  public GenerateResponseLetterUseCaseServlet(GenerateResponseLetterUnmarshaller unmarshaller, LetterCreator letterCreator, AsyncProcessor asyncProcessor) {
    this.unmarshaller = unmarshaller;
    this.letterCreator = letterCreator;
    this.asyncProcessor = asyncProcessor;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ReadFileUpdaterDTO dto = unmarshaller.unmarshall(req.getInputStream());
    asyncProcessor.process(() -> letterCreator.createLetter(dto));
    resp.getWriter().print("generating file");
  }
}
