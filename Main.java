// Pokemon Arena
// Kevin Cui
// This program allows users to pick a team of Pokemon from a data file that they enter and then battle opposing Pokemon using their Pokemon
import java.util.*;
import java.io.*;

class Main {
  public static void main(String[] args) {
    PokemonArena ket = new PokemonArena();
    PokemonArena.load();
    PokemonArena.chooseTeam();
    PokemonArena.preBattle();
    return;
  }
}

class PokemonArena{
  static int enemyPoke; // the enemy's Pokemon that is out
  static boolean goodWildCard=false; // if wildcard went through
  static boolean badWildCard=false;
  static boolean goodStun=false; // if the stun happened
  static boolean badStun=false;
  static boolean goodWildStorm=false; // if wildstorm went through
  static boolean badWildStorm=false;
  static int goodWildStormDam=0; // additional damage from wildstorm
  static int badWildStormDam=0;
  static int wildStorm; // if wildstorm will go through
  static int wildCard; // if wildcard will go through
  static int stun; // if stun will go through
  static int switchCount; // counter for displaying retreat Pokemon
  static int moveNum; // the move number
  static int firstPoke; // the first Pokemon that the user sends out
  static int moveDamage; // the damage the move does
  static int enemyMove; // the damage the enemy move does
  static double typeMultiplier; // the multiplier that will be determied by weaknesses and resistances
  static Scanner kb = new Scanner (System.in); // input scanner

  private static ArrayList<Pokemon> pickPokemon = new ArrayList<Pokemon> (); // the Pokemon that the user chose
  private static ArrayList<Pokemon> picked = new ArrayList<Pokemon> (); // all Pokemon loaded from the file
  private static int numLoad;

  public static void load(){ // method to load from file 

    try{
      Scanner inFile = new Scanner(new BufferedReader(new FileReader("pokemon.txt"))); // text file 
      numLoad = inFile.nextInt();
      inFile.nextLine(); // skipping the first line 
      for (int i=0; i<numLoad;i++){
        picked.add(new Pokemon(inFile.nextLine())); // adding each Pokemon object to an array list
      }
      inFile.close(); // close file 
    }
    catch (IOException ex){ // catching the error
      System.out.println("Error");
    }
  }
 public static ArrayList<Pokemon> chooseTeam(){ // method for the user to choose the 4 of Pokemon that they want
 System.out.println("\nWelcome to Pokemon Arena!\nChoose your team!\n");
   Scanner kb = new Scanner (System.in);
    for (int i=0;i<picked.size();i++){ // printing the names of the Pokemon based on the number in the data file
      System.out.println(i+1 + ". " + picked.get(i).getName());
    }

    // Getting the Pokemon the user wants to use

    for (int i =0;i<4;i++){ // allowing the user to pick which Pokemon from the list they want 
      System.out.println("Enter the number Pokemon you want");
      int getPoke = kb.nextInt();
      if (getPoke<1 || getPoke>picked.size()){ // if the user entered an invalid option allow them to pick again
        System.out.println("\nInvalid choice try again\n");
    for (int k=0;k<picked.size();k++){
      System.out.println(k+1 + ". " + picked.get(k).getName());
    }
    i-=1;
      }
      else{ // if the option is valid add the Pokemon to a seperate array list and remove it from the one with all the Pokemon
        pickPokemon.add(picked.get(getPoke-1));
        picked.remove(getPoke-1);
        for (int k=0;k<picked.size();k++){
          if (pickPokemon.size()==4){
            break;
          }
          System.out.println(k+1 + ". " + picked.get(k).getName());
       }
     }
    }

    // Printing the team the user chose 

    System.out.println("\nYour team is ");
    for (int i  = 0; i<pickPokemon.size();i++){
      System.out.println(i+1 +". "+ pickPokemon.get(i).getName());
    }
    System.out.println("");

    return pickPokemon;
  }

