import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text. *;
import java.util. *;



public class Auction {
   
	private static Scanner scanner = new Scanner(System.in);
	private static String username;
	private static Connection conn;

	enum Category {
		ELECTRONICS,
    BOOKS, 
		HOME,
		SPORTINGGOODS,
    SOUVENIR,
		OTHERS;
   
   private static final Map<Category, String> categoryMap = new HashMap<>();

    // Static block to initialize the map
    static {
        categoryMap.put(Category.ELECTRONICS, "Electronics");
        categoryMap.put(Category.BOOKS, "Books");
        categoryMap.put(Category.HOME, "Home");
        categoryMap.put(Category.SPORTINGGOODS, "Sporting Goods");
        categoryMap.put(Category.SOUVENIR, "Souvenir");
        categoryMap.put(Category.OTHERS, "Others");
    }

    // Static method to retrieve the string value for a Condition
    public static String getCategory (Category category) {
        return categoryMap.get(category);
    }
	}
 
 
	enum Condition {
		NEW,
		LIKE_NEW,
		GOOD,
		ACCEPTABLE;
   
   private static final Map<Condition, String> conditionMap = new HashMap<>();

    // Static block to initialize the map
    static {
        conditionMap.put(Condition.NEW, "New");
        conditionMap.put(Condition.LIKE_NEW, "Like-new");
        conditionMap.put(Condition.GOOD, "Good");
        conditionMap.put(Condition.ACCEPTABLE, "Acceptable");
    }

    // Static method to retrieve the string value for a Condition
    public static String getCondition(Condition condition) {
        return conditionMap.get(condition);
    }
	}

	private static boolean LoginMenu() {
		String userpass, isAdmin;
		System.out.println("----< User Login >");
		try {
			while (true) {
			System.out.print(" ** To go back, enter 'back' in user ID.\n" +
                                	"     user ID: ");
			username = scanner.next();
			scanner.nextLine();

			if(username.equalsIgnoreCase("back")){
				return false;
			}

			System.out.print("     password: ");
			userpass = scanner.next();
			scanner.nextLine();

			//로그인 잘못했을 때 구현 필요
			String sql= "select * from worker where id = ?";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,username);
                        ResultSet rs=pstmt.executeQuery();

			if (!rs.next() || !rs.getString("password").equals(userpass)) {
                        	System.out.println("\nError: Not signed up, or wrong password.");
                                continue;
                        }
			break;

			}


		}catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.");
			username = null;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* Your code should come here to check ID and password */ 

		if (false) {  
			/* If Login Fails */
			System.out.println("Error: Incorrect user name or password");
			return false; 
		}

		System.out.println("You are successfully logged in.\n");
		return true;
	}

	private static boolean SellMenu() {
		Category category=null;
		Condition condition=null;
    String description;
		char choice=' ';
		int price;
		boolean flag_catg = true, flag_cond = true;

		do{
			System.out.println(
					"----< Sell Item >\n" +
					"---- Choose a category.\n" +
					"    1. Electronics\n" +
					"    2. Books\n" +
					"    3. Home\n" +
					"    4. Sporting Goods\n" +
					"    5. Souvenir\n" +
          "    6. Other Categories\n" +
					"    P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
			}catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
			
			}

			flag_catg = true;

			switch (choice){
				case '1':
					category = Category.ELECTRONICS;
					continue;
				case '2':
					category = Category.BOOKS;
					continue;
				case '3':
					category = Category.HOME;
					continue;
				case '4':
					category = Category.SPORTINGGOODS;
					continue;
				case '5':
					category = Category.SOUVENIR;
					continue;
				case '6':
					category = Category.OTHERS;
					continue;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.");
					flag_catg = false;
					continue;
			}
		}while(!flag_catg);

		do{
			System.out.println(
					"---- Select the condition of the item to sell.\n" +
					"   1. New\n" +
					"   2. Like-new\n" +
					"   3. Used (Good)\n" +
					"   4. Used (Acceptable)\n" +
					"   P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}

			flag_cond = true;

			switch (choice) {
				case '1':
					condition = Condition.NEW;
					break;
				case '2':
					condition = Condition.LIKE_NEW;
					break;
				case '3':
					condition = Condition.GOOD;
					break;
				case '4':
					condition = Condition.ACCEPTABLE;
					break;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.");
					flag_cond = false;
					continue;
			}
		}while(!flag_cond);

		try {
			System.out.println("---- Description of the item (one line): ");
			description = scanner.nextLine();
			System.out.println("---- Buy-It-Now price: ");

			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Invalid input is entered. Please enter Buy-It-Now price: ");
			}

			price = scanner.nextInt();
			scanner.nextLine();
/*
			System.out.print("---- Bid closing date and time (YYYY-MM-DD HH:MM): ");
			// you may assume users always enter valid date/time
-
			String date = scanner.nextLine();  // "2023-03-04 11:30";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
*/
		}catch (Exception e) {
			System.out.println("Error: Invalid input is entered. Going back to the previous menu.");
			return false;
		}
