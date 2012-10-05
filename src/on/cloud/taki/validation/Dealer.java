package on.cloud.taki.validation;

import java.util.Vector;


public class Dealer {
	
	/**
	 * Default Constructor
	 * @param 	inPack		 	- the current active exposed card in deck. assuming valid card, Not null
	 * @param 	player_cards	- Vector<Card> of cards the player try to drop altogether. assuming valid card(s), Not null
	 * @return	the truth
	 */
	public static boolean Validate(Card inPack, Vector<Card> player_cards) {
		
		return Validate(inPack, player_cards, false, "" , false, "" , false , false , false);
		
	}
	
	
	/**
	 * Taki Validation method will validate whether the player cards are valid to drop or nay.
	 * @param 	inPack		 	- the current active exposed card in deck. assuming valid card, Not null
	 * @param 	player_cards	- Vector<Card> of cards the player try to drop altogether. assuming valid card(s), Not null
	 * @param 	_taki_state  	- is in TAKI state? 
	 * @param 	_taki_color 	- the TAKI color
	 * @param 	_plus_state 	- is in PLUS state?
	 * @param 	_plus_color 	- the PLUS color
	 * @param 	_plus_two_state - is '+2' state?
	 * @param 	_plus_three_state - is '+3' state?
	 * @param 	_king_state 	- is in King state?
	 * first card dropped will be the first element in the Vector(elementAt(0))
	 * @return the truth
	 * @see Card
	 * @see <a href="http://www.shafirgames.com/images/instructions/taki_he.pdf">Taki Rules</a>
	 */
	public static boolean Validate(Card inPack, Vector<Card> player_cards,boolean _taki_state,String _taki_color,boolean _plus_state,String _plus_color,boolean _plus_two_state, boolean _plus_three_state , boolean _king_state) throws AssertionError {
		// INIT
		boolean cannot_throw_more=false;  // makes sure the player did not threw more than the allowed per turn.
		boolean taki_state=_taki_state;
		String taki_color=_taki_color;
		boolean plus_state=_plus_state;
		String plus_color=_plus_color;
		boolean plus_two_state=_plus_two_state;
		boolean plus_three_state=_plus_three_state;
		boolean king_state=_king_state;
		Card currently_active_card = new Card(inPack);
		
		int ind = 0;		// evaluates the first card(index 0) first.
		
		while (player_cards.size() > 0)	{
			if (cannot_throw_more)
				throw new AssertionError("player threw one too many cards in current turn. Wait for other turn to throw more cards.");
			if(king_state)		// any 'next' card is legal
				king_state=false;
			if(inPack.getColor().equals(Config.COLOR.get(4)))  {	// if inPack Color = IRRELEVANT
				if(!inPack.getType().equals(Config.TYPE.get(13)) && !inPack.getType().equals(Config.TYPE.get(14)) && !inPack.getType().equals(Config.TYPE.get(15)) && !inPack.getType().equals(Config.TYPE.get(17)))
					throw new AssertionError("Card other than CHANGE COLOR, SWITCH HANDS, SUPER TAKI or KING cannot have IRRELEVANT color.");
			}
			if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(14))) {  // if SWITCH HANDS
				if(plus_three_state)	// this will not evade +3 state
					throw new AssertionError("Cannot evade +3 state with SWITCH HANDS card.");
				// trade hands between players. last card played will be the currently_active_card.
				currently_active_card = new Card(inPack);
				//reset states
				taki_state=false;
				taki_color="";
				plus_state=false;
				plus_color="";
				plus_two_state=false;
				plus_three_state=false;
				king_state=false;
				player_cards.remove(ind);  // SWITCH HANDS card cannot be a leader card(on top of the pack)
				continue;
			}
			if(taki_state)
				if(taki_color.equals(""))		// on SUPER TAKI state
					taki_color = player_cards.elementAt(ind).getColor();
			if(plus_three_state && !plus_state)
				if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(10))) {	// if '+' than continue. NEXT MUST BE FOLLOWED BY A '3' CARD
					plus_state=true;
					plus_color = player_cards.elementAt(ind).getColor();	// keep the color of that plus, the following '3' card must be the same color to be valid
					currently_active_card = new Card(player_cards.elementAt(ind));
					player_cards.remove(ind);
					continue;
				} else 
					throw new AssertionError("in +3 state, all other players must pick up 3 cards. The only way to cancel this state is to throw '+' and '3' cards with the same color. any different card is invalid");
				
			if(plus_three_state && plus_state)	
				if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(2)))  {	// if '3' , check that his color matches previous '+' card
					if(player_cards.elementAt(ind).getColor().equals(plus_color)) {
						plus_state=false;
						plus_color="";
						plus_three_state=false;
						cannot_throw_more=true;
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					} else
						throw new AssertionError("in +3 state, all other players must pick up 3 cards. The only way to cancel this state is to throw '+' and '3' cards with the same color. The '+' and the '3' Not in the same color");
//						return false; // a '3' is thrown, BUT wrong color.
				} else
					throw new AssertionError("in +3 state, all other players must pick up 3 cards. The only way to cancel this state is to throw '+' and '3' cards with the same color. The card that follows '+' is Not a '3' thus invalid");