  public static void preBattle  () {
    // Getting the first Pokemon the user wants to send out 

    System.out.println("Enter the first Pokemon to send out!");
    for (int i = 0; i<1;i++){
     firstPoke = kb.nextInt();
     if (firstPoke>0 && firstPoke <5){
      System.out.println("\n"+pickPokemon.get(firstPoke-1).getName() + " I choose you!"); // displaying pokemon chosen to send out
     }
     else{ // letting the user pick again if they entered an invalid number 
       System.out.println("Invalid choice\nPlease try again");
       i-=1;
     }
    }

    enemyPoke = randint(0,picked.size()-1);
    System.out.println("Enemy: " + picked.get(enemyPoke).getName() + " I choose you!"); // displaying dialog for the enemy

    System.out.println("\nPress Enter to begin the battle!");

    kb.nextLine();
    kb.nextLine(); // stopping until the user enters Enter

    int chooseFirst = randint(0,1); // selecting who goes first 

    if (chooseFirst ==0){
      battle();
      return; // ending the method at the end of the game 
    }
    else{
      enemyBattle();
      return;
    }
  }

  public static void battle (){
    switchCount = 1; 
    while (true){ // loop for the users turn 
      goodWildCard = false; // setting all 1 turn variables to false
      goodWildStorm = false;
      goodStun = false;
      goodWildStormDam = 0;
    System.out.println("\n-----------------------------------\n");
    System.out.println("             YOUR TURN            ");
    System.out.println("\n-----------------------------------");

    for (int i=0;i<pickPokemon.size();i++){
     if (pickPokemon.get(i).getEnergy() >=50){ // making sure energy cannot go over 50
        pickPokemon.get(i).setEnergy(50);
      }
    }
    if (pickPokemon.get(firstPoke-1).getDisable()==true){
        System.out.println(pickPokemon.get(firstPoke-1).getName()+" is disabled!"); // printing disabled dialog 
          }
    System.out.println ("\nWhat will " +pickPokemon.get(firstPoke-1).getName() + " do?"); // displaying battle choices
    System.out.println("HP: " + pickPokemon.get(firstPoke-1).getHp()); // displaying HP
    System.out.println("Energy: " + pickPokemon.get(firstPoke-1).getEnergy()); // displaying energy

    System.out.println("1. Attack"); // displaying what the Pokemon can do
    System.out.println("2. Retreat");
    System.out.println("3. Pass\n");

    System.out.println(picked.get(enemyPoke).getName()+" has "+ picked.get(enemyPoke).getHp()+ " HP!\n"); // displaying the enemy Pokemon's HP

    int action = kb.nextInt();
    System.out.println("");
    if (action == 1){ // if the user picked attack call the attack method 
      actionOne();
    }
    else if (action == 2){ // if the user picked retreat call the retreat method 
      actionTwo();
    }
    else if (action == 3){ // if the user picked pass 
      System.out.println(pickPokemon.get(firstPoke-1).getName()+" passed");
      }
    else{
      System.out.println("Invalid Input");
      System.out.println(pickPokemon.get(firstPoke-1).getName()+" passed!");
    }
    for (int i =0; i<pickPokemon.size();i++){
      pickPokemon.get(i).addEnergy(10); //adding energy back to all Pokemon
    }
    System.out.println(pickPokemon.get(firstPoke-1).getName()+" gained 10 energy back!");
    if (picked.get(enemyPoke).getHp()<=0){ // if the enemy Pokemon ran out of HP due to the attack 
      badFaint();
      if (picked.size()==0){ // if all the enemy Pokemon have fainted then break out of the method 
        break;
        }
      }
      enemyBattle(); // at the end of the users turn call the enemy's turn
      if (pickPokemon.size()==0){ // if all the users Pokemon have fainted then break out of the method 
        return;
      }
    }
  }

