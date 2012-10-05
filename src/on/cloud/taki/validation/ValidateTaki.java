package on.cloud.taki.validation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ValidateTaki extends HttpServlet {
	private static final Logger log = Logger.getLogger(ValidateTaki.class.getName());
	private static final long serialVersionUID = 1L;
	
	PrintWriter out;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("GET is not supported. Try POSTing");

    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		Card inPack=null;
		Vector<Card> player_cards=new Vector<Card>();
		
		out = resp.getWriter();
        resp.setContentType("text/plain");
		Map parameters = req.getParameterMap();
		log.info("parameters taken");
		
		String[] tmp;
		String inpack_type,inPack_color;
        // get the inPack card
        tmp= (String[]) parameters.get("inpack_type");  // The keys in the parameter map are of type String. The values in the parameter map are of type String array.
        if(tmp!=null)
        	inpack_type = tmp[0];
        else
        	inpack_type = null;
        tmp= (String[]) parameters.get("inpack_color");
        if(tmp!=null)
        	inPack_color = tmp[0];
        else 
        	inPack_color=null;
        if(inpack_type == null || !Config.TYPE.contains(inpack_type))
        	WrongType("inpack_type");
        else if(inPack_color == null || !Config.COLOR.contains(inPack_color))
        	WrongColor("inpack_color");
        else
        {
        	inPack = new Card(inpack_type,inPack_color);
        }
        log.info("inPack taken");
        
        // at least one player card to validate
        // pc = 'player card'
        String pc_type,pc_color;
        tmp= (String[]) parameters.get("pc1_type");
        if(tmp!=null)
        	pc_type = tmp[0];
        else
        	pc_type=null;
        tmp= (String[]) parameters.get("pc1_color");
        if(tmp!=null)
        	pc_color = tmp[0];
        else
        	pc_color = null;
        if(pc_type == null || !Config.TYPE.contains(pc_type))
        	WrongType("pc1_type");
        else if(pc_color == null || !Config.COLOR.contains(pc_color))
        	WrongColor("pc1_color");
        else
        {
        	player_cards.add(new Card(pc_type,pc_color));
        }
        log.info("pc1 card taken");
        
        
        // get the rest of the player cards
        int ind=2; // index for card no. 2 if exists
        String pcX_type;
        String pcX_color;
        
        while (true)   {
        	log.info("validating player card at index "+ind);
        	tmp= (String[]) parameters.get("pc" + ind + "_type");
        	if(tmp!=null)
        		pcX_type = tmp[0];
        	else
        		pcX_type = null;
        	tmp= (String[]) parameters.get("pc" + ind + "_color");
        	if(tmp!=null)
        		pcX_color = tmp[0];
        	else
        		pcX_color=null;
        	if(!Config.TYPE.contains(pcX_type) && pcX_type != null) {
        		WrongType("pc" + ind + "_type");
        		break;
        	}
            else if(!Config.COLOR.contains(pcX_color) && pcX_color != null) { 
            	WrongColor("pc" + ind + "_color");
            	break;
            }
            else if((pcX_type == null && pcX_color != null) || (pcX_type != null && pcX_color == null) ) {
            	out.write("Missing Parameters for player card NO. " + ind + ". POST both type and color for each Card");
            	out.close();
            }
            else if (pcX_type == null && pcX_color == null)
            	break;
            else {
            	player_cards.add(new Card(pcX_type,pcX_color));
            }
        	ind++;
        }
        
        // get the special card modes from parameters
        log.info("reading special cards..");
        boolean plus_3 = false;
        boolean plus_2 = false;
        boolean plus = false;
        String plus_color = "";
        boolean king = false;
        boolean taki = false;
        String taki_color = "";
        
        tmp=(String[]) parameters.get("plus_3");
		if (tmp != null)
			if (tmp[0] == "true") {
				plus_3 = true;
			} else
				plus_3 = false;

        tmp=(String[]) parameters.get("plus_2");
		if (tmp != null)
			if (tmp[0] == "true") {
				plus_2 = true;
			} else
				plus_2 = false;
        
        tmp=(String[]) parameters.get("plus");
		String[] tmp2=(String[]) parameters.get("plus_color");
        if(tmp!=null && tmp2 != null){
        	if (tmp[0] == "true")	{
        		if (Config.COLOR.contains(tmp2[0])) {
        			if(!tmp2[0].equals(Config.COLOR.get(4)))	{	// make sure COLOR != IRRELEVANT
        				plus=true;
        				plus_color=tmp2[0];
        			} else {
        				out.write("cannot set plus state to true while COLOR is IRRELEVANT - choose different color");
            			out.close();
        			}
        		} else {
        			out.write("cannot set plus state to true without setting valid plus color");
        			out.close();
        		}
			} else { // false plus state
				out.write("cannot set plus state to false while setting plus_color. please correct parameters and post again");
				out.close();
			}
        } else {
        	if(tmp != null && tmp2 == null) {
        		out.write("cannot set plus state without setting plus_color. please correct parameters and post again");
				out.close();
        	} else if (tmp==null && tmp2 !=null) {
        		out.write("cannot leave plus state empty while setting plus_color. please correct parameters and post again");
				out.close();
        	}	
        }
        
		tmp = (String[]) parameters.get("king");
		if (tmp != null)
			if (tmp[0] == "true") {
				king = true;
			} else
				king = false;

        tmp=(String[]) parameters.get("taki");
		tmp2=(String[]) parameters.get("taki_color");
        if(tmp!=null && tmp2 != null){
        	if (tmp[0] == "true")	{
        		if (Config.COLOR.contains(tmp2[0])) {
        			if(!tmp2[0].equals(Config.COLOR.get(4)))	{	// make sure COLOR != IRRELEVANT
        				taki=true;
        				taki_color=tmp2[0];
        			} else {
        				out.write("cannot set taki state to true while COLOR is IRRELEVANT - choose different color");
            			out.close();
        			}
        		} else {
        			out.write("cannot set taki state to true without setting valid taki color");
        			out.close();
        		}
			} else { // false taki state
				out.write("cannot set taki state to false while setting taki_color. please correct parameters and post again");
				out.close();
			}
        } else {
        	if(tmp != null && tmp2 == null) {
        		out.write("cannot set taki state without setting taki_color. please correct parameters and post again");
				out.close();
        	} else if (tmp==null && tmp2 !=null) {
        		out.write("cannot leave taki state empty while setting taki_color. please correct parameters and post again");
				out.close();
        	}	
        }
        
        // Validate taki move
        log.info("shooting Validate method");
        String error_msg = "";
        boolean answer = false;
        try {
        answer = Dealer.Validate(inPack, player_cards,taki,taki_color,plus,plus_color,plus_2,plus_3,king);
        } catch (AssertionError e) {
        	error_msg = e.getMessage();
        }
        log.info("got "+ answer);
        if(answer) {
        	out.write("VALID");
        	out.close();
        }
        else {
        	out.write("INVALID! due to: " + error_msg);
        	out.close();
        }

	}
	
	
	private void WrongColor(String paramName){
		out.write(paramName + " MUST be one of the following: BLUE,GREEN,YELLOW,RED or IRRELEVANT");
		out.close();
	}
	private void WrongType(String paramName){
		out.write(paramName + " MUST be one of the following: ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE," +
				"+2,PLUS,STOP,SWITCH DIRECTION,CHANGE COLOR,SWITCH HANDS,SUPER TAKI,TAKI,KING");
		out.close();
	}
	
}