//					return false;	// not a '3' after plus_three_state and '+' altogether
						
			else if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(13))) { // if CHANGE COLOR
				if(plus_two_state || plus_three_state)
					throw new AssertionError("Cannot evade +2 Or +3 states with a CHANGE COLOR card.");
				if(king_state) {
					king_state=false;
					cannot_throw_more=true;
				}
				if(plus_state) {
					plus_state=false;
					plus_color="";
				}
				if(!taki_state)
					cannot_throw_more=true;
				else {  //taki_state is true
					taki_state = false;		// change color will end taki_state
					taki_color="";
					cannot_throw_more=true;
				}
				currently_active_card = new Card(player_cards.elementAt(ind));
				player_cards.remove(ind);
				continue;		// else, continue anyway
			}
			else if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(17))) { // if KING
				king_state = true;
				taki_state=false;
				taki_color="";
				plus_state=false;
				plus_color="";
				plus_two_state=false;
				plus_three_state=false;
				cannot_throw_more=false;
				currently_active_card = new Card(player_cards.elementAt(ind));
				player_cards.remove(ind);
				continue;
			}
			else if (player_cards.elementAt(ind).getType().equals(Config.TYPE.get(15))) { // if SUPER TAKI
				if(plus_three_state || plus_two_state)
					throw new AssertionError("Cannot evade +2 Or +3 states with a SUPER TAKI card.");
				if(plus_state){
					plus_state=false;
					plus_color="";
				}
				if(king_state)
					king_state=false;
				taki_state=true;	
				taki_color="";		// taki color stays unknown; this will be determined on next card
				currently_active_card = new Card(player_cards.elementAt(ind));
				player_cards.remove(ind);
				continue;
			}

			if (currently_active_card.getColor().equals(player_cards.elementAt(ind).getColor()))  {	//if same color
				if(taki_state) {
					if(plus_two_state) {
						if (player_cards.elementAt(ind).getType().equals(Config.TYPE.get(9))) {	// '+2' ..
							cannot_throw_more=false;	// can keep throw cards due to taki state
							currently_active_card = new Card(player_cards.elementAt(ind));
							player_cards.remove(ind);
							continue;
						} else { // not a '+2' card
							plus_two_state=false;			
						}
					}
					if(plus_three_state){
						plus_three_state=false; // because another card is being evaluated now, after +3_state archived. this will force a false on state_+3.
					}
					if(plus_state){
						if (player_cards.elementAt(ind).getType().equals(Config.TYPE.get(2))) {	// if a '3' card is after a '+' - +3_state is true
							cannot_throw_more=false; //due to taki_state and plus_state
							plus_state=false;
							plus_color="";
							plus_three_state=true;
							currently_active_card = new Card(player_cards.elementAt(ind));
							player_cards.remove(ind);
							continue;
						} else {
							plus_state = false;
							plus_color="";
						}
					}
					if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(9)))  { // if '+2'..
						cannot_throw_more=false;	// can keep throw cards due to taki state
						plus_two_state=true;
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
					if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(10)))  {  // if '+' ..
						plus_state=true;
						cannot_throw_more=false; //due to taki_state and plus_state
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
					if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(10)))  { // if 'CHANGE COLOR'..
						taki_state=false;
						taki_color="";
						cannot_throw_more=true;
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
					if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(10)))  { // if 'SUPER TAKI' ..
						taki_color="";	// taki color stays unknown; this will be determined on next card
					}
					
					// on all other cases just continue
					currently_active_card = new Card(player_cards.elementAt(ind));
					player_cards.remove(ind);
					continue;
					
				} else { // else (same color and) not in taki_state..
					if (player_cards.elementAt(ind).getType().equals(Config.TYPE.get(9)) && !plus_two_state) { // if not in +2_state and player puts '+2' ..
						cannot_throw_more = true;
						plus_two_state = true;
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
					if (plus_two_state) { // in '+2' state
						if (player_cards.elementAt(ind).getType().equals(Config.TYPE.get(9))) { // if '+2' ..
							cannot_throw_more = true;
							currently_active_card = new Card(player_cards.elementAt(ind));
							player_cards.remove(ind);
							continue;
						} else
							throw new AssertionError("Cannot evade +2 state with card different that '+2'");
					}
					if(plus_state) {
						if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(2)))  {		// if '3'
							plus_three_state=true;
							cannot_throw_more = true;
						}
						if(!player_cards.elementAt(ind).getType().equals(Config.TYPE.get(10))){	// if not '+' ..
							cannot_throw_more=true;
						} else { // if another '+'
							cannot_throw_more=false;
							currently_active_card = new Card(player_cards.elementAt(ind));
							player_cards.remove(ind);
							continue;
						}
						plus_state=false;
						plus_color="";
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
					
					if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(16))) { // if TAKI
						taki_state = true;
						taki_color = player_cards.elementAt(ind).getColor();
						if(!taki_color.equals(currently_active_card.getColor()))
							throw new AssertionError("Cannot throw TAKI card with different color than the card currently active in Deck");
						cannot_throw_more=false;
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
					if(player_cards.elementAt(ind).getType().equals(Config.TYPE.get(10))) { // if '+'
						plus_state=true;
						plus_color = player_cards.elementAt(ind).getColor();
						cannot_throw_more=false;
						currently_active_card = new Card(player_cards.elementAt(ind));
						player_cards.remove(ind);
						continue;
					}
				}
				currently_active_card = new Card(player_cards.elementAt(ind));
				player_cards.remove(ind);
				continue;
			}
			
			
			else if (currently_active_card.getType().equals(player_cards.get(ind).getType())) { // if same type (like '7' and '7')
				if(taki_state)		// same type, NOT the same color
					if(!taki_color.equals(currently_active_card.getColor())) {
							taki_state=false;
							cannot_throw_more=true;
							currently_active_card = new Card(player_cards.elementAt(ind));
							player_cards.remove(ind);
							continue;
					}
				if(plus_two_state)	{	// both inPack and players card is same type ('+2') so its legal
					cannot_throw_more=true;
					currently_active_card = new Card(player_cards.elementAt(ind));
					player_cards.remove(ind);
					continue;
				}
				if(plus_three_state)
					throw new AssertionError("in '+3' state ,Cannot evade with any card except the sequence '+' and '3' that has the same color.");
				if(king_state) {
					king_state=false;
					cannot_throw_more=true;
					currently_active_card = new Card(player_cards.elementAt(ind));
					player_cards.remove(ind);
					continue;
				}
				if(plus_state) {
					plus_color = player_cards.get(ind).getColor();
					
					cannot_throw_more=false;
					currently_active_card = new Card(player_cards.elementAt(ind));
					player_cards.remove(ind);
					continue;
				}
					
				cannot_throw_more=true;
				
				currently_active_card = new Card(player_cards.elementAt(ind));
				player_cards.remove(ind);
				continue;
			}
			
			throw new AssertionError("Not the same color AND Not the same type");
//			return false;
		}		// end of while
		
		return true;

	}

	
	
}
