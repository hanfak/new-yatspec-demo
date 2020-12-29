package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.sequence.Participants;

import static java.lang.String.format;

public class YatspecConstants {
  private static final String APP = "app";
  public static final Participant APP_PARTICIPANT = Participants.PARTICIPANT.create(APP);

  private static final String CLIENT = "client";
  public static final Participant CLIENT_ACTOR = Participants.ACTOR.create(CLIENT);

  private static final String STAR_WARS_SERVICE = "starWarsService";
  public static final Participant STAR_WARS_SERVICE_PARTICIPANT = Participants.PARTICIPANT.create(STAR_WARS_SERVICE);

  private static final String REQUEST_FORMAT = "Request from %s to %s";
  public static final String REQUEST_FROM_CLIENT_TO_APP = format(REQUEST_FORMAT, CLIENT, APP);
  public static final String REQUEST_FROM_APP_TO_STAR_WARS_SERVICE = format(REQUEST_FORMAT, APP, STAR_WARS_SERVICE);

  private static final String RESPONSE_FORMAT = "Response from %s to %s";
  public static final String RESPONSE_FROM_APP_TO_CLIENT = format(RESPONSE_FORMAT, APP, CLIENT);
  public static final String RESPONSE_FROM_APP_TO_STAR_WARS_SERVICE = format(RESPONSE_FORMAT, STAR_WARS_SERVICE, APP);

}