  public static void actionOne(){ // method if the user attacks 
      System.out.println("");
      for (int i =0; i<pickPokemon.get(firstPoke-1).getAttacks().length;i++){ // printing each attack the users Pokemon can use and also the properties of each attack
        System.out.println(i+1+". "+pickPokemon.get(firstPoke-1).getAttacks()[i][0] + ", Cost: " + pickPokemon.get(firstPoke-1).getAttacks()[i][1] + ", Damage: "+ pickPokemon.get(firstPoke-1).getAttacks()[i][2] + ", Special: " + pickPokemon.get(firstPoke-1).getAttacks()[i][3]); // printing avaliable moves
      }
      System.out.println("");

      while (true){ // loop for the attacks 
        moveNum = kb.nextInt(); // the move number the user uses 
        if (moveNum>0 && moveNum<pickPokemon.get(firstPoke-1).getAttacks().length+1){ // if the move selected is in range
      if (pickPokemon.get(firstPoke-1).getEnergy() - Integer.parseInt(pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][1])>=0){ // if the Pokemon has enough energy to make the move
        System.out.println("");
        System.out.println(pickPokemon.get(firstPoke-1).getName()+" used "+pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][0]+"!"); // printing dialog

        pickPokemon.get(firstPoke-1).addEnergy(-1*(Integer.parseInt(pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][1]))); // changing energy

        goodSpecial(); // checking for specials of the move used 

        if (!goodWildCard && !goodWildStorm && !badStun){ // if wild card, wild storm, and stun lets the move through

        if (picked.get(enemyPoke).getWeak().equals(pickPokemon.get(firstPoke-1).getType())){ // checking the strengths and weaknesses of the user's Pokemon and the enemy Pokemon and applying the type multiplier accordingly 
          typeMultiplier = 2;
        }

        else if (picked.get(enemyPoke).getStrong().equals(pickPokemon.get(firstPoke-1).getType())){
          typeMultiplier = 0.5;
        }
        else{
          typeMultiplier = 1;
        }

        if (goodWildStormDam == 0){ // if wildstorm did not do additional damage
        if (typeMultiplier == 2){ // printing dialog for super effective
          System.out.println("It's super effective!");
        }
        else if (typeMultiplier == 0.5){ // printing dialog for not very effective
          System.out.println("It's not very effective");
        }
        moveDamage = round(typeMultiplier*(Integer.parseInt(pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][2]) - pickPokemon.get(firstPoke-1).getDisableDam())) ; // changing hp normally
        picked.get(enemyPoke).setHp(moveDamage);
          }
          else{ // if wild storm did additional damage 
            if (typeMultiplier == 2){ // dialog for type multipliers 
              System.out.println("It's super effective!");
            }
            else if (typeMultiplier == 0.5){
              System.out.println("It's not very effective");
            }
          moveDamage = round(typeMultiplier*(goodWildStormDam - pickPokemon.get(firstPoke-1).getDisableDam())) ; // changing hp with wild storm multiplier
          picked.get(enemyPoke).setHp(moveDamage);
          System.out.println(pickPokemon.get(firstPoke-1).getName()+" did "+goodWildStormDam+ " damage thanks to Wild Storm!"); // printing wild storm additional damage dialog
          }
          System.out.println(picked.get(enemyPoke).getName()+" took "+moveDamage+" damage!"); // printing damage dialog 
          goodRecharge(); // checking for recharge special after original energy is subtracted 
        }
        else{ // if wild storm, wild card, and stun do not let the move through 
          if (goodWildStorm){ // printing wild storm fail dialog
            System.out.println("Attack failed due to Wild Storm!");
          }
          if (goodWildCard){ // printing wild card fail dialog
            System.out.println("Attack failed due to Wild Card!");
          }
          if (badStun){ // printing stun fail dialog 
            System.out.println(pickPokemon.get(firstPoke-1).getName()+ " is stunned!");
          }
          System.out.println(picked.get(enemyPoke).getName()+" took no damage!");
        }
      }
      else{
        System.out.println("Not enough energy\n"+pickPokemon.get(firstPoke-1).getName()+" passed"); // printing dialog if the user's Pokemon doesn't have enough energy to attack
          }
        break;
        }
      else{
        System.out.println("Invaild input\n"+pickPokemon.get(firstPoke-1).getName()+" passed"); // printing text for if the input for the attack was invalid 
        break;
        }
      }
    }

    public static void actionTwo(){ // if the user chose retreat 
      ArrayList<Pokemon> switchPokemon = new ArrayList<Pokemon>();
      System.out.println("\nPick a pokemon to switch to");
      for (int i =0; i<pickPokemon.size(); i++){ // displaying the possibel Pokemon that the user can swtich into
      String retreatPokes = pickPokemon.get(i).getName();
      if (retreatPokes != pickPokemon.get(firstPoke-1).getName()){
        switchPokemon.add(pickPokemon.get(i));
        System.out.println(switchCount+ ". "+retreatPokes);
        switchCount += 1;
      }
    }
      while (true){ // loop for retreating 
        int switchPoke = kb.nextInt(); // the Pokemon the user wants to switch to 
        if (switchPoke>0 && switchPoke<switchPokemon.size()+1){ // if the Pokemon chosen is in range 
        String retreatName = switchPokemon.get(switchPoke-1).getName(); // the name of the Pokemon the user wants to switch to 
        int retreatIndex = 0; // variable to go through indexes of the user's Pokemon array list 
        for (int i =0; i<pickPokemon.size();i++){ // finding which index matches the Pokemon the user wants to switch to 
          if (pickPokemon.get(i).getName() == retreatName){
            retreatIndex = i+1;
          }
        }
        firstPoke = retreatIndex; // setting the active Pokemon to that index 
        switchCount = 1;
        break;
        }
        else{ // if input was invalid 
          System.out.println("Invaild input");
          break;
        }
      }
    }

  public static void enemyBattle(){ // enemy Pokemon's turn 
      if (picked.get(enemyPoke).getEnergy()>50){
        picked.get(enemyPoke).setEnergy(50); // making sure energy cannot go over 50
        }
      System.out.println("\n-----------------------------------\n");
      System.out.println("             ENEMY TURN            ");
      System.out.println("\n-----------------------------------");
      badWildCard = false; // setting all 1 turn variables to false 
      badWildStorm = false;
      badStun = false;
      badWildStormDam = 0;
      enemyMove = randint(0,picked.get(enemyPoke).getAttacks().length-1); // randomly picking the enemy's attack

      System.out.println("HP: " + picked.get(enemyPoke).getHp()); // printing the enemy's HP

      if (picked.get(enemyPoke).getEnergy() - Integer.parseInt(picked.get(enemyPoke).getAttacks()[enemyMove][1])>=0){ // if there is enough energy to attack

        System.out.println(picked.get(enemyPoke).getName()+" used "+picked.get(enemyPoke).getAttacks()[enemyMove][0] + "!"); // dialog for enemy attack

        badSpecial(); // checking specials for the enemy's move 

        if (!badWildCard && !badWildStorm && !goodStun){ // if wild card, wildStorm and stun lets the move through

        if (picked.get(enemyPoke).getWeak().equals(pickPokemon.get(firstPoke-1).getType())){ // checking for type multipliers on the user's Pokemon and setting accordingly 
          typeMultiplier = 2;
        }

        else if (picked.get(enemyPoke).getStrong().equals(pickPokemon.get(firstPoke-1).getType())){
          typeMultiplier = 0.5;
        }

        else{
          typeMultiplier = 1;
        }

        if (badWildStormDam == 0){ // if wildstorm did not additional damage
        if (typeMultiplier == 2){ // printing dialog for type multipliers
          System.out.println("It's super effective!");
        }
        else if (typeMultiplier == 0.5){
          System.out.println("It's not very effective");
        }
        moveDamage = round(typeMultiplier*(Integer.parseInt(picked.get(enemyPoke).getAttacks()[enemyMove][2])- picked.get(enemyPoke).getDisableDam())); // changing hp normally
        pickPokemon.get(firstPoke-1).setHp(moveDamage);
          }
          else{ // printing dialog for type multipliers 
            if (typeMultiplier == 2){
              System.out.println("It's super effective!");
            }
            else if (typeMultiplier == 0.5){
              System.out.println("It's not very effective");
        }
          moveDamage  = round(typeMultiplier*(badWildStormDam - picked.get(enemyPoke).getDisableDam())); // changing hp with wild storm multiplier
          pickPokemon.get(firstPoke-1).setHp(moveDamage);
          System.out.println(picked.get(enemyPoke).getName()+" did "+goodWildStormDam+ " damage thanks to Wild Storm!");  // printing wild storm additional damage dialog 
        }
        System.out.println(pickPokemon.get(firstPoke-1).getName()+" took "+moveDamage+" damage!"); // user's Pokemon damage dialog 
        if (pickPokemon.get(firstPoke-1).getHp()<=0){ // if the user's Pokemon faints as a result of an attack
          goodFaint();
          return; // if the user has no Pokemon break out of method
        }
        if (picked.get(enemyPoke).getDisable()==true){ // if the enemy is diabled dialog 
          System.out.println(picked.get(enemyPoke).getName()+" is disabled!");
        }
        badRecharge(); // checking if the enemy Pokemon's special is recharge after original energy is subtracted 
      }
      else{ // printing respective dialogs if the move failed 
        if (badWildStorm){
          System.out.println("Attack failed due to Wild Storm!");
          }
          if (badWildCard){
            System.out.println("Attack failed due to Wild Card!");
          }
          if (goodStun){
            System.out.println(picked.get(enemyPoke).getName()+ " is stunned!");
          }
          System.out.println(pickPokemon.get(firstPoke-1).getName()+" took no damage!");
        }
     }
     else{ // if the enemy Pokemon doesn't have enough energy to attack and passes dialog 
       System.out.println(picked.get(enemyPoke).getName()+" passed");
     }
     picked.get(enemyPoke).addEnergy(10); // adding energy back to enemy Pokemon
     battle(); // setting the turn back to the user 
  }

  public static void badFaint(){ // if the enemy Pokemon faints 
    pickPokemon.get(firstPoke-1).setDisableDam(0); // resetting diable damages for both Pokemon
    picked.get(enemyPoke).setDisableDam(0);
    for (int i =0;i<pickPokemon.size();i++){
      pickPokemon.get(i).setDisable(false);
    }
    System.out.println(picked.get(enemyPoke).getName()+" fainted!"); // printing faainted dialog 
    picked.remove(enemyPoke); // removing fainted Pokemon from array list
    if (picked.size()==0){ // if the enemy has no Pokmemon left then call the winner method 
      winner();
      return; // break out of method if enemy is out of Pokemon
    }
    System.out.println("Press Enter to start the next battle!");

    kb.nextLine();
    kb.nextLine(); // making user press enter to start next battle

    for (int i=0; i<pickPokemon.size();i++){ //adding 20 HP to all the user's remaining Pokemon
      pickPokemon.get(i).setHp(-20);
      if (pickPokemon.get(i).getHp()>pickPokemon.get(i).maxHp()){
        pickPokemon.get(i).changeHp(pickPokemon.get(i).maxHp());
      }
    }
    enemyPoke = randint(0,picked.size()-1); // picking a random Pokemon for the enemy to send out next
    System.out.println("Enemy: "+ picked.get(enemyPoke).getName()+" I choose you!");
    int turn = randint (0,1); // randomly selecting whose turn it will be next 
    if (turn == 0){
      battle();
    }
    else{
      enemyBattle();
    }
  }

  public static void goodFaint(){ // if the user's Pokemon fainted 
    System.out.println(pickPokemon.get(firstPoke-1).getName()+" fainted!\n"); // fainted dialog 
    pickPokemon.remove(firstPoke-1); // removing the Pokemon from the array list 
    if (pickPokemon.size()==0){ // if the user is out of Pokemon print losing text and breaking out of method 
      System.out.println("All your Pokemon have fainted!\nYou lost the battle!\nBetter luck next time!");
      return;
    }
    System.out.println("Choose another Pokemon to switch to: \n");
    for (int i =0; i<pickPokemon.size();i++){ // if the user is not out of Pokemon print list of remaining Pokemon to switch in to 
      System.out.println(i+1+". "+pickPokemon.get(i).getName());
    }
    while (true){ // loop for switch input 
      int newPokemon = kb.nextInt(); // the index of the Pokemon that the user wants to switch in to ntext 
      if (newPokemon>0 && newPokemon<pickPokemon.size()+1){ // if the input is valid set the selected input to the active Pokemon and break out of the loop 
        firstPoke = newPokemon;
        break;
      }
      else{
        System.out.println("Invalid Choice\nPlease try again"); // if the input is invalid print the text and allow the user to pick again
      }
    }
    battle(); // allowing the user to have the next turn 
  }
  public static void goodRecharge(){ // user's Pokemon recharge special
    if (pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][3]=="recharge"){ // if special is recharge 
    pickPokemon.get(firstPoke-1).addEnergy(20); // add 20 to energy
    System.out.println(pickPokemon.get(firstPoke-1).getName()+" gained 20 energy thanks to Recharge!");
     }
  }

  public static void badRecharge(){ // enemy's Pokemon recharge special
    if (picked.get(enemyPoke).getAttacks()[enemyMove][3]=="recharge"){ // if special is recharge 
      picked.get(enemyPoke).addEnergy(20); // add 20 to energy
        }
  }

  public static void goodSpecial(){ // the user's Pokemon's specials
      if (pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][3].equals("disable")){ // if special is diable
          pickPokemon.get(firstPoke-1).setDisableDam(10); // damage to subtract 
          picked.get(enemyPoke).setDisable(true); // setting field to true for dialog 
        }

      if (pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][3].equals("wild card")){ // if special is wildcard
          wildCard = randint(0,1); // variable for if the move will go through or not 
          if (wildCard == 0){
            goodWildCard = true;
          }
        }

      if (pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][3].equals("stun")){ // if special is stun
          stun = randint(0,1); // variable for if the move will go through or not 
          if (stun ==0){
            goodStun = true;
          }
        }
        if (pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][3].equals("wild storm")){ // if special is wild storm
          wildStorm = randint(0,1); // variable for if the move will go through or not 
          if (wildStorm ==0){
            goodWildStorm = true;
          }
        else{ // if the move goes through add the damage onto itself and continue doing it until a 0 is generated 
          goodWildStormDam += Integer.parseInt(pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][2]);
            while (wildStorm !=0){
              wildStorm = randint(0,1);
              if (wildStorm == 1){
                goodWildStormDam += Integer.parseInt(pickPokemon.get(firstPoke-1).getAttacks()[moveNum-1][2]);
              }
            }
          }
        }
      }

  public static void badSpecial(){ // specials for the enemy's Pokemon
      if (picked.get(enemyPoke).getAttacks()[enemyMove][3].equals("disable")){ // if special is diable
          picked.get(enemyPoke).setDisableDam(10); // damage to subtract 
          pickPokemon.get(firstPoke-1).setDisable(true); //setting field to true for dialog 
        }

      if (picked.get(enemyPoke).getAttacks()[enemyMove][3].equals("wild card")){ // if special is wildcard
          wildCard = randint(0,1); // randomly choosing if move will go through 
          if (wildCard == 0){
            badWildCard = true;
          }
        }

      if (picked.get(enemyPoke).getAttacks()[enemyMove][3].equals("stun")){ // if special is stun
          stun = randint(0,1); // randomly choosing if move will go through 
          if (stun ==0){
            badStun = true;
          }
        }
        if (picked.get(enemyPoke).getAttacks()[enemyMove][3].equals("wild storm")){ // if special is wild storm
          wildStorm = randint(0,1); // randomly choosing if move will go through 
          if (wildStorm ==0){
            badWildStorm = true;
          } 
        else{ // if the move goes through add the damage onto itself and continue doing it until a 0 is generated 
          badWildStormDam += Integer.parseInt(picked.get(enemyPoke).getAttacks()[enemyMove][2]);
            while (wildStorm !=0){
              wildStorm = randint(0,1);
              if (wildStorm == 1){
                badWildStormDam += Integer.parseInt(picked.get(enemyPoke).getAttacks()[enemyMove][2]);
              }
            }
          }
        }
  }

  public static void winner(){ // if all the enemy Pokemon have been defeated print winner message 
    System.out.println("\nAll the enemy Pokemon have been defeated!");
    System.out.println("Congratulations!");
    System.out.println("You are now Traniner Supreme!");
  }

  public static int randint(int low, int high){ // random integer method
        return (int)(Math.random()*(high-low+1)+low);
      }

  public static int round(double val) { // rounding method to not have a decimal HP in the case that a resistance and an attack damage cause the final damage to be a decimal 
    double x = val % 1.0; // adding decimal
    if (x < 0.5){ // if the number rounds down by rounding rules then round it down
      return (int)val;
    } 
    else{ // if the number rounds down by rounding rules then round it down
      return (int) val+1;
    } 

  }
}