try {
		/* TODO: Your code should come here to store the user inputs in your database */
    String insertsql = "insert into item (category, description, condition, buy_it_now, seller) values (?,?,?,?,?)";
    PreparedStatement pstmt=null;
    pstmt=conn.prepareStatement(insertsql);
    pstmt.setString(1, Category.getCategory(category));
    pstmt.setString(2, description);
    pstmt.setString(3, Condition.getCondition(condition));
    pstmt.setInt(4, price);
    pstmt.setString(5, username);
    int res=pstmt.executeUpdate();
    
    	if (res>0) {
			System.out.println("Your item has been successfully listed.\n");
		  return true;
      } else {
			System.out.println("Failed to list your item.\n");
			return false;
			}
      } catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Please select again.");
			return false;
		} catch (SQLException e) {
		System.out.println("SQL Exception!!!! : "+e.getMessage());
		return false;
		}
	}
 
  private static boolean PasswordMenu() {
  char samepass='0';
  String userpass=null;
  String confirmpass;
  System.out.print("----< Change Password >\n" + 
				" ** To go back, enter 'back' in user ID.\n" +
				"---- user id: ");
        String user_id=scanner.next();
        scanner.nextLine();
        if(user_id.equalsIgnoreCase("back")){
				return false;
			}
      try {
      String selectsql="select * from worker where id=?";
      PreparedStatement pstmt=conn.prepareStatement(selectsql);
      pstmt.setString(1, user_id);
      ResultSet rset=pstmt.executeQuery();
      if (!rset.next()) {
      System.out.println("Not registered.");
      return false;
      }
      
      
      }catch (SQLException e) {System.out.println(e);}
      
      String realoldpass=null;
      String useroldpass=null;
      System.out.print("---- enter old password: ");
      useroldpass=scanner.next();
      scanner.nextLine();
      try{
      String selectsql="select password from worker where id=?";
      PreparedStatement pstmt=conn.prepareStatement(selectsql);
      pstmt.setString(1, user_id);
      ResultSet rset=pstmt.executeQuery();
      if (rset.next()) {realoldpass=rset.getString(1);} else {
      System.out.println("Not registered\n");
      return false;}
      } catch (SQLException e) {System.out.println("\nSQLException : "+e);}
      if (!realoldpass.equals(useroldpass)) {
      System.out.println("Wrong password. Try agian.\n");
      return false;
      }
      
      
      while (samepass!='1') {
			System.out.print("---- new password: ");
			userpass = scanner.next();
			scanner.nextLine();
			System.out.print("---- password confirm: ");
			confirmpass=scanner.next();
			if (userpass.equals(confirmpass)) {samepass='1';}
			else {System.out.println("Password doesn't match. Please try again.");}
      try {
      String updatesql="update worker set password=? where id=?";
      PreparedStatement pstmt=conn.prepareStatement(updatesql);
      pstmt.setString(1, userpass);
      pstmt.setString(2, user_id);
      pstmt.executeUpdate();
      } catch (SQLException e) {System.out.println("\nSQLException : "+e);}
  }
  System.out.println("Succeeded to change your password.");
  System.out.println();
  return true;
  }

	private static boolean SignupMenu() {
		/* 2. Sign Up */
		String new_id, confirmpass, new_name, isAdmin_temp;
		String userpass=null;
		char samepass='0';
		System.out.print("----< Sign Up >\n" + 
				" ** To go back, enter 'back' in user ID.\n" +
				"---- user id: ");
		try {
			new_id = scanner.next();
			scanner.nextLine();
			if(new_id.equalsIgnoreCase("back")){
				return false;
			}
			while (samepass!='1') {
			System.out.print("---- password: ");
			userpass = scanner.next();
			scanner.nextLine();
			System.out.print("---- password confirm: ");
			confirmpass=scanner.next();
			if (userpass.equals(confirmpass)) {samepass='1';}
			else {System.out.println("Password doesn't match. Please try again.");}
			}
      //name
      System.out.print("---- user name: ");
      new_name = scanner.next();
      scanner.nextLine();
      //isAdmin
			System.out.print("---- Are you an administrator? (Y/N): ");
			isAdmin_temp = scanner.next();
			scanner.nextLine();
			isAdmin_temp.toUpperCase();
			if (!isAdmin_temp.equalsIgnoreCase("Y") && !isAdmin_temp.equalsIgnoreCase("N")) {
				System.out.println("Error: Invalid input is entered. Going back to main menu.");
				return false;
			}
      if (isAdmin_temp.equalsIgnoreCase("Y")) {
      String adminCode;
      System.out.print("---- admin code: ");
      adminCode=scanner.next();
      scanner.nextLine();
      if (!adminCode.equals("admin")) {
      System.out.println("Error: Wrong admin code. Going back to main menu.");
      return false;
      }
      }
			boolean isAdmin= isAdmin_temp.equalsIgnoreCase("Y");

			String insertsql = "insert into worker (id, password, name, rank, isAdmin) values (?, ?, ?, ?, ?)";
			PreparedStatement pstmt=null;

			pstmt=conn.prepareStatement(insertsql);
			pstmt.setString(1,new_id);
			pstmt.setString(2, userpass);
      pstmt.setString(3, new_name);
      pstmt.setString(4, "private");
			pstmt.setBoolean(5, isAdmin);
			int res=pstmt.executeUpdate();
			if (res>0) {
			System.out.println("Your account has been successfully created.\n");
			return true;} else {
			System.out.println("Account creation failed.");
			return false;
			}
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Please select again.");
			return false;
		} catch (SQLException e) {
		System.out.println("SQL Exception!!!! : "+e.getMessage());
		return false;
		}
	}
		/* TODO: Your code should come here to create a user account in your database */
  
  
  private static void WorkerMenu(){
    char choice;
    PreparedStatement pstmt;
    Statement stmt=null;
    ResultSet rset;
    
    do {
      System.out.println(
					"\n----< Work Menu > \n" +
					"    (1) Check Unassigned Call / Get your Job \n" +
					"    (2) Change Status / Check your Duty \n" +
					"    (Q) Quit"
					);
       try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			} if (choice=='1') {
				System.out.println("----< Unassigned Duties>");
        System.out.println(" #  |    datetime     |  problem description  |  sn  | requester ID");
        System.out.println("-------------------------------------------------------------------");
				try{
				String selectsql="select * from disorder where disorder.id not in (select disorder_id from fix)";
				stmt=conn.createStatement();
				rset=stmt.executeQuery(selectsql);
        
				if (rset.next()) {
          do {
              System.out.printf("%-3d | %-16s | %-21s | %-4s | %s\n",
              rset.getInt("id"),
              rset.getString("datetime").substring(0,16),
              rset.getString("problem"),
              rset.getString("sn"),
              rset.getInt("requester_id")
              ); //테이블 포맷팅
            } while (rset.next());
        } else {
        System.out.println("No data to display.");
        continue;}
				} catch (SQLException e) {
					System.out.println("SQLException : "+e);
				}
        String choice_id="";
        System.out.println("Enter Q to exit.");
        System.out.print("Or Enter disorder id you'd like to fix : ");
        choice_id=scanner.next();
        scanner.nextLine();
        if (choice_id.equalsIgnoreCase("Q")) {continue;}
        try {
        //fix 테이블에 추가
        String insertsql="insert into fix (worker_id, disorder_id, start, finish) values (?, ?, current_timestamp, null)";
        pstmt=conn.prepareStatement(insertsql);
        pstmt.setString(1, username);
        pstmt.setInt(2, Integer.parseInt(choice_id));
        pstmt.executeUpdate();
        } catch (SQLException e) {System.out.println("SQLException : "+e);}
        System.out.println("Done\n");
			}

      else if (choice=='2') {
        System.out.println("----< Your duties >");
        System.out.println("disorder # | requester # | equipment # |  problem description  | start_datetime");
        System.out.println("-------------------------------------------------------------------------------");
        try {
          String selectsql="select * from fix f join disorder d on (f.disorder_id=d.id) where worker_id=? and finish is null order by start";
          pstmt=conn.prepareStatement(selectsql);
          pstmt.setString(1, username);
          rset=pstmt.executeQuery();
          
          if (rset.next()) {
          //결과 테이블 출력
          do {
            System.out.printf("%-10d | %-11d | %-11s | %-21s | %s\n",
            rset.getInt("disorder_id"),
            rset.getInt("requester_id"),
            rset.getString("sn"),
            rset.getString("problem"),
            rset.getString("start").substring(0,16)
            );
          } while (rset.next());
          } else {
          System.out.println("No data to display.");
          continue;}
        } catch (SQLException e) {System.out.println("SQLException : "+e);}
        
        String choice_id="";
        System.out.println("Enter Q to exit.");
        System.out.print("Or Enter disorder id to finish your duty : ");
        choice_id=scanner.next();
        scanner.nextLine();
        
        if (choice_id.equalsIgnoreCase("Q")) {continue;}
        
        try {
          String updatesql="update fix set finish=current_timestamp where disorder_id=?::int";
          pstmt=conn.prepareStatement(updatesql);
          pstmt.setString(1, choice_id);
          pstmt.executeUpdate();
        } catch (SQLException e) {System.out.println("SQLException : "+e);}
        System.out.println();
      }

      else if (choice=='Q' || choice=='q') {
        System.out.println("Good Bye~^^");
        try {conn.close();} catch (SQLException e) {System.out.println("SQLException : "+e);}
        System.exit(1);
      } else {
        System.out.println("Error: Invalid input is entered. Try again.");
				continue;
      }      
    } while (true);
  
  
  }

	private static boolean AdminMenu() {
		/* 3. Login as Administrator */
		char choice;
		String adminname, adminpass;
		String keyword, seller;
    PreparedStatement pstmt;
    Statement stmt=null;
    ResultSet rset;
		System.out.println("----< Login as Administrator >");
		try {
			while (true) {
			
			System.out.print(" ** To go back, enter 'back' in user ID.\n" +
				"---- admin ID: ");
			adminname = scanner.next();
			scanner.nextLine();
			if(adminname.equalsIgnoreCase("back")){
				return false;
			}
			System.out.print("---- password: ");
			adminpass = scanner.nextLine();
			// TODO: check the admin's account and password.
			String sql="select * from worker where id = ? and isAdmin = true";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,adminname);
			ResultSet rs=pstmt.executeQuery();

			if (!rs.next()) {
				System.out.println("\nError: Authentication failed. Try again.");
        System.out.println("Note : If you cannot either authenticate in user mode, you are probably not signed up, or the password might be incorrect.");
				continue;
			}
			else if (!rs.getString("password").equals(adminpass)) {
				System.out.println("Error: Incorrect password. Try again.");
				continue;
			}
			break;
		}
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.");
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		

		boolean login_success = true;

		if(!login_success){
			// login failed. go back to the previous menu.
			return false;
		}

		do {
			System.out.println(
					"----< Admin menu > \n" +
					"    1. Print Sold Items per Category \n" +
					"    2. Print Account Balance for Seller \n" +
					"    3. Print Seller Ranking \n" +
					"    4. Print Buyer Ranking \n" +
          "    ----------------------------------- \n" +
          "    5. Assign New Disorder\n"+
          "    6. Print all Disorder, Fix Info\n"+
          "    7. Print Worker/Serial Ranking \n"+
          "    ----------------------------------- \n"+
          "    8. Change User Information \n"+
					"    P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}

			if (choice == '1') {
      String[] array={"Electronics", "Books", "Home", "Sporting Goods", "Souvenir", "Others"};
        do {
        System.out.println("----Note : Invalid input will make you type again.");
        System.out.println("----Valid inputs are {Electronics, Books, Home, Sporting Goods, Souvenir, Others}");
				System.out.print("----Enter Category to search : ");
				keyword = scanner.next();
				scanner.nextLine();
        } while (!Arrays.asList(array).contains(keyword));
        
        try {
				/*TODO: Print Sold Items per Category */
				System.out.println("sold item (id) |     sold date    |   seller ID  |   buyer ID   |  price  | commissions");
				System.out.println("---------------------------------------------------------------------------------------");
				String selectsql="select item.id, purchase_date, billing.seller, billing.buyer, buyer_pay, floor(buyer_pay*0.1) as commission from item join billing on item.id=billing.item_id where item.category=?";
        pstmt=conn.prepareStatement(selectsql);
        pstmt.setString(1, keyword);
        rset=pstmt.executeQuery();
        
				   if (rset.next()) {
    do {
        String purchaseDateFormatted = rset.getString("purchase_date").substring(0, 16);

        // 표 형식으로 출력
        System.out.printf("%-14d | %-16s | %-12s | %-12s | %-7d | %-10d%n",
            rset.getInt("id"),
            purchaseDateFormatted,
            rset.getString("seller"),
            rset.getString("buyer"),
            rset.getInt("buyer_pay"),
            rset.getInt("commission"));
    } while (rset.next());
    System.out.println();
} else {
    System.out.println("No data to display.");
}

				 
            } catch (SQLException e) {
            System.out.println("SQLException : "+e);
            }
				continue;
			} else if (choice == '2') {
				/*TODO: Print Account Balance for Seller */
				System.out.println("---- Enter Seller ID to search : ");
				seller = scanner.next();
				scanner.nextLine();
        
        try{
				System.out.println("sold item(id) |    sold date     |   buyer ID   |  price  | commissions");
				System.out.println("-----------------------------------------------------------------------");
				
        String selectsql="select item_id, purchase_date,buyer, buyer_pay, floor(buyer_pay*0.1) as commission from billing where seller=?";
        pstmt=conn.prepareStatement(selectsql);
        pstmt.setString(1, seller);
        rset=pstmt.executeQuery();
        
				   if (rset.next()) {
    do {
        // purchase_date는 날짜 및 시간 형식으로 출력 (16자까지)
        String purchaseDateFormatted = rset.getString("purchase_date").substring(0, 16);

        // 표 형식으로 출력
        System.out.printf("%-13d | %-16s | %-12s | %-7d | %-10d%n",
            rset.getInt("item_id"),
            purchaseDateFormatted,
            rset.getString("buyer"),
            rset.getInt("buyer_pay"),
            rset.getInt("commission"));
    } while (rset.next());
    System.out.println();
} else {
    System.out.println("No data to display.");
}
				 
            } catch (SQLException e) {
            System.out.println("SQLException : "+e);
            }
				continue;
			} else if (choice == '3') {
      try {
				/* TODO: Print Seller Ranking */
				System.out.println("seller ID    | # of items sold | Total Profit (excluding commissions)");
				System.out.println("---------------------------------------------------------------------");
				stmt=conn.createStatement();
        rset=stmt.executeQuery("select seller, count(item_id) as cnt, sum(buyer_pay)-sum(floor(buyer_pay*0.1)) as profit from billing group by seller order by cnt desc, profit desc");
				   if (rset.next()) {
    do {
        // 표 형식으로 출력
        System.out.printf("%-12s | %-15d | %-10d%n",
            rset.getString("seller"),
            rset.getInt("cnt"),
            rset.getInt("profit"));
    } while (rset.next());
    System.out.println();
} else {
    System.out.println("No data to display.");
}
     } catch (SQLException e) {
     System.out.println("SQLException : "+e);
     }
				continue;
			} else if (choice == '4') {
				/*TODO: Print Buyer Ranking */
				System.out.println("buyer ID     | # of items purchased | Total Money Spent ");
				System.out.println("------------------------------------------------------");
				try{
        stmt=conn.createStatement();
        rset=stmt.executeQuery("select buyer, count(item_id) as cnt, sum(buyer_pay) as spent from billing group by buyer order by cnt desc, spent desc");
				   if (rset.next()) {
    do {
        // 표 형식으로 출력
        System.out.printf("%-12s | %-20d | %-10d%n",
            rset.getString("buyer"),
            rset.getInt("cnt"),
            rset.getInt("spent"));
    } while (rset.next());
    System.out.println();
} else {
    System.out.println("No data to display.");
}
				 } catch (SQLException e) {
           System.out.println("SQLException : "+e);
         }
				continue;
			} else if (choice=='5') {
      String choice_id;
      String userInput="";
      String datetime;
      String sn;
      String problem;
      System.out.println("----Insert one of name, rank, phone, department information of requester.");
      System.out.print("Your input (Case-sensitive) : ");
      userInput = scanner.next();
			scanner.nextLine();
      String selectsql;
      try {
      selectsql="select * from member where rank like ? or name like ? or phone like ? or department like ?";
      pstmt=conn.prepareStatement(selectsql);
      pstmt.setString(1, "%"+userInput+"%");
      pstmt.setString(2, "%"+userInput+"%");
      pstmt.setString(3, "%"+userInput+"%");
      pstmt.setString(4, "%"+userInput+"%");
      rset=pstmt.executeQuery();
      System.out.println("member ID |    rank    |    name    |    phone    | department ");
      System.out.println("-------------------------------------------------------- ");
      if (rset.next()) {
      do {
        System.out.printf("%-9d | %-10s | %-10s | %-11s | %s\n",
          rset.getInt("id"),
          rset.getString("rank"),
          rset.getString("name"),
          rset.getString("phone"),
          rset.getString("department")
        );
      } while (rset.next());
      } else {
      System.out.println("No data to display.");
      } 
      } catch (SQLException e) {
      System.out.println("SQLException : "+e);
      }
      System.out.print("---- Requester's member ID : ");
      choice_id=scanner.next();
      scanner.nextLine();
      
      System.out.println("---- Note : enter 'now' to save with current time");
      System.out.print("---- Enter date posted (YYYY-MM-DD HH:MM) : ");
      datetime=scanner.next();
      scanner.nextLine();
      
      System.out.print("---- Write short description : ");
      problem=scanner.nextLine();
      
      System.out.print("---- Write serial # of equipment : ");
      sn=scanner.next();
      scanner.nextLine();
      
      //insert into disorder
      try {
        String insertsql="insert into disorder (requester_id, sn, datetime, problem) values (?,?,?,?)";
        pstmt=conn.prepareStatement(insertsql);
        pstmt.setInt(1, Integer.parseInt(choice_id));
        pstmt.setString(2, sn);
        if ("now".equalsIgnoreCase(datetime)) {
          pstmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
        } else {pstmt.setString(3, datetime);}
        pstmt.setString(4, problem);
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.out.println("SQLException : "+e);
      }  
      System.out.println("Done\n");
      
      
      } else if (choice=='6') { 
        String startdate,enddate;
        System.out.print("Enter start date (YYYY-MM-DD) : ");
        startdate=scanner.next();
        scanner.nextLine();
        startdate=startdate+" 00:00:00";
        
        System.out.print("Enter end date (YYYY-MM-DD) : ");
        enddate=scanner.next();
        scanner.nextLine();
        enddate=enddate+" 23:59:59.99";
        
        System.out.println("disorder # |     datetime     |  problem description  |  sn  | requester ID | worker |    start_time    | finish_time");
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        try {
          String selectsql="select * from disorder d left join fix f on (d.id=f.disorder_id) where datetime>=?::timestamptz and datetime<=?::timestamptz order by id";
          pstmt=conn.prepareStatement(selectsql);
          pstmt.setString(1, startdate);
          pstmt.setString(2, enddate);
          rset=pstmt.executeQuery();
          
          if (rset.next()) {
            do {
              System.out.printf("%-10d | %-16s | %-21s | %-4s | %-12d | %-12s | %-16s | %s\n",
              rset.getInt("id"),
              rset.getString("datetime").substring(0,16),
              rset.getString("problem"),
              rset.getString("sn"),
              rset.getInt("requester_id"),
              (rset.getString("worker_id") != null) ? rset.getString("worker_id") : "null",
              (rset.getString("start") != null) ? rset.getString("start").substring(0, 16) : " null ",
              (rset.getString("finish") != null) ? rset.getString("finish").substring(0, 16) : "      null      "
              ); //테이블 포맷팅
            } while (rset.next());
          } else {System.out.println("No data to display");}
          System.out.println();
        } catch (SQLException e) {System.out.println("SQLException : "+e);}
      } else if (choice=='7') {
      //ranking
      System.out.println("<Worker Ranking>");
      System.out.println("     id      |    name     | # of equipment fixed | spent time");
      System.out.println("--------------------------------------------------------------");
      try {
      stmt=conn.createStatement();
      rset=stmt.executeQuery("with complete(worker_id, disorder_id, start, finish) as (select * from fix where finish is not null) select worker_id, name, count(disorder_id) as cnt, sum(finish-start) as spent_time from complete join worker on complete.worker_id=worker.id group by (complete.worker_id, worker.name) order by cnt desc, spent_time desc");
      
      while (rset.next()) {
      System.out.printf("%-12s | %-12s | %-20d | %s\n",
      rset.getString("worker_id"),
      rset.getString("name"),
      rset.getInt("cnt"),
      rset.getString("spent_time")
      );
      }
      System.out.println();
      } catch (SQLException e) {System.out.println("\nSQLException : "+e);}
      
      System.out.println("<Frequently Malfunctioning Equipment>");
      System.out.println("serial # |   type   |   model   | count");
      System.out.println("---------------------------------------");
      try {
      stmt=conn.createStatement();
      rset=stmt.executeQuery("select sn, type, model, count(disorder.id) as cnt from equipment natural join disorder group by sn, type, model having count(disorder.id)>=2 order by cnt desc");
      
      while (rset.next()) {
        System.out.printf("%-8s | %-8s | %-9s | %d\n",
        rset.getString("sn"),
        rset.getString("type"),
        rset.getString("model"),
        rset.getInt("cnt")
        );
      }
      } catch (SQLException e) {System.out.println("\nSQLException : "+e);}
      System.out.println();
      }
      
      
      else if (choice=='8') {
        System.out.println("   worker id |     name     |    rank    | isAdmin");
        System.out.println("-------------------------------------");
        ArrayList<String> idList=new ArrayList<>();
        try {
          String selectsql="select * from worker";
          stmt=conn.createStatement();
          rset= stmt.executeQuery(selectsql);
          
          while (rset.next()) {
          idList.add(rset.getString("id"));
            System.out.printf("%-12s | %-12s | %-10s | %b\n",
            rset.getString("id"),
            rset.getString("name"),
            rset.getString("rank"),
            rset.getBoolean("isAdmin")
            );
          }
        } catch (SQLException e) {System.out.println("SQLException : "+e);}
        String choice_id="";
          System.out.print("Enter worker id you'd like to change info : ");
          choice_id=scanner.next();
          scanner.nextLine();
          if (!idList.contains(choice_id)) {
            System.out.println("Error : invalid input");
            continue;
          }
          System.out.println("Note : You can only change rank/isAdmin");
          String rank;
          String isAdmin="";
          System.out.print("Enter rank : ");
          rank=scanner.nextLine();
          do {
          System.out.print("Enter isAdmin [y/n] : ");
          isAdmin=scanner.next();
          scanner.nextLine();
          } while ((!isAdmin.equalsIgnoreCase("Y")) && (!isAdmin.equalsIgnoreCase("N")));
          
          try {
          String updatesql="update worker set rank=?, isAdmin=? where id=?";
          pstmt=conn.prepareStatement(updatesql);
          pstmt.setString(1, rank);
          pstmt.setBoolean(2, isAdmin.equalsIgnoreCase("y") ? true : false);
          pstmt.setString(3, choice_id);
          pstmt.executeUpdate();
          } catch (SQLException e) {System.out.println("SQLException : "+e);}
          System.out.println("Successfully changed info\n");
          
          
      } 
      
      else if (choice == 'P' || choice == 'p') {
				return false;
			} else {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}
		} while(true);
	}

	public static void CheckSellStatus(){
		/* TODO: Check the status of the item the current user is selling */
try {
		System.out.println("item listed in Auction    |  1st bidder  | highest price | bidding date/time | end at");
		System.out.println("-------------------------------------------------------------------------------------");
    String selectsql="with max_bid(item_id, bidder, price, posted,close) as "+
                                        "(select distinct on (item_id) item_id, bidder, price, posted, close from bid order by item_id, price desc) "+
                                        "select description, bidder, price, max_bid.posted as bid_posted, close from item left join max_bid on item.id=max_bid.item_id "+
                                        "where (item.seller=?) and (item.status='Open')";
    PreparedStatement pstmt = conn.prepareStatement(selectsql);
    pstmt.setString(1, username);
    ResultSet rset = pstmt.executeQuery();
    
		   if (rset.next()) {
       do {
       //null일 경우 대비
       String bidder=rset.getString("bidder")!=null ? rset.getString("bidder") : "no bid";
       int price=rset.getInt("price");
       price=rset.wasNull() ? 0 : price;
       String bid_posted=rset.getString("bid_posted")!=null ? rset.getString("bid_posted").substring(0,16) : "no bid";
       String close=rset.getString("close")!=null ? rset.getString("close").substring(0,16) : "no bid";
        // 표 형식으로 출력
        System.out.printf("%-25s | %-12s | %-13d | %-17s | %-16s%n",
            rset.getString("description"),
            bidder,
            price,
            bid_posted,
            close
            );
    } while (rset.next());
          System.out.println();
		   } else {System.out.println("No data to display.");}
      } catch (SQLException e) {
      e.printStackTrace();
      }
	}

	public static boolean BuyItem(){
    ArrayList<String> idList=new ArrayList<>(); //item id 담을 배열
		Category category=null;
		Condition condition=null;
		char choice;
    String choice_id;
    String close="";
		int price;
    int buyItNow=0;
    int highest=0;
		String keyword,datePosted;
    String seller=null;
    ResultSet rset=null;
		boolean flag_catg = true, flag_cond = true;
		
		do {

			System.out.println( "----< Select category > : \n" +
					"    1. Electronics\n"+
					"    2. Books\n" + 
					"    3. Home\n" + 
					"    4. Sporting Goods\n" +
					"    5. Souvenir\n" +
					"    6. Other categories\n" +
          "    7. Any category\n" +
					"    P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				return false;
			}

			flag_catg = true;

			switch (choice) {
				case '1':
					category = Category.ELECTRONICS;
					break;
				case '2':
					category = Category.BOOKS;
					break;
				case '3':
					category = Category.HOME;
					break;
				case '4':
          category = Category.SPORTINGGOODS;
					break;
				case '5':
					category = Category.SOUVENIR;
					break;
				case '6':
					category = Category.OTHERS;
					break;
				case '7':
					break;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.");
					flag_catg = false;
					continue;
			}
		} while(!flag_catg);

		do {

			System.out.println(
					"----< Select the condition > \n" +
					"   1. New\n" +
					"   2. Like-new\n" +
					"   3. Used (Good)\n" +
					"   4. Used (Acceptable)\n" +
					"   P. Go Back to Previous Menu"
					);
			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				return false;
			}

			flag_cond = true;

			switch (choice) {
				case '1':
					condition = Condition.NEW;
					break;
				case '2':
					condition = Condition.LIKE_NEW;
					break;
				case '3':
					condition = Condition.GOOD;
					break;
				case '4':
					condition = Condition.ACCEPTABLE;
					break;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.");
					flag_cond = false;
					continue;
				}
		} while(!flag_cond);

		try {
			System.out.println("---- Enter keyword to search the description (case-sensitive): ");
			keyword = scanner.next();
			scanner.nextLine();

			System.out.println("---- Enter Seller ID to search : ");
			System.out.println(" ** Enter 'any' if you want to see items from any seller. ");
			seller = scanner.next();
			scanner.nextLine();

			System.out.println("---- Enter date posted (YYYY-MM-DD): ");
			System.out.println(" ** This will search items that have been posted after the designated date.");
			datePosted = scanner.next();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.");
			return false;
		} 
    try {
		/* TODO: Query condition: item category */
		/* TODO: Query condition: item condition */
		/* TODO: Query condition: items whose description match the keyword (use LIKE operator) */
		/* TODO: Query condition: items from a particular seller */
		/* TODO: Query condition: posted date of item */
    PreparedStatement pstmt=conn.prepareStatement("with highest_bid(item_id, current_bid, highest_bidder, close) as "+
                                                  "(select distinct on (item_id) item_id, price, bidder, close from bid order by item_id, price desc) "+
                                                  "select id, description, condition, seller, buy_it_now, current_bid, highest_bidder, extract(day from close-current_timestamp)||' day '||extract(hour from close-current_timestamp)||' hrs' as time_left,close at time zone 'Asia/Seoul' as close from item left join highest_bid on highest_bid.item_id=item.id"+
                                                  " where (category=? or 1=?) and (condition=?) and (description like ?) and (seller=? or 1=?) and (seller<>?) and (posted > ?::timestamptz) and (item.status='Open') and (close>current_timestamp or close is null)"
    );
    pstmt.setString(1,Category.getCategory(category));
    pstmt.setInt(2, (category==null) ? 1 : 0);
    pstmt.setString(3, Condition.getCondition(condition));
    pstmt.setString(4, "%"+keyword+"%");
    pstmt.setString(5, seller);
    pstmt.setInt(6, (seller.equals("any")) ? 1:0);
    pstmt.setString(7, username);
    Timestamp mytimestamp=Timestamp.valueOf(datePosted+" 23:59:59.999");
    pstmt.setTimestamp(8, mytimestamp);
    //print the complete statement
    //******************************************************Only for Debugging*****************************
    rset=pstmt.executeQuery();
    //String executedQuery = rset.getStatement().toString();
    //System.out.println(executedQuery);
 
		/* TODO: List all items that match the query condition */
		System.out.println("Item ID |      Item description     |  Condition |    Seller    | Buy-It-Now | Current Bid | highest bidder |   Time left  | bid close");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
		 
		   if (rset.next()){
          do {
    System.out.printf("%-7s | %-25s | %-10s | %-12s | %-10s | %-11s | %-14s | %-12s | %-16s%n",
        rset.getString("id"),
        rset.getString("description"),
        rset.getString("condition"),
        rset.getString("seller"),
        rset.getString("buy_it_now"),
        rset.getString("current_bid"),
        rset.getString("highest_bidder"),
        rset.getString("time_left"),
        (rset.getString("close") != null) ? rset.getString("close").substring(0, 16) : "no bid"
        );
    idList.add(rset.getString("id"));
} while (rset.next());
          System.out.println();
		   } else {
       System.out.println("No data to display\n");
       return false;}
    } catch (SQLException e) {
    	System.out.println("SQLException : "+e);
    }
          
		 

		System.out.println("---- Select Item ID to buy or bid: ");
   choice_id = scanner.next();
   scanner.nextLine();
   
   if (!idList.contains(choice_id)) {
     System.out.println("Error : Invalid id is given.");
     return false;
   }
   
do {
		try {
			
			System.out.println("    Note : If you bid cheaper than the current highest price, bidding will fail and you'll have to enter price again.");
			System.out.print("     Price: ");
			price = scanner.nextInt();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.");
			return false;
		}
   //여기서 item id 조건 주고 select
   try {
     String selectsql="with highest_bid(id, price, close) as (select distinct on (item_id) item_id, price, close from bid order by item_id, price desc) select * from item left join highest_bid on highest_bid.id=item.id where item.id=?";
     PreparedStatement pstmt=conn.prepareStatement(selectsql);
     pstmt.setInt(1, Integer.parseInt(choice_id));
     rset=pstmt.executeQuery();
     rset.next();
     buyItNow=rset.getInt("buy_it_now");
     seller=rset.getString("seller");
     highest=rset.getInt("price");
     highest=rset.wasNull() ? 0: highest;
     close=rset.getString("close")!=null ? rset.getString("close"):"null";
   } catch (SQLException e) {
     System.out.println("SQLException : "+e);
     return false;
   }
   } while (price<=highest);
   
   //현재 시각, close 비교
   if (!close.equals("null")) {
   try {
     String selectsql="select current_timestamp > ?::timestamptz as error_condition";
     PreparedStatement pstmt=conn.prepareStatement(selectsql);
     pstmt.setString(1, close);
     rset=pstmt.executeQuery();
     
     rset.next();
     if (rset.getBoolean("error_condition")==true) {
       System.out.println("Bid Ended.\n");
       return false;
     }
   } catch (SQLException e) {
     System.out.println("SQLException : "+e);
   }
   }
   
		/* TODO: Buy-it-now or bid: If the entered price is higher or equal to Buy-It-Now price, the bid ends. */
		/* Even if the bid price is higher than the Buy-It-Now price, the buyer pays the B-I-N price. */
   if (price >= buyItNow) {
     try{
     conn.setAutoCommit(false);
     //insert into bid
     String insertsql="insert into bid(posted, price, bidder, item_id, close) values (current_timestamp, ?, ?, ?, current_timestamp)";
     PreparedStatement pstmt=conn.prepareStatement(insertsql);
     pstmt.setInt(1, buyItNow);
     pstmt.setString(2, username);
     pstmt.setInt(3, Integer.parseInt(choice_id));
     pstmt.executeUpdate();
     
     //change the item's status (Open -> Ended)
     String updatesql="update item set status='Ended' where id=?";
     pstmt=conn.prepareStatement(updatesql);
     pstmt.setInt(1, Integer.parseInt(choice_id));
     pstmt.executeUpdate();
     
     //make billing info
     insertsql="insert into billing (buyer_pay, seller, buyer, status, item_id) values (?,?,?,'Done',?)";
     pstmt=conn.prepareStatement(insertsql);
     pstmt.setInt(1, buyItNow);
     pstmt.setString(2, seller);
     pstmt.setString(3, username);
     pstmt.setInt(4, Integer.parseInt(choice_id));
     pstmt.executeUpdate();    
     
     conn.commit();
     conn.setAutoCommit(true);
     } catch (SQLException e) {
     try {conn.rollback();} catch (SQLException sqle) {
     System.out.println("Couldn't roll back.");
     System.out.println(sqle);}
     e.printStackTrace();
     System.out.println("SQLException : "+e);
     } finally {
     try {conn.setAutoCommit(true);} catch (SQLException sqlex) {System.out.println("\nSQLException");}
     }
     
     /* TODO: if you won, print the following */
		System.out.println("Congratulations, the item is yours now.\n");
   return true;
   } 
   else {
   try {
     //insert into bid
     String insertsql="insert into bid(posted, price, bidder, item_id) values (current_timestamp, ?, ?, ?)";
     PreparedStatement pstmt=conn.prepareStatement(insertsql);
     pstmt.setInt(1, price);
     pstmt.setString(2, username);
     pstmt.setInt(3, Integer.parseInt(choice_id));
     pstmt.executeUpdate();
     
   } catch (SQLException e) {
   System.out.println("SQLException : "+e);
   }
                /* TODO: if you are the current highest bidder, print the following */
		System.out.println("Congratulations, you are the highest bidder.\n"); 
		return true;
   }
	}

	public static void CheckBuyStatus(){
    Statement stmt=null;
    PreparedStatement pstmt;
    ResultSet rset=null;
		/* TODO: Check the status of the item the current buyer is bidding on */
		/* Even if you are outbidded or the bid closing date has passed, all the items this user has bidded on must be displayed */
   
   //현재 시간과 close 비교해서 1. 아이템 상태 변경, 2. billing에 데이터 추가 하는 트랜잭션 필요
   try {
     conn.setAutoCommit(false);
     stmt=conn.createStatement();
     stmt.executeUpdate("update item set status='Ended' where id in (select distinct on (item_id) item_id from bid where current_timestamp>close order by item_id, price desc)");
     stmt.executeUpdate("insert into billing (purchase_date, buyer_pay, seller, buyer, item_id) select b.close, b.price, i.seller, b.bidder, b.item_id from bid b join item i on b.item_id=i.id where current_timestamp>b.close and i.status='Ended' and b.price=(select max(b1.price) from bid b1 where b1.item_id =b.item_id) and not exists (select 1 from billing bl where bl.item_id =b.item_id)");
     conn.commit();   
   } catch (SQLException e) {
   System.out.println("SQLException : "+e);
   try {conn.rollback();} catch (SQLException sqle) {System.out.println("SQLException : "+sqle);}
   }
   finally {try {conn.setAutoCommit(true);} catch (SQLException e) {System.out.println("SQLException : "+e);}}
   
    try {
    String selectsql="with highest_bidder(item_id, highest_bidder, highest_price, close) as "+
                            "(select distinct on (item_id) item_id, bidder, price, close from bid order by item_id, price desc), "+
                            "my_max(item_id, price) as (select distinct on (item_id) item_id, price from bid where bidder=? order by item_id, price desc) "+
                            "select hb.item_id, item.description, hb.highest_bidder, hb.highest_price, mm.price as price, close from (highest_bidder hb natural join my_max mm) join item on hb.item_id=item.id order by close desc";
    pstmt=conn.prepareStatement(selectsql);
    pstmt.setString(1, username);
    rset=pstmt.executeQuery();
    
    //bid 테이블에서 item_id, my bidding price
    //item 테이블에서 item_id, description
    //임시 테이블에서 item_id, highest bidder, highest bidding price, bid closing date/time
		System.out.println("item ID   |     item description      |  1st bidder  | highest bid | your bid | bid closing date/time");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
   
		
	   while(rset.next()){
        // 표 형식으로 출력
        System.out.printf("%-9s | %-25s | %-12s | %-11d | %-8d | %-16s%n",
            rset.getString("item_id"),
            rset.getString("description"),
            rset.getString("highest_bidder"),
            rset.getInt("highest_price"),
            rset.getInt("price"),
            rset.getString("close").substring(0,16));

       }
       System.out.println();
    } catch (SQLException e) {
      System.out.println("SQLException : "+e);
    }
	}

	public static void CheckAccount(){
   PreparedStatement pstmt;
   ResultSet rset=null;
   Statement stmt=null;
   try {
     conn.setAutoCommit(false);
     stmt=conn.createStatement();
     stmt.executeUpdate("update item set status='Ended' where id in (select distinct on (item_id) item_id from bid where current_timestamp>close order by item_id, price desc)");
     stmt.executeUpdate("insert into billing (purchase_date, buyer_pay, seller, buyer, item_id) select b.close, b.price, i.seller, b.bidder, b.item_id from bid b join item i on b.item_id=i.id where current_timestamp>b.close and i.status='Ended' and b.price=(select max(b1.price) from bid b1 where b1.item_id =b.item_id) and not exists (select 1 from billing bl where bl.item_id =b.item_id)");
     conn.commit();   
   } catch (SQLException e) {
   System.out.println("SQLException : "+e);
   try {conn.rollback();} catch (SQLException sqle) {System.out.println("SQLException : "+sqle);}
   }
   finally {try {conn.setAutoCommit(true);} catch (SQLException e) {System.out.println("SQLException : "+e);}}
   
   
 try {
		/* TODO: Check the balance of the current user.  */
		System.out.println("[Sold Items] \n");
		System.out.println("item category  | item ID |     sold date    | sold price |   buyer ID   | commission  ");
		System.out.println("-------------------------------------------------------------------------------");
   
   String selectsql="select category, item.id as item_id, purchase_date, buyer_pay, buyer, floor(buyer_pay*0.1) as commission from billing join item on billing.item_id=item.id where item.status='Ended' and billing.seller=?";
   pstmt=conn.prepareStatement(selectsql);
   pstmt.setString(1, username);
   rset=pstmt.executeQuery();
    
		   if (rset.next()){
          do {
        // 표 형식으로 출력
        System.out.printf("%-14s | %-7s | %-16s | %-10s | %-12s | %-10s%n",
            rset.getString("category"),
            rset.getString("item_id"),
            rset.getString("purchase_date").substring(0,16),
            rset.getString("buyer_pay"),
            rset.getString("buyer"),
            rset.getString("commission")
            );
    } while (rset.next());
          }
       System.out.println();
		 } catch (SQLException e) {
       System.out.println("SQLException : "+e);
     }
   try {
		System.out.println("[Purchased Items] \n");
		System.out.println("item category  | item ID |  purchased date  | puchased price | seller ID ");
		System.out.println("--------------------------------------------------------------------------");
		
     String selectsql="select category, item.id as item_id, purchase_date, buyer_pay, billing.seller as seller from billing join item on billing.item_id=item.id where item.status='Ended' and billing.buyer=?";
     pstmt=conn.prepareStatement(selectsql);
     pstmt.setString(1, username);
     rset=pstmt.executeQuery();
		  if (rset.next()){
          do {
        // 표 형식으로 출력
        System.out.printf("%-14s | %-7s | %-16s | %-14s | %-12s%n",
            rset.getString("category"),
            rset.getString("item_id"),
            rset.getString("purchase_date").substring(0,16),
            rset.getString("buyer_pay"),
            rset.getString("seller")
            );
    } while (rset.next());
          }
       System.out.println();
      } catch (SQLException e) {
       System.out.println("SQLException : "+e);
     }
	}

	
	public static void main(String[] args) {
		char choice;
		boolean ret;
		Statement stmt=null;

		if(args.length<2){
			System.out.println("Usage: java Auction postgres_id password");
			System.exit(1);
		}


		try{
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/"+args[0], args[0], args[1]); 
	    //    	conn is defined at the very first of the code
            	//conn = DriverManager.getConnection("jdbc:postgresql://localhost/s20313974", "s20313974", "pw"); 
       /*
        String createFunctionSQL = "CREATE OR REPLACE FUNCTION check_bid_time() " +
                                   "RETURNS TRIGGER AS $$ " +
                                   "BEGIN " +
                                   "    IF current_timestamp > NEW.close THEN " +
                                   "        RAISE EXCEPTION 'Error: Bid Ended'; " +
                                   "    END IF; " +
                                   "    RETURN NEW; " +
                                   "END; $$ LANGUAGE 'plpgsql'";
                                   
        String dropTriggerSQL= "drop trigger if exists trigger_check_bid_time on bid;";
                                   
        String createTriggerSQL = "CREATE TRIGGER trigger_check_bid_time " +
                                  "BEFORE INSERT ON bid " +
                                  "FOR EACH ROW " +
                                  "EXECUTE FUNCTION check_bid_time();";
        stmt=conn.createStatement();
        stmt.executeUpdate(createFunctionSQL);
        stmt.executeUpdate(dropTriggerSQL);
        stmt.executeUpdate(createTriggerSQL);
        */
                                  
                                  
		} catch (SQLException e) {
		System.out.println("SQLException : "+e);
		System.exit(1);
		}
   
    

		do {	
			username = null;
			System.out.println(
					"----< Login menu >\n" + 
					"----(1) Login\n" +
					"----(2) Sign up\n" +
					"----(3) Login as Administrator\n" +
          "----(4) Change password\n"+
					"----(Q) Quit"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}

			try {
				switch (choice) {
					case '1':
						ret = LoginMenu();
						if(!ret) continue;
						break;
					case '2':
						ret = SignupMenu();
						if(!ret) continue;
						break;
					case '3':
						ret = AdminMenu();
						if(!ret) continue;
          case '4':
            ret=PasswordMenu();
            if(!ret) continue;
            continue;
					case 'q':
					case 'Q':
						System.out.println("Good Bye");
						/* TODO: close the connection and clean up everything here */
						conn.close();
						System.exit(1);
					default:
						System.out.println("Error: Invalid input is entered. Try again.");
				}
			} catch (SQLException e) {
				System.out.println("SQLException : " + e);	
						System.out.println("Error: Invalid input is entered. Try again.");
				}
		} while (username==null || username.equalsIgnoreCase("back"));  

		// logged in as a normal user 
   System.out.println(
    "---< How can I help you? > :\n" +
    "----(1) Auction\n" +
    "----(2) Work\n" +
    "----(Q) Quit"
);
try {
    choice = scanner.next().charAt(0);
    scanner.nextLine();
} catch (java.util.InputMismatchException e) {
    System.out.println("Error: Invalid input is entered. Try again.");
    // 프로그램의 나머지를 실행하지 않으려면 return이나 exit 사용 가능
    return; // 또는 System.exit(1);
}

try {
    switch (choice) {
        case '1':
            // Auction logic
            break;
        case '2':
            WorkerMenu();
            break;
        case 'q':
        case 'Q':
            System.out.println("Good Bye");
            /* TODO: close the connection and clean up everything here */
            conn.close();
            System.exit(1);
            break;
        default:
            System.out.println("Invalid choice. Please try again.");
    }
} catch (SQLException e) {
    System.out.println("SQLException: " + e);
}
System.out.println();
   
   
		do {
			System.out.println(
					"---< Auction Main menu > :\n" +
					"----(1) Sell Item\n" +
					"----(2) Status of Your Item Listed on Auction\n" +
					"----(3) Buy Item\n" +
					"----(4) Check Status of your Bid \n" +
					"----(5) Check your Account \n" +
					"----(Q) Quit"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}

			try{
				switch (choice) {
					case '1':
						ret = SellMenu();
						if(!ret) continue;
						break;
					case '2':
						CheckSellStatus();
						break;
					case '3':
						ret = BuyItem();
						if(!ret) continue;
						break;
					case '4':
						CheckBuyStatus();
						break;
					case '5':
						CheckAccount();
						break;
					case 'q':
					case 'Q':
						System.out.println("Good Bye");
						/* TODO: close the connection and clean up everything here */
						conn.close();
						System.exit(1);
				}
			} catch (SQLException e) {
				System.out.println("SQLException : " + e);	
				System.exit(1);
			}
		} while(true);
	} // End of main 
} // End of class


