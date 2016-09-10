package mojap108.mojap;

/**
 * Created by gollaba on 7/17/16.
 */
public interface Constants {
    public static final String BASE_URL = "https://mojapdev.herokuapp.com";
    public static final String LOGIN_API = "/api/login";
    public static final String INSTALLATION_API = "/api/authId/install";
    public static final String CREATE_ACTIVITY = "/api/activity";
    public static final String GET_ACTIVITY = "/api/activity";
    public static final String POST_RESET = "/api/activity/reset";

    public static final String PHONE_NO = "phoneNo";
    public static final String AUTH_ID = "authId";
    public static final String USER_ID = "userId";
    public static final String DATETIME = "dateTime";
    public static final String BEEDCOUNT = "beedCount";
    public static final String BEADCOUNTFORDAY = "beedCountForDay";
    public static final String GCM_TOKEN = "token";
    public static final int LOGIN_RESPONSE = 1;
    public static final int INSTALLATION_RESPONSE = 1;
    public static final int PROFILE_HEADER_ITEM = 1;
    public static final int PROFILE_ROW_ITEM = 2;
    public static final String GCM_SENDERID = "585318721245";
    public static final  int BEAD_TO_MALA_RATIO = 108;


    public static final String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";

    public static final String InfoText = "<p>&nbsp;Chanting is said to be the simplest spiritual practice of all. It is also said to be the preferred practice of Kaliyug, the time we live in.</p>" +
            " \n" +
            "<p>&nbsp;This method works whether you do it on a train, while waiting for a plane, in a coffee shop, or in any other situation. You don&#8217;t have to quit your job or go to a cave in the Himalayas to practice it. You can do it sitting in the Times Square, if you will.</p>" +
            " \n" +
            "<p>&nbsp;A great old saint by the name of Ramakrishna Paramhamsa described chanting similar to planting of a seed on the roof of a house. And when the conditions are right, the planted seed with the right amount of soil, sun and water would germinate into a small plant and later grow so big that it would bring down the whole house. He compared bringing down of the house with bringing down an illusory world that we build in our imagination.</p>" +
            " \n" +
            "<p>&nbsp;With each bead, you slowly clear the mirror of your heart till one day you are illumined fully.</p>";

    public static final String TOKEN_TYPE = "type";
    public static final String FEEDBACK = "feedback";
    public static final String POST_FEEDBACK = "/api/feedback";

    public final String COACHMARK_SHOWN = "COACHMARK_SHOWN";
    public static final String MANTRA_SET = "MANTRA_SET" ;
    public static final String DEFAULT_MANTRA_TEXT = "ॐ नम: शिवाय्";
}
