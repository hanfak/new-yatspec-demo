package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.sequence.Participants;

import static java.lang.String.format;

public class YatspecConstants {
  private static final String APP = "app";
  public static final Participant APP_PARTICIPANT = Participants.PARTICIPANT.create(APP);

  private static final String CLIENT = "client";
  public static final Participant CLIENT_ACTOR = Participants.ACTOR.create(CLIENT);

  private static final String RESPONSE_FORMAT = "Response from %s to %s";
  public static final String RESPONSE_FROM_APP_TO_CLIENT = format(RESPONSE_FORMAT, APP, CLIENT);
  private static final String REQUEST_FORMAT = "Request from %s to %s";
  public static final String REQUEST_FROM_CLIENT_TO_APP = format(REQUEST_FORMAT, CLIENT, APP);
}
