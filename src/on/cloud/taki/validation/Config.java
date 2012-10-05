package on.cloud.taki.validation;

import java.util.Arrays;
import java.util.List;

/** 
 * Config.java maps the available data to be used in Taki Game 
 * */
public class Config {

	public static final List<String> TYPE = Arrays.asList(
			"ONE" 				/** 0   */,				"TWO" 			/** 1   */,		
			"THREE" 			/** 2   */,				"FOUR"			/** 3   */,
			"FIVE" 				/** 4   */,				"SIX"			/** 5   */,
			"SEVEN"				/** 6   */,				"EIGHT"			/** 7   */,
			"NINE"				/** 8   */,				"+2"			/** 9   */,
			"PLUS"				/** 10  */,				"STOP"			/** 11  */,
			"SWITCH DIRECTION"	/** 12  */,				"CHANGE COLOR"	/** 13  */,
			"SWITCH HANDS"		/** 14  */,				"SUPER TAKI"	/** 15  */,
			"TAKI"				/** 16  */,				"KING"			/** 17 	*/
			);
	
	public static final List<String> COLOR = Arrays.asList(
			"BLUE"		/** 0 */,
			"GREEN"		/** 1 */,
			"YELLOW"	/** 2 */,
			"RED"		/** 3 */,
			"IRRELEVANT"/** 4 */
			);
	
	/**
	 * Currently not in use
	 */
	public static final List<String> LOCATION = Arrays.asList(
			"OPEN DECK"		/** 0 */,
			"CLOSE DECK"	/** 1 */,
			"PLAYER1"		/** 2 */,
			"PLAYER2"		/** 3 */,
			"PLAYER3"		/** 4 */,
			"PLAYER4"		/** 5 */
			);

}