class Pokemon{ // class for making Pokemon objects 
  private String name; // name field
  private int hp; // HP field 
  private int maxHp; // max HP field 
  private String type; // type field 
  private String weak; // weakness field 
  private String strong; // strength field 
  private String [][] attacks; // attacks field 
  private int energy; // energy field 
  private boolean disabled; // disabled field 
  private int disableDam; // disable damage

  public Pokemon(String line){ // setting each field to it's corrosponding spot in the text file 
    String [] stats = line.split(",");
    name = stats[0];
    hp = Integer.parseInt(stats[1]);
    maxHp = Integer.parseInt(stats[1]);
    type = stats[2];
    strong = stats[3];
    weak = stats[4];
    attacks = new String [Integer.parseInt(stats[5])][4];
    for (int k=0; k<Integer.parseInt(stats[5]);k++){
      for (int i=0; i<4;i++){
        attacks[k][i] = stats[4*k+6+i];
      }
    }
    energy = 50;
    disabled = false;
  }
  public String getName (){ // gets the Pokemon's name
    return name;
  }
  public String [][] getAttacks(){ // gets the Pokemon's attacks
    return attacks;
  }
  public int getHp(){ // gets the Pokemon's HP
    return hp;
  }
  public void setHp(int damage){ // changes the Pokemon's HP by an amount
    this.hp=hp-damage;
  }
  public int maxHp(){ // gets the maximum HP of the Pokemon
    return maxHp;
  }
  public void changeHp(int change){ // changes the HP to a certain amount
    this.hp=change;
  }
  public int getEnergy(){ // gets the energy of the Pokemon
    return energy;
  }
  public void addEnergy(int added){ // adds to the Pokemon's Energy
    this.energy = energy+added;
  }
  public void setEnergy(int setEnergy){ // sets the Pokemon HP to a certain amount 
    this.energy=setEnergy;
  }
  public boolean getDisable(){ // returns if the Pokemon is disabled or not 
    return disabled;
  }
  public void setDisable(boolean disableStatus){ // sets a Pokemon's status to disabled or not disabled 
    this.disabled = disableStatus;
  }
  public String getType(){ // gets the Pokemon's type 
    return type;
  }
  public String getStrong(){ // gets the type that the Pokemon is strong to
    return strong;
  }
  public String getWeak(){ // gets the type that the Pokemon is weak to 
    return weak;
  }
  public void setDisableDam(int disableDamage){ // setss disable damage
    this.disableDam = disableDamage;
  }
  public int getDisableDam(){ // returns disable damage
    return disableDam;
  }
}
