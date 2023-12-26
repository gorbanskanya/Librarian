package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book 
{
   static
   {
      try
      {
         Class.forName("oracle.jdbc.driver.OracleDriver");
      } catch (ClassNotFoundException cnfe)
      {
         cnfe.printStackTrace();
      }
   }

   Connection con;
   PreparedStatement psmt6;
   PreparedStatement psmt7;
   PreparedStatement psmt8;
   PreparedStatement psmt9;
   PreparedStatement psmt10;
   
   Scanner sc = new Scanner(System.in);
   
   public static void main(String[] args) {
      Book smb = new Book(); // 스태틱 용량 줄이기 위함
      smb.BookRun();
   }
   
   public void BookRun()
   {
      connectDatabase();
      int choice;
      while (true)
      {
         showB();
         choice = sc.nextInt();
         sc.nextLine();
         switch (choice)
         {
         case 1:
            allBook();
            break;
         case 2:
            selBook();
            break;
         case 3:
            rentBook();
            break;
         case 4:
            rtnBook();
            break;
         case 5:
            adtdateBook();
            break;
         case 6:
            System.out.println("프로그램을 종료합니다");
            return;
         default:
            System.out.println("잘못입력하셨습니다");
            break;
         }
      }
   }
   
   public void showB()
   {
      System.out.println("[도서 대여 및 반납]");
      System.out.print("1. 도서 전체 조회  ");
      System.out.print("2. 도서 찾기  ");
      System.out.print("3. 도서 대여  ");
      System.out.print("4. 도서 반납  ");
      System.out.print("5. 도서 대여기간 연장  ");
      System.out.println("6. 종료");
   }
   
   public void allBook()
   {
      try
      {
         Connection con = DriverManager.getConnection
               ("jdbc:oracle:thin:@localhost:1521:xe", "Librarian", "1234");
         Statement stmt = con.createStatement();
         
         StringBuffer sb = new StringBuffer();
         sb.append("select * from BookDB");
         
         ResultSet rs = stmt.executeQuery(sb.toString());
         while (rs.next())
         {
            System.out.println();
            System.out.println("도서번호 : " + rs.getString(3));
            System.out.println("도서명 : " + rs.getString(4));
            System.out.print("저자 : " + rs.getString(5));
            System.out.print("출판사 : " + rs.getString(6));
            System.out.print("도서관 소장 권수 : " + rs.getString(7));
            System.out.println("현재 대여 가능 권수 : " + rs.getString(8));
            System.out.println();
         }
         
         rs.close();
         stmt.close();
         con.close();
      }
      catch (SQLException sqle)
      {
         System.out.println("Connection Error");
         sqle.printStackTrace();
      }
   }
   
   public void selBook()
   {
      System.out.print("**1. 도서명으로 조회 2. 저자 이름으로 조회**");
      String selNum = sc.nextLine();
      switch (selNum)
      {
      case "1":
         try
         {
            System.out.println("도서명을 입력하세요. 일부만 일치해도 검색됩니다.");
            String nBook = sc.nextLine();
            psmt6.setString(1, nBook);
            psmt6.setString(2, "%" + nBook + "%");
            ResultSet rs = psmt6.executeQuery();
            
            System.out.println();
            System.out.println("**조회 결과입니다**");
            while (rs.next())
            {
               System.out.println();
               System.out.println("도서번호 : " + rs.getString(3));
               System.out.println("도서명 : " + rs.getString(4));
               System.out.print("저자 : " + rs.getString(5) + " / ");
               System.out.println("출판사 : " + rs.getString(6) + " / ");
               System.out.print("도서관 소장 권수 : " + rs.getInt(7) + " / " );
               System.out.println("현재 대여 가능 권수 : " + rs.getInt(8));
               System.out.println();               
            }
            
            int result6 = rs.getRow();
            if(result6 == 0)
            {
               System.out.println("**존재하지 않는 도서명입니다**");
               System.out.println();
            }
            rs.close();
         }
         catch (Exception e)
         {
            System.out.println("알 수 없는 에러가 발생했습니다.");
         }
         break;
      case "2":
         try
         {
            System.out.println("저자명을 입력하세요. 일부만 일치해도 검색됩니다.");
            String nName = sc.nextLine();
            psmt7.setString(1, nName);
            psmt7.setString(2, "%" + nName + "%");
            ResultSet rs = psmt7.executeQuery();
            
            System.out.println();
            System.out.print("**조회 결과입니다**");
            while (rs.next())
            {
               System.out.println();
               System.out.println("도서번호 : " + rs.getString(3));
               System.out.println("도서명 : " + rs.getString(4));
               System.out.print("저자 : " + rs.getString(5) + " / ");
               System.out.println("출판사 : " + rs.getString(6) + " / ");
               System.out.print("도서관 소장 권수 : " + rs.getInt(7) + " / " );
               System.out.println("현재 대여 가능 권수 : " + rs.getInt(8));
               System.out.println();               
            }
            
            int result7 = rs.getRow();
            if(result7 == 0)
            {
               System.out.println("**존재하지 않는 도서명입니다**");
               System.out.println();
            }
            rs.close();
         }
         catch (Exception e)
         {
            System.out.println("알 수 없는 에러가 발생했습니다.");
         }
      }      
   }
   
   // 도서대여
   public void rentBook()
   {
      System.out.println("대여자의 전화번호를 입력해주세요. ex)010-1111-1111");
      String pNum = sc.next();
      
      if((pNum != "블랙리스트번호")&&(pNum == "회원번호"))
      {
         System.out.println("대여할 도서의 도서번호를 입력해주세요");
         String isbn = sc.next();
         
         if(isbn == "도서번호")
         {
            // 오늘날짜
            long miliseconds = System.currentTimeMillis();
            Date bDate = new Date(miliseconds);
            // Date rDate = new Date(0,0,7) + bDate;
            
            try
            {
               psmt8.setString(1, isbn);
               psmt8.setString(2, isbn);
               psmt8.setString(3, isbn);
               psmt8.setString(4, pNum);
               psmt8.setDate(5, bDate);
               psmt8.setDate(6, bDate);
               psmt8.setInt(7, 0);
               
               int updateCount = psmt8.executeUpdate();
               
               if(updateCount == 1)
               {
                  System.out.println();
                  System.out.println("**대여 완료되었습니다**");
                  System.out.print("대여자 이름 : / ");
                  System.out.print("대여자 전화번호 : " + pNum + " / ");
                  System.out.println("도서번호 : " + isbn);
                  System.out.println("도서명 : ");
                  System.out.print("대여일자 : " + bDate + " / ");
                  System.out.println("반납기한 : " + bDate);
                  System.out.println("*반납기한을 지키지 않으면 연체일수*2의 기간동안 블랙리스트에 오릅니다.");
                  System.out.println("*블랙리스트 기간동안에는 도서를 대여하실 수 없습니다.");
                  System.out.println();                  
               }
               else
               {
                  System.out.println("시스템 오류로 대여가 되지 않았습니다");
               }               
            }
            catch (Exception e)
            {
               System.out.println("**오류! 이름 또는 전화번호가 입력되지 않았습니다**");
            }
         }
         else
         {
            System.out.println("해당 도서번호가 존재하지 않습니다.");
            return;
         }
      }
      else
      {
         System.out.println("미등록 전화번호이거나 블랙리스트 기간 중인 회원입니다.");
         return;
      }
   }
   //도서 대여기간 연장
   public void adtdateBook()
   {
      try
      {
         System.out.println("**대여 이용자의 전화번호를 누르세요 ex)010-1111-1111");
         String pNum = sc.nextLine();
         if(pNum == "대여목록에 있을 때")
         {
            System.out.println("**연장할 책의 도서번호를 누르세요");
            String bNum = sc.nextLine();
            
            psmt10.setString(1, "반납기한+7");
            psmt10.setString(2, pNum);
            psmt10.setString(3, bNum);
            
            int result10 = psmt10.executeUpdate();
            if(result10 == 1)
            {
               System.out.println("연장 성공");
            }
            else
            {
               System.out.println("대여목록에 없는 도서번호입니다.");
               return;
            }
         }
         else
         {
            System.out.println("대여목록에 없는 회원전화번호입니다.");
            return;
         }
      }
      catch (SQLException e)
      {
         e.printStackTrace();
      }
   }
   
   //도서 반납
   public void rtnBook()
   {
      System.out.println("**반납할 도서번호를 입력후 엔터를 눌러주세요**");
      String isbn = sc.nextLine();
      if(isbn == "도서번호")
      {
         try
         {
            psmt9.setString(1, isbn);
            int updateCount = psmt9.executeUpdate();
            if(updateCount == 1)
            {
               System.out.println("반납되었습니다.");
            }
            else
            {
               System.out.println("대여기록에 없는 도서번호입니다.");
            }
         }
         catch(Exception e)
         {
            System.out.println("시스템 에러입니다");
            return;
         }
      }
   }
      public void connectDatabase()
      {
         try
         {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","Librarian","1234");
            
            String sqlselBook = "select * from BookDB where 도서명 = ? or 도서명 like ? ";
            psmt6 = con.prepareStatement(sqlselBook);
            
            String sqlselName = "select * from BookDB where 저자 = ? or 저자 like ? ";
            psmt7 = con.prepareStatement(sqlselName);
            
            String sqlRental = "insert into RentalDB values(?, ?, ?, ?, ?, ?, ?)";
            psmt8 = con.prepareStatement(sqlRental);
            
            String sqlDelete = "delete from RentalDB where 도서번호 = ?";
            psmt9 = con.prepareStatement(sqlDelete);
            
            String sqladtDate = "update RentalDB set 반납기한 = ? where 전화번호 =? and 도서번호 =?";
            psmt10 = con.prepareStatement(sqladtDate);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
