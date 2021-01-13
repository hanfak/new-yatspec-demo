package testinfrastructure;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class YatspecLoggerCapturer {
  private final TestState testState;
  private final OutputCapture outputCapture;

  public YatspecLoggerCapturer(TestState testState) {
    this.testState = testState;
    this.outputCapture = new OutputCapture();
  }

  public void beginCapturingOutput() {
    outputCapture.captureOutput();
  }

  public void captureOutputToYatspec() {
    try {
      testState.log("logs", outputCapture.toString());
    } finally {
      outputCapture.releaseOutput();
    }
  }

  static class OutputCapture {

    private CaptureOutputStream captureOut;
    private CaptureOutputStream captureErr;
    private ByteArrayOutputStream copy;

    void captureOutput() {
      this.copy = new ByteArrayOutputStream();
      this.captureOut = new CaptureOutputStream(System.out, this.copy);
      System.setOut(new PrintStream(this.captureOut));
      this.captureErr = new CaptureOutputStream(System.err, this.copy);
      System.setErr(new PrintStream(this.captureErr));
    }

    void releaseOutput() {
      System.setOut(this.captureOut.getOriginal());
      System.setErr(this.captureErr.getOriginal());
      this.copy = null;
    }

    private void flush() {
      try {
        this.captureOut.flush();
        this.captureErr.flush();
      } catch (IOException e) {
        throw new IllegalStateException("something went wrong capturing writing to from stream", e);
      }
    }

    @Override
    public String toString() {
      flush();
      return this.copy.toString();
    }

    private static class CaptureOutputStream extends OutputStream {

      private final PrintStream original;
      private final OutputStream copy;

      CaptureOutputStream(PrintStream original, OutputStream copy) {
        this.original = original;
        this.copy = copy;
      }

      PrintStream getOriginal() {
        return this.original;
      }

      @Override
      public void write(int b) throws IOException {
        this.copy.write(b);
        this.original.write(b);
        this.original.flush();
      }

      @Override
      public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
        this.copy.write(b, off, len);
        this.original.write(b, off, len);
      }

      @Override
      public void flush() throws IOException {
        this.copy.flush();
        this.original.flush();
      }
    }
  }
}